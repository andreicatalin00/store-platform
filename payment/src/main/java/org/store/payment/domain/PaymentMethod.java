package org.store.payment.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethod {
  private Long id;
  private String coreUserId;

  private String cardNumber;

  private int cardExpirationMonth;

  private int cardExpirationYear;

  private int cardCVC;

  private LocalDateTime createdAt;

  private String idempotencyKey;
}
