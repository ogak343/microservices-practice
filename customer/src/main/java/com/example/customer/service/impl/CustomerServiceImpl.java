package com.example.customer.service.impl;

import com.example.customer.contants.ErrorCode;
import com.example.customer.dto.req.CustomerConfirmReq;
import com.example.customer.dto.req.CustomerCreateReq;
import com.example.customer.dto.req.CustomerUpdateReq;
import com.example.customer.dto.resp.CustomerResp;
import com.example.customer.dto.resp.ConfirmResp;
import com.example.customer.entity.Customer;
import com.example.customer.entity.OTP;
import com.example.customer.config.exception.CustomException;
import com.example.customer.mapper.CustomerMapper;
import com.example.customer.repository.CustomerRepository;
import com.example.customer.repository.OTPRepository;
import com.example.customer.service.CustomerService;
import com.example.customer.service.helper.KafkaPublisher;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;
    private final CustomerMapper mapper;
    private final OTPRepository otpRepository;
    private final KafkaPublisher kafkaPublisher;
    private final BCryptPasswordEncoder encoder;
    private final RealmResource realmResource;

    @Override
    public Long create(CustomerCreateReq create) {

        if (repository.existsByEmail(create.getEmail()))
            throw new CustomException(ErrorCode.CUSTOMER_NOT_FOUND);

        var customer = mapper.toEntity(create);

        realmResource.users().create(mapper.toKeycloakCreate(customer));

        customer.setPassword(encoder.encode(create.getPassword()));
        customer = repository.save(customer);

        fetchKeycloakDetails(customer.getEmail(), create.getPassword());
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

        enableKeycloakUser(customer.getEmail());

        repository.save(customer);
        otpRepository.deleteOTPByCustomerId(customer.getId());

        return new ConfirmResp(200, "Account has been confirmed");
    }

    @Override
    public CustomerResp profile() {
        var customer = getCustomerId();
        return mapper.toResp(getById(customer));
    }

    @Override
    public CustomerResp update(CustomerUpdateReq updateDto) {

        validateAccess(updateDto.getId());

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
        repository.save(customer);
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

    private Customer getById(Long id) {
        return repository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new CustomException(ErrorCode.CUSTOMER_NOT_FOUND));
    }

    @Async(value = "async")
    public void enableKeycloakUser(String username) {

        var resp = realmResource.users().searchByUsername(username, true);

        if (resp.isEmpty())
            throw new CustomException(ErrorCode.CUSTOMER_NOT_FOUND);

        var user = resp.get(0);
        user.setEmailVerified(true);
        user.setEnabled(true);

        realmResource.users().get(user.getId()).update(user);
    }

    @Async(value = "async")
    public void fetchKeycloakDetails(String email, String password) {

        var resp = realmResource.users().searchByUsername(email, true);

        if (resp.isEmpty())
            throw new CustomException(ErrorCode.CUSTOMER_NOT_FOUND);

        var entity = repository.findByEmail(email)
                .orElseThrow(()-> new CustomException(ErrorCode.CUSTOMER_NOT_FOUND));

        var data = resp.get(0);

        entity.setKeycloakUserId(data.getId());
        repository.save(entity);
        CredentialRepresentation credentials = new CredentialRepresentation();

        credentials.setTemporary(false);
        credentials.setType(CredentialRepresentation.PASSWORD);
        credentials.setValue(password);
        data.setCredentials(List.of(credentials));

        realmResource.users().get(entity.getKeycloakUserId()).update(data);
    }
}
