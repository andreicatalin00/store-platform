package org.store.payment.validation;

import org.store.payment.domain.PaymentMethodType;
import org.store.payment.dto.CreatePaymentMethodRequest;

public interface PaymentMethodValidator {
    boolean supports(PaymentMethodType paymentMethodType);
    void validate(CreatePaymentMethodRequest request);
}
