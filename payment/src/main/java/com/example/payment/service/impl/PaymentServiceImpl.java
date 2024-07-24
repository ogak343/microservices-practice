package com.example.payment.service.impl;

import com.example.payment.constants.ErrorCode;
import com.example.payment.constants.PaymentStatus;
import com.example.payment.constants.Template;
import com.example.payment.dto.EmailReqDto;
import com.example.payment.dto.PaymentPerformDto;
import com.example.payment.entity.Payment;
import com.example.payment.entity.PaymentOtp;
import com.example.payment.config.exception.CustomException;
import com.example.payment.external.feign.OrderClient;
import com.example.payment.external.feign.dto.OrderResp;
import com.example.payment.external.feign.dto.Status;
import com.example.payment.repo.OtpRepository;
import com.example.payment.repo.PaymentRepository;
import com.example.payment.service.PaymentService;
import com.example.payment.service.helper.KafkaPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Map;

@Service("PAYMENT")
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderClient orderClient;
    private final KafkaPublisher kafkaPublisher;
    private final OtpRepository otpRepository;

    @Override
    public Long create(Long orderId) {

        //Reminder: actual payment logic here
        //It is just a mock payment

        var order = orderClient.getOrder(orderId);

        if (order.getStatus() != Status.WAITING_FOR_PAYMENT)
            throw new CustomException(ErrorCode.INVALID_STATUS);

        var payment = new Payment(order);
        payment = paymentRepository.save(payment);

        var otp = otpRepository.save(new PaymentOtp(payment));

        var email = getEmail();

        kafkaPublisher.publishPaymentOtp(makeOtpDto(otp.getCode(), order, email));
        return otp.getId();
    }

    @Override
    public String perform(PaymentPerformDto dto) {

        //Reminder: actual payment logic here
        //It is just a mock payment

        var optionalOtp = otpRepository.findByIdAndExpiredAtAfter(dto.otpId(), OffsetDateTime.now());

        if (optionalOtp.isEmpty())
            throw new CustomException(ErrorCode.INVALID_OTP);

        var otp = optionalOtp.get();
        if (!otp.getPayment().getPaymentStatus().equals(PaymentStatus.CREATED))
            throw new CustomException(ErrorCode.INVALID_STATUS);

        otp.setConfirmedAt(OffsetDateTime.now());
        var payment = otp.getPayment();

        orderClient.verifyOrder(payment.getOrderId());

        payment.setPaymentStatus(PaymentStatus.PERFORMED);
        payment.setPerformedAt(OffsetDateTime.now());

        paymentRepository.save(payment);
        otpRepository.save(otp);

        var email = getEmail();
        kafkaPublisher.publishPaymentOtp(makeInfoDto(payment, email));

        return "Payment was successful";
    }

    private String getEmail() {
        var jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return jwt.getSubject();
    }

    private EmailReqDto makeOtpDto(Integer otpCode, OrderResp order, String email) {

        StringBuilder description = new StringBuilder();

        order.getProductDetails().forEach(detail ->
                description.append(String.format(", %s %s", detail.quantity(), detail.name()))
        );

        return EmailReqDto
                .builder()
                .receiver(email)
                .title("Payment Details")
                .template(Template.PAYMENT_CREATE)
                .value(Map.of("otpCode", otpCode, "price", "$" + order.getTotalPrice(), "description", description.substring(2)))
                .build();
    }

    private EmailReqDto makeInfoDto(Payment payment, String email) {

        return EmailReqDto
                .builder()
                .receiver(email)
                .title("Payment Details")
                .template(Template.PAYMENT_PERFORM)
                .value(Map.of("message", "Payment performed successfully", "price", "$" + payment.getAmount()))
                .build();
    }
}
