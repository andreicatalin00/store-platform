package com.store.order.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String customerId;

  @Column(columnDefinition = "TEXT")
  private String itemsJson;

  private LocalDateTime createdAt;

  @Column(unique = true)
  private String idempotencyKey;

  @PrePersist
  private void onCreate() {
    createdAt = LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS);
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    Order order = (Order) o;
    return Objects.equals(id, order.id)
        && Objects.equals(customerId, order.customerId)
        && Objects.equals(itemsJson, order.itemsJson)
        && Objects.equals(createdAt, order.createdAt)
        && Objects.equals(idempotencyKey, order.idempotencyKey);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, customerId, itemsJson, createdAt, idempotencyKey);
  }
}
