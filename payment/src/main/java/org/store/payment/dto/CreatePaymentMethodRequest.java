package org.store.payment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreatePaymentMethodRequest {
    @NotBlank
    private String coreUserId;

    @NotBlank
    private String cardNumber;

    @NotNull
    private Integer cardExpirationMonth;

    @NotNull
    private Integer cardExpirationYear;

    @NotNull
    private Integer cardCVC;

}
