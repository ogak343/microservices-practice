package com.example.payment.service;

import com.example.payment.constants.ErrorCode;
import com.example.payment.constants.PaymentStatus;
import com.example.payment.constants.Type;
import com.example.payment.dto.NotificationDto;
import com.example.payment.dto.PaymentPerformDto;
import com.example.payment.entity.Payment;
import com.example.payment.entity.PaymentOtp;
import com.example.payment.exception.CustomException;
import com.example.payment.external.feign.CustomerClient;
import com.example.payment.external.feign.OrderClient;
import com.example.payment.external.feign.Status;
import com.example.payment.external.feign.dto.CustomerResp;
import com.example.payment.repo.OtpRepository;
import com.example.payment.repo.PaymentRepository;
import com.example.payment.service.helper.KafkaPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service("PAYMENT")
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository repository;
    private final OrderClient orderClient;
    private final KafkaPublisher kafkaPublisher;
    private final OtpRepository otpRepository;
    private final CustomerClient customerClient;

    @Override
    public Long create(Long orderId) {

        var order = orderClient.getOrder(orderId);

        if (order.getStatus() != Status.WAITING_FOR_PAYMENT) {
            throw new CustomException(ErrorCode.INVALID_STATUS);
        }

        var payment = new Payment(order);
        payment = repository.save(payment);

        var otp = otpRepository.save(new PaymentOtp(payment));

        var customer = customerClient.profile(); //TODO need implementation for caching of customer e-address

        kafkaPublisher.publishPaymentOtp(makeOtpDto(payment, customer));
        return otp.getId();
    }

    @Override
    public String perform(PaymentPerformDto dto) {

        var optionalOtp = otpRepository.findByIdAndExpiredAtBefore(dto.getOtpId(), OffsetDateTime.now());

        if (optionalOtp.isEmpty())
            throw new CustomException(ErrorCode.INVALID_OTP);

        var otp = optionalOtp.get();
        if (!otp.getPayment().getPaymentStatus().equals(PaymentStatus.CREATED))
            throw new CustomException(ErrorCode.INVALID_STATUS);

        otp.setConfirmedAt(OffsetDateTime.now());
        var payment = otp.getPayment();

        payment.setPaymentStatus(PaymentStatus.PERFORMED);
        payment.setPerformedAt(OffsetDateTime.now());

        orderClient.verifyOrder(payment.getOrderId());

        CustomerResp customer = customerClient.profile();

        repository.save(payment);

        otpRepository.save(otp);

        kafkaPublisher.publishPaymentOtp(makeInfoDto(payment, customer));

        return "Payment is being performed";
    }

    private NotificationDto makeOtpDto(Payment payment, CustomerResp customer) {
        return NotificationDto
                .builder()
                .type(Type.MAIL_WITH_ATTACHMENT)
                .receiver(customer.getEmail())
                .message(String.format("Your verification code: %s", payment.getAmount()))
                .build();
    }

    private NotificationDto makeInfoDto(Payment payment, CustomerResp customer) {

        return NotificationDto
                .builder()
                .type(Type.MAIL_WITH_ATTACHMENT)
                .receiver(customer.getEmail())
                .message(String.format("Payment with amount %s has been performed", payment.getAmount()))
                .build();
    }
}
