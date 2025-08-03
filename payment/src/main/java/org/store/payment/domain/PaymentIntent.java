package org.store.payment.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PaymentIntent {
  private Long id;
  private Long amount;
  private String currency;
  private PaymentStatus status;
  private LocalDateTime createdAt;
  private String idempotencyKey;
}
