package org.store.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.store.payment.entity.PaymentMethodEntity;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethodEntity, Long> {}
