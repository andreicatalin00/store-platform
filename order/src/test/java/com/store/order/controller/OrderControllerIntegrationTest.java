package com.store.order.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.store.order.dto.OrderRequest;
import com.store.order.entity.Order;
import com.store.order.repository.OrderRepository;
import com.store.order.repository.OutboxEventRepository;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderControllerIntegrationTest {

  @LocalServerPort private int port;

  @Autowired private TestRestTemplate restTemplate;

  @Autowired private OrderRepository orderRepository;

  @Autowired private OutboxEventRepository outboxEventRepository;

  @Test
  void createOrder_createsOrderAndOutboxEvent() {
    OrderRequest request = new OrderRequest("customer-1", "[{\"item\":\"apple\",\"qty\":2}]");

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<OrderRequest> entity = new HttpEntity<>(request, headers);

    ResponseEntity<Order> response = restTemplate.postForEntity(baseUrl(), entity, Order.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    Order created = response.getBody();
    assertThat(created).isNotNull();
    assertThat(created.getCustomerId()).isEqualTo("customer-1");

    assertThat(outboxEventRepository.findAll())
        .anyMatch(event -> event.getOrderId().equals(created.getId()));
  }

  @Test
  void createOrder_withIdempotencyKey_returnsSameOrder() {
    OrderRequest orderRequest = new OrderRequest("customer-2", "[{\"item\":\"banana\",\"qty\":3}]");
    HttpEntity<OrderRequest> entity = createHttpEntity(orderRequest);

    ResponseEntity<Order> firstResponse =
        restTemplate.postForEntity(baseUrl(), entity, Order.class);
    ResponseEntity<Order> secondResponse =
        restTemplate.postForEntity(baseUrl(), entity, Order.class);

    Order order1 = firstResponse.getBody();
    Order order2 = secondResponse.getBody();

    assertThat(order1)
            .isEqualTo(order2);
  }

  @Test
  void createOrder_withDifferentIdempotencyKey_returnsDifferentOrder() {
    OrderRequest orderRequest = new OrderRequest("customer-2", "[{\"item\":\"banana\",\"qty\":3}]");

    ResponseEntity<Order> firstResponse =
        restTemplate.postForEntity(baseUrl(), createHttpEntity(orderRequest), Order.class);
    ResponseEntity<Order> secondResponse =
        restTemplate.postForEntity(baseUrl(), createHttpEntity(orderRequest), Order.class);

      Order order1 = firstResponse.getBody();
      Order order2 = secondResponse.getBody();

      assertThat(order1).isNotNull();
      assertThat(order2).isNotNull();
      assertThat(order1.getId()).isNotEqualTo(order2.getId());
    assertThat(order1)
            .usingRecursiveComparison()
            .ignoringFields("id", "idempotencyKey", "createdAt")
            .isEqualTo(order2);
  }

  @Test
  void getOrderById_returnsOrder() {
    Order order = new Order();
    order.setCustomerId("customer-3");
    order.setItemsJson("[{\"item\":\"pear\",\"qty\":1}]");
    orderRepository.save(order);

    ResponseEntity<Order> response =
        restTemplate.getForEntity(baseUrl() + "/" + order.getId(), Order.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getCustomerId()).isEqualTo("customer-3");
  }

  @Test
  void getOrderById_notFound() {
    ResponseEntity<Order> response = restTemplate.getForEntity(baseUrl() + "/999999", Order.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  private static HttpEntity<OrderRequest> createHttpEntity(OrderRequest orderRequest) {
    String idempotencyKey = String.valueOf(UUID.randomUUID());

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set("Idempotency-Key", idempotencyKey);

      return new HttpEntity<>(orderRequest, headers);
  }

  private String baseUrl() {
    return "http://localhost:" + port + "/orders";
  }
}
