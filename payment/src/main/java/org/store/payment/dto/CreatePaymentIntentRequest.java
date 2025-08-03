package org.store.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreatePaymentIntentRequest {
    private Long amount;
    private String currency;
}
