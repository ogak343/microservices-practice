package com.example.customer.service.impl;

import com.example.customer.contants.ErrorMessage;
import com.example.customer.dto.req.CustomerConfirmReq;
import com.example.customer.dto.req.CustomerCreateReq;
import com.example.customer.dto.req.CustomerUpdateReq;
import com.example.customer.dto.req.LoginReq;
import com.example.customer.dto.resp.CustomerResp;
import com.example.customer.dto.resp.LoginResp;
import com.example.customer.entity.OTP;
import com.example.customer.exception.CustomException;
import com.example.customer.mapper.CustomerMapper;
import com.example.customer.repository.CustomerRepository;
import com.example.customer.repository.OTPRepository;
import com.example.customer.service.CustomerService;
import com.example.customer.service.NotificationService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
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
    private final NotificationService notificationService;
    private final BCryptPasswordEncoder encoder;

    @Override
    public Long create(CustomerCreateReq create) {

        if (repository.existsByEmail(create.getEmail()))
            throw new EntityExistsException("Customer with this email already exists");

        var customer = mapper.toEntity(create);
        customer.setPassword(encoder.encode(create.getPassword()));

        customer = repository.save(customer);

        var otp = otpRepository.save(new OTP(customer));
        notificationService.sendOTP(otp);

        return otp.getId();
    }

    @Override
    public LoginResp confirm(CustomerConfirmReq confirmReq) {

        var otp = otpRepository.findById(confirmReq.getOtpId())
                .orElseThrow(() -> new EntityNotFoundException("Invalid OTP"));

        if (otp.getExpiredAt().isBefore(OffsetDateTime.now()))
            throw new CustomException(ErrorMessage.OTP_EXPIRED);

        if (!Objects.equals(otp.getCode(), confirmReq.getCode()))
            throw new CustomException(ErrorMessage.WRONG_OTP_CODE);

        var customer = otp.getCustomer();
        customer.setActive(true);
        repository.save(customer);

        return new LoginResp();
    }

    @Override
    public LoginResp login(LoginReq login) {

        var customer = repository.findByEmail(login.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));

        if (!encoder.matches(login.getPassword(), customer.getPassword()))
            throw new CustomException(ErrorMessage.WRONG_PASSWORD);

        return new LoginResp();
    }

    @Override
    public CustomerResp get(Long id) {

        validateAccess(id);

        return mapper.toResp(repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found")));
    }

    @Override
    public CustomerResp update(CustomerUpdateReq updateDto) {

        validateAccess(updateDto.getId());

        var entity = repository.findById(updateDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));

        mapper.update(entity, updateDto);

        return mapper.toResp(repository.save(entity));
    }

    @Override
    public void delete() {

        repository.deleteById(getCustomerId());

    }

    private Long getCustomerId() {
        try {
            return (Long) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        } catch (ClassCastException e) {
            throw new CustomException(ErrorMessage.INVALID_TOKEN);
        }
    }

    private void validateAccess(Long id) {

        var customerId = getCustomerId();

        if (!Objects.equals(customerId, id))
            throw new CustomException(ErrorMessage.WRONG_CREDENTIALS);
    }
}
