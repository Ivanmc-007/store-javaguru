package com.ivan.javaguru.store_email_notification.persistence.repository;

import com.ivan.javaguru.store_email_notification.persistence.model.OrderCreatedEventUnique;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderCreatedEventUniqueRepo extends JpaRepository<OrderCreatedEventUnique, Long> {
    Optional<OrderCreatedEventUnique> findByMessageId(String messageId);
}
