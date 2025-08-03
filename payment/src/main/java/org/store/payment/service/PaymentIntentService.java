package org.store.payment.service;

import java.util.Optional;

import org.store.payment.domain.PaymentIntent;
import org.store.payment.entity.PaymentIntentEntity;

public interface PaymentIntentService {
  PaymentIntent create(Long amount, String currency);

  Optional<PaymentIntent> findById(Long id);

  Optional<PaymentIntent> confirm(Long id, Long paymentMethodId);

  Optional<PaymentIntent> cancel(Long id);
}
