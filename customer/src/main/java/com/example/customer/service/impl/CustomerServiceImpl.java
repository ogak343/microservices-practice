package com.example.customer.service.impl;

import com.example.customer.contants.ErrorCode;
import com.example.customer.contants.ClientType;
import com.example.customer.dto.req.CustomerConfirmReq;
import com.example.customer.dto.req.CustomerCreateReq;
import com.example.customer.dto.req.CustomerUpdateReq;
import com.example.customer.dto.req.LoginReq;
import com.example.customer.dto.resp.CustomerResp;
import com.example.customer.dto.resp.LoginResp;
import com.example.customer.entity.OTP;
import com.example.customer.config.exception.CustomException;
import com.example.customer.mapper.CustomerMapper;
import com.example.customer.repository.CustomerRepository;
import com.example.customer.repository.OTPRepository;
import com.example.customer.service.CustomerService;
import com.example.customer.service.JwtService;
import com.example.customer.service.helper.KafkaPublisher;
import com.example.customer.service.helper.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;
    private final CustomerMapper mapper;
    private final OTPRepository otpRepository;
    private final KafkaPublisher kafkaPublisher;
    private final BCryptPasswordEncoder encoder;
    private final JwtService jwtService;
    private final RedisService redisService;

    @Override
    public Long create(CustomerCreateReq create) {

        if (repository.existsByEmail(create.getEmail()))
            throw new CustomException(ErrorCode.CUSTOMER_NOT_FOUND);

        var customer = mapper.toEntity(create);
        customer.setPassword(encoder.encode(create.getPassword()));

        customer = repository.save(customer);

        var otp = otpRepository.save(new OTP(customer));
        kafkaPublisher.sendOTP(otp);

        return otp.getId();
    }

    @Override
    public LoginResp confirm(CustomerConfirmReq confirmReq) {

        var otp = otpRepository.findById(confirmReq.getOtpId())
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_OTP));

        if (otp.getExpiredAt().isBefore(OffsetDateTime.now()))
            throw new CustomException(ErrorCode.OTP_EXPIRED);

        if (!Objects.equals(otp.getCode(), confirmReq.getCode()))
            throw new CustomException(ErrorCode.WRONG_OTP_CODE);

        var customer = otp.getCustomer();
        customer.setActive(true);
        repository.save(customer);
        otpRepository.deleteOTPByCustomerId(customer.getId());

        redisService.save(customer.getId(), customer.getEmail());

        return new LoginResp(200, "Success", jwtService.generateToken(customer.getId(), ClientType.CUSTOMER));
    }

    @Override
    public LoginResp login(LoginReq login) {

        var customer = repository.findByEmailAndActiveTrue(login.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.CUSTOMER_NOT_FOUND));

        if (!encoder.matches(login.getPassword(), customer.getPassword()))
            throw new CustomException(ErrorCode.WRONG_PASSWORD);

        redisService.save(customer.getId(), customer.getEmail());

        return new LoginResp(200, "Success", jwtService.generateToken(customer.getId(), ClientType.CUSTOMER));
    }

    @Override
    public CustomerResp profile() {
        var customer = getCustomerId();
        return mapper.toResp(repository.findById(customer)
                .orElseThrow(() -> new CustomException(ErrorCode.CUSTOMER_NOT_FOUND)));
    }

    @Override
    public CustomerResp update(CustomerUpdateReq updateDto) {

        validateAccess(updateDto.getId());

        var entity = repository.findById(updateDto.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.CUSTOMER_NOT_FOUND));

        mapper.update(entity, updateDto);
        entity = repository.save(entity);

        redisService.save(entity.getId(), entity.getEmail());
        return mapper.toResp(entity);
    }

    @Override
    public void delete() {

        repository.deleteById(getCustomerId());
        redisService.remove(getCustomerId());
    }

    private Long getCustomerId() {
        try {
            return (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (ClassCastException e) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
    }

    private void validateAccess(Long id) {

        var customerId = getCustomerId();

        if (!Objects.equals(customerId, id))
            throw new CustomException(ErrorCode.WRONG_CREDENTIALS);
    }
}
