package org.store.payment.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import lombok.*;
import org.store.payment.domain.PaymentStatus;

@Entity
@Table(name = "payment_intents")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentIntentEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long amount;
  private String currency;

  @Enumerated(EnumType.STRING)
  private PaymentStatus status;

  private LocalDateTime createdAt;

  @Column(unique = true)
  private String idempotencyKey;

  @PrePersist
  private void onCreate() {
    createdAt = LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS);
  }
}
