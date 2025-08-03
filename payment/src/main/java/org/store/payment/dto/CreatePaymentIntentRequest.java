package org.store.payment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreatePaymentIntentRequest {
    @NotNull
    private Long amount;
    @NotBlank
    private String currency;
}
