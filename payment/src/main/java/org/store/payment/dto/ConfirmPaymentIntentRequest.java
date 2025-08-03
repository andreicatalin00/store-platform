package org.store.payment.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ConfirmPaymentIntentRequest {
  @NotNull private Long paymentMethodId;
}
