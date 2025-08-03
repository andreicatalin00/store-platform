package org.store.payment.validation;

import org.springframework.stereotype.Component;
import org.store.payment.domain.PaymentMethodType;
import org.store.payment.dto.CreatePaymentMethodRequest;

@Component
public class ApplePayValidator implements PaymentMethodValidator {
  @Override
  public boolean supports(PaymentMethodType paymentMethodType) {
    return PaymentMethodType.APPLE_PAY.equals(paymentMethodType);
  }

  @Override
  public void validate(CreatePaymentMethodRequest request) {
    if (request.getApplePayToken() == null || request.getApplePayToken().isEmpty()) {
      throw new IllegalArgumentException("Apple Pay token is required");
    }
  }
}
