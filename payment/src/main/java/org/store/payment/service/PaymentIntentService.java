package org.store.payment.service;

import java.util.Optional;

import org.store.payment.entity.PaymentIntentEntity;

public interface PaymentIntentService {
  PaymentIntentEntity create(Long amount, String currency);

  Optional<PaymentIntentEntity> findById(Long id);

  Optional<PaymentIntentEntity> confirm(Long id, Long paymentMethodId);

  Optional<PaymentIntentEntity> cancel(Long id);
}
