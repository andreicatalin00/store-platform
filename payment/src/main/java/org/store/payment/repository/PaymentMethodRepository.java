package org.store.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.store.payment.entity.PaymentMethod;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {}
