package org.store.payment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.store.payment.domain.PaymentMethodType;

@AllArgsConstructor
@Getter
public class CreatePaymentMethodRequest {
    @NotBlank
    private String coreUserId;
    @NotNull
    private PaymentMethodType type;

    private String cardNumber;
    private Integer cardExpirationMonth;
    private Integer cardExpirationYear;
    private Integer cardCVC;

    private String applePayToken;



}
