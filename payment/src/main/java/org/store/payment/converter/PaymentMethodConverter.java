package org.store.payment.converter;

import org.store.payment.domain.PaymentMethod;
import org.store.payment.entity.PaymentMethodEntity;

public class PaymentMethodConverter {
  private PaymentMethodConverter() {
    // No instantiation
  }

  public static PaymentMethod toDomain(PaymentMethodEntity entity) {
    if (entity == null) return null;

    return PaymentMethod.builder()
        .id(entity.getId())
        .coreUserId(entity.getCoreUserId())
        .cardNumber(entity.getCardNumber())
        .cardExpirationMonth(entity.getCardExpirationMonth())
        .cardExpirationYear(entity.getCardExpirationYear())
        .cardCVC(entity.getCardCVC())
        .type(entity.getType())
        .applePayToken(entity.getApplePayToken())
        .build();
  }

  public static PaymentMethodEntity toEntity(PaymentMethod domain) {
    if (domain == null) return null;
    return PaymentMethodEntity.builder()
        .id(domain.getId())
        .coreUserId(domain.getCoreUserId())
        .cardNumber(domain.getCardNumber())
        .cardExpirationMonth(domain.getCardExpirationMonth())
        .cardExpirationYear(domain.getCardExpirationYear())
        .cardCVC(domain.getCardCVC())
        .type(domain.getType())
        .applePayToken(domain.getApplePayToken())
        .build();
  }
}
