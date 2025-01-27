package com.ivan.javaguru.store_order.persistence.repository;

import com.ivan.javaguru.store_order.persistence.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepo extends JpaRepository<Order, Long> {
}
