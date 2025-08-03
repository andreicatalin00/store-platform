package org.store.payment.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@Entity
@Table(name = "payment_methods")
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethodEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String coreUserId;

  private String cardNumber;

  private int cardExpirationMonth;

  private int cardExpirationYear;

  private int cardCVC;

  private LocalDateTime createdAt;

  @Column(unique = true)
  private String idempotencyKey;

  @PrePersist
  private void onCreate() {
    createdAt = LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS);
  }
}
