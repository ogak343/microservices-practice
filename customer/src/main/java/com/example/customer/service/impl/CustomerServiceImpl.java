package com.example.customer.service.impl;

import com.example.customer.contants.ErrorCode;
import com.example.customer.dto.KeyCloakCreate;
import com.example.customer.dto.req.CustomerConfirmReq;
import com.example.customer.dto.req.CustomerCreateReq;
import com.example.customer.dto.req.CustomerUpdateReq;
import com.example.customer.dto.req.LoginReq;
import com.example.customer.dto.resp.CustomerResp;
import com.example.customer.dto.resp.ConfirmResp;
import com.example.customer.dto.resp.LoginResp;
import com.example.customer.entity.Customer;
import com.example.customer.entity.OTP;
import com.example.customer.config.exception.CustomException;
import com.example.customer.external.KeyCloakService;
import com.example.customer.mapper.CustomerMapper;
import com.example.customer.repository.CustomerRepository;
import com.example.customer.repository.OTPRepository;
import com.example.customer.service.CustomerService;
import com.example.customer.service.helper.KafkaPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.Objects;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;
    private final CustomerMapper mapper;
    private final OTPRepository otpRepository;
    private final KafkaPublisher kafkaPublisher;
    private final BCryptPasswordEncoder encoder;
    private final KeyCloakService keyCloakService;

    @Autowired
    public CustomerServiceImpl(
            CustomerRepository repository,
            CustomerMapper mapper,
            OTPRepository otpRepository,
            KafkaPublisher kafkaPublisher,
            BCryptPasswordEncoder encoder,
            KeyCloakService keyCloakService
    ) {
        this.repository = repository;
        this.mapper = mapper;
        this.otpRepository = otpRepository;
        this.kafkaPublisher = kafkaPublisher;
        this.encoder = encoder;
        this.keyCloakService = keyCloakService;
    }

    @Override
    public Long create(CustomerCreateReq create) {

        if (repository.existsByEmail(create.getEmail()))
            throw new CustomException(ErrorCode.CUSTOMER_EXISTS);

        var customer = mapper.toEntity(create);

        keyCloakService.createUser(new KeyCloakCreate(create.getEmail(), create.getPassword()));

        customer.setPassword(encoder.encode(create.getPassword()));
        customer = repository.save(customer);

        fetchKeycloakDetails(customer.getEmail());
        var otp = otpRepository.save(new OTP(customer));
        kafkaPublisher.sendOTP(otp);

        return otp.getId();
    }

    @Override
    public ConfirmResp confirm(CustomerConfirmReq confirmReq) {

        var otp = otpRepository.findById(confirmReq.getOtpId())
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_OTP));

        if (otp.getExpiredAt().isBefore(OffsetDateTime.now()))
            throw new CustomException(ErrorCode.OTP_EXPIRED);

        if (!Objects.equals(otp.getCode(), confirmReq.getCode()))
            throw new CustomException(ErrorCode.WRONG_OTP_CODE);

        var customer = otp.getCustomer();
        customer.setActive(true);

        enableKeycloakUser(customer.getKeycloakUserId());

        repository.save(customer);
        otpRepository.deleteOTPByCustomerId(customer.getId());

        return new ConfirmResp(200, "Account has been confirmed");
    }

    @Override
    public LoginResp login(LoginReq dto) {
        return new LoginResp(200, "Login succeeded", keyCloakService.login(dto));
    }

    @Override
    public CustomerResp profile() {
        var customer = getCustomerId();
        return mapper.toResp(getById(customer));
    }

    @Override
    public CustomerResp update(CustomerUpdateReq updateDto) {

        var entity = getById(getCustomerId());

        mapper.update(entity, updateDto);
        entity = repository.save(entity);

        return mapper.toResp(entity);
    }

    @Override
    public void delete() {

        var customer = getById(getCustomerId());
        customer.setActive(false);
        customer.setDeletedAt(new Timestamp(System.currentTimeMillis()));
        keyCloakService.deleteUser(customer.getKeycloakUserId());
        repository.save(customer);
    }

    private String getCustomerId() {

        var jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return jwt.getSubject();
    }

    private Customer getById(String keycloakUserId) {
        return repository.findByKeycloakUserIdAndDeletedAtIsNull(keycloakUserId)
                .orElseThrow(() -> new CustomException(ErrorCode.CUSTOMER_NOT_FOUND));
    }

    @Async(value = "async")
    public void enableKeycloakUser(String userId) {

        keyCloakService.enableUser(userId);
    }

    @Async(value = "async")
    public void fetchKeycloakDetails(String email) {

        var userId = keyCloakService.searchByUsername(email);

        var entity = repository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.CUSTOMER_NOT_FOUND));

        entity.setKeycloakUserId(userId);
        repository.save(entity);

    }
}
