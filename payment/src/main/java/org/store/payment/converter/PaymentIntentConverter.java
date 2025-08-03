package org.store.payment.converter;

import org.store.payment.domain.PaymentIntent;
import org.store.payment.entity.PaymentIntentEntity;

public class PaymentIntentConverter {
    private PaymentIntentConverter(){
        // No instantiation
    }
    public static PaymentIntent toDomain(PaymentIntentEntity entity) {
        if (entity == null) return null;

        return PaymentIntent.builder()
            .id(entity.getId())
            .amount(entity.getAmount())
            .currency(entity.getCurrency())
            .status(entity.getStatus())
            .createdAt(entity.getCreatedAt())
            .idempotencyKey(entity.getIdempotencyKey())
            .build();
    }

    public static PaymentIntentEntity toEntity(PaymentIntent domain) {
        if (domain == null) return null;

        return PaymentIntentEntity.builder()
                .id(domain.getId())
                .amount(domain.getAmount())
                .currency(domain.getCurrency())
                .status(domain.getStatus())
                .createdAt(domain.getCreatedAt())
                .idempotencyKey(domain.getIdempotencyKey())
                .build();
    }
}
