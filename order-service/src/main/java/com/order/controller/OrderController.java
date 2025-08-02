package com.order.controller;

import com.order.dto.OrderRequest;
import com.order.entity.Order;
import com.order.entity.OutboxEvent;
import com.order.repository.OrderRepository;
import com.order.repository.OutboxEventRepository;

import java.util.*;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {
  private final OrderRepository orderRepository;
  private final OutboxEventRepository outboxRepository;

  public OrderController(OrderRepository orderRepository, OutboxEventRepository outboxRepository) {
    this.orderRepository = orderRepository;
    this.outboxRepository = outboxRepository;
  }

  @PostMapping
  @Transactional
  public ResponseEntity<Order> createOrder(
      @RequestHeader(value = "X-Idempotency-Key", required = false) String idempotencyKey,
      @RequestBody OrderRequest orderRequest) {
    if (idempotencyKey != null) {
      Optional<Order> existingOrder = orderRepository.findByIdempotencyKey(idempotencyKey);
      if (existingOrder.isPresent()) {
        return ResponseEntity.ok(existingOrder.get());
      }
    }

    Order order = new Order();
    order.setCustomerId(orderRequest.getCustomerId());
    order.setItemsJson(orderRequest.getItemsJson());
    order.setIdempotencyKey(idempotencyKey);

    order = orderRepository.save(order);

    OutboxEvent event = new OutboxEvent();
    event.setOrderId(order.getId());
    event.setPayloadJson("{\"orderId\": " + order.getId() + ", \"event\": \"ORDER_CREATED\"}");
    outboxRepository.save(event);

    return ResponseEntity.ok(order);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
    return orderRepository
        .findById(id)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }
}
