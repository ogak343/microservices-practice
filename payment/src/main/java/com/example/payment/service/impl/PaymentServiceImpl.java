package com.example.payment.service.impl;

import com.example.payment.constants.ErrorCode;
import com.example.payment.constants.PaymentStatus;
import com.example.payment.constants.Type;
import com.example.payment.dto.NotificationDto;
import com.example.payment.dto.PaymentPerformDto;
import com.example.payment.entity.Payment;
import com.example.payment.entity.PaymentOtp;
import com.example.payment.exception.CustomException;
import com.example.payment.external.feign.OrderClient;
import com.example.payment.external.feign.dto.Status;
import com.example.payment.repo.OtpRepository;
import com.example.payment.repo.PaymentRepository;
import com.example.payment.service.PaymentService;
import com.example.payment.service.helper.KafkaPublisher;
import com.example.payment.service.helper.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service("PAYMENT")
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderClient orderClient;
    private final KafkaPublisher kafkaPublisher;
    private final OtpRepository otpRepository;
    private final RedisService redisService;

    @Override
    public Long create(Long orderId) {

        var order = orderClient.getOrder(orderId);

        if (order.getStatus() != Status.WAITING_FOR_PAYMENT) {
            throw new CustomException(ErrorCode.INVALID_STATUS);
        }

        var payment = new Payment(order);
        payment = paymentRepository.save(payment);

        var otp = otpRepository.save(new PaymentOtp(payment));

        var email = getEmail();

        kafkaPublisher.publishPaymentOtp(makeOtpDto(payment, email));
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
        paymentRepository.save(payment);
        otpRepository.save(otp);

        var email = getEmail();
        kafkaPublisher.publishPaymentOtp(makeInfoDto(payment, email));

        return "Payment was successful";
    }

    private String getEmail() {
        Long customerId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return redisService.getMessage(customerId);
    }

    private NotificationDto makeOtpDto(Payment payment, String email) {
        return NotificationDto
                .builder()
                .type(Type.MAIL)
                .receiver(email)
                .message(String.format("Your verification code: %s", payment.getAmount()))
                .build();
    }

    private NotificationDto makeInfoDto(Payment payment, String email) {

        return NotificationDto
                .builder()
                .type(Type.MAIL)
                .receiver(email)
                .message(String.format("Payment with amount %s has been performed", payment.getAmount()))
                .build();
    }
}
