package org.store.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.store.payment.entity.PaymentIntentEntity;

public interface PaymentIntentRepository extends JpaRepository<PaymentIntentEntity, Long> {}
