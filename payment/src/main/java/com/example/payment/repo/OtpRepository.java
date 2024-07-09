package com.example.payment.repo;

import com.example.payment.entity.PaymentOtp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<PaymentOtp, Long> {

    Optional<PaymentOtp> findByIdAndExpiredAtBefore(Long id, OffsetDateTime expiredAt);
}
