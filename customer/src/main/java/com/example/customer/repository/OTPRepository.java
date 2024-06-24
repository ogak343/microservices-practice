package com.example.customer.repository;

import com.example.customer.entity.OTP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface OTPRepository extends JpaRepository<OTP, Long> {

    @Transactional
    @Modifying
    void deleteOTPByCustomerId(Long customerId);
}
