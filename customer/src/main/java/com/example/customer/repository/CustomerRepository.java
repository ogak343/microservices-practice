package com.example.customer.repository;

import com.example.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    boolean existsByEmail(String email);

    Optional<Customer> findByIdAndDeletedAtIsNull(Long id);

    Optional<Customer> findByKeycloakUserIdAndDeletedAtIsNull(String id);

    Optional<Customer> findByEmail(String email);

}
