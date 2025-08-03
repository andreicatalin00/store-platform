package org.store.payment.validation;

import org.springframework.stereotype.Component;
import org.store.payment.domain.PaymentMethodType;
import org.store.payment.dto.CreatePaymentMethodRequest;

@Component
public class CardPaymentValidator implements PaymentMethodValidator {
    @Override
    public boolean supports(PaymentMethodType paymentMethodType) {
        return PaymentMethodType.CARD.equals(paymentMethodType);
    }

    @Override
    public void validate(CreatePaymentMethodRequest request) {
        if (request.getCardNumber() == null || request.getCardNumber().isEmpty()) {
            throw new IllegalArgumentException("Card number is required");
        }
        if (request.getCardExpirationMonth() < 1 || request.getCardExpirationMonth() > 12) {
            throw new IllegalArgumentException("Invalid expiration month");
        }
        if (request.getCardExpirationYear() < 2023) {
            throw new IllegalArgumentException("Invalid expiration year");
        }
        if (request.getCardCVC() <= 0) {
            throw new IllegalArgumentException("Invalid CVC");
        }
    }
}