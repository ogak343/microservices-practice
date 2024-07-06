package com.example.order.repository;

import com.example.order.contants.Status;
import com.example.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByIdAndStatus(Long aLong, Status status);

    boolean existsByIdAndStatus(Long id, Status status);
}
