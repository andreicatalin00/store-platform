package org.store.payment.service;

import org.store.payment.dto.CreatePaymentMethodRequest;
import org.store.payment.entity.PaymentMethod;

import java.util.Optional;

public interface PaymentMethodService {
    PaymentMethod create(CreatePaymentMethodRequest createPaymentMethodRequest);
    Optional<PaymentMethod> get(long paymentMethodId);
    void delete(long paymentMethodId);
}
