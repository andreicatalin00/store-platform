package org.store.payment.service;

import org.store.payment.dto.CreatePaymentMethodRequest;
import org.store.payment.entity.PaymentMethodEntity;

import java.util.Optional;

public interface PaymentMethodService {
    PaymentMethodEntity create(CreatePaymentMethodRequest createPaymentMethodRequest);
    Optional<PaymentMethodEntity> get(long paymentMethodId);
    void delete(long paymentMethodId);
}
