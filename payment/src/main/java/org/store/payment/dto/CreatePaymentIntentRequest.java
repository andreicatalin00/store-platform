package org.store.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreatePaymentIntentRequest {
    Long amount;
    String currency;
}
