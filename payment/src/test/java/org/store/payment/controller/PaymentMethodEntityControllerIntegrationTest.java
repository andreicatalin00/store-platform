package org.store.payment.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.store.payment.domain.PaymentMethodType;
import org.store.payment.dto.CreatePaymentMethodRequest;
import org.store.payment.entity.PaymentMethodEntity;
import org.store.payment.repository.PaymentMethodRepository;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PaymentMethodEntityControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Test
    void create_createsPaymentMethod() {
        CreatePaymentMethodRequest request = new CreatePaymentMethodRequest(
                "user-123", PaymentMethodType.CARD,"4111111111111111", 12, 2030, 123, null);

        ResponseEntity<PaymentMethodEntity> response = restTemplate.postForEntity(
                baseUrl(), new HttpEntity<>(request, defaultHeaders()), PaymentMethodEntity.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        PaymentMethodEntity created = response.getBody();

        assertThat(created).isNotNull();
        assertThat(created.getCoreUserId()).isEqualTo("user-123");
        assertThat(created.getCardNumber()).isEqualTo("4111111111111111");
    }

    @Test
    void get_returnsPaymentMethod() {
        PaymentMethodEntity saved = PaymentMethodEntity.builder()
                .coreUserId("user-456")
                .cardNumber("4111111111111111")
                .cardExpirationMonth(6)
                .cardExpirationYear(2029)
                .cardCVC(321)
                .build();

        saved = paymentMethodRepository.save(saved);

        ResponseEntity<PaymentMethodEntity> response = restTemplate.getForEntity(
                baseUrl() + "/" + saved.getId(), PaymentMethodEntity.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCoreUserId()).isEqualTo("user-456");
    }

    @Test
    void get_notFound() {
        ResponseEntity<PaymentMethodEntity> response = restTemplate.getForEntity(
                baseUrl() + "/999999", PaymentMethodEntity.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void delete_removesPaymentMethod() {
        PaymentMethodEntity saved = PaymentMethodEntity.builder()
                .coreUserId("user-789")
                .cardNumber("4000000000000002")
                .cardExpirationMonth(10)
                .cardExpirationYear(2032)
                .cardCVC(999)
                .build();

        saved = paymentMethodRepository.save(saved);

        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl() + "/" + saved.getId(), HttpMethod.DELETE, null, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(paymentMethodRepository.findById(saved.getId())).isEmpty();
    }

    @Test
    void create_missingCoreUserId_returnsBadRequest() {
        CreatePaymentMethodRequest request = new CreatePaymentMethodRequest(
                null, PaymentMethodType.CARD,"4111111111111111", 12, 2030, 123, null);

        ResponseEntity<String> response = restTemplate.postForEntity(
                baseUrl(), new HttpEntity<>(request, defaultHeaders()), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void create_missingType_returnsBadRequest() {
        CreatePaymentMethodRequest request = new CreatePaymentMethodRequest(
                "123", null,"4111111111111111", 12, 2030, 123, null);

        ResponseEntity<String> response = restTemplate.postForEntity(
                baseUrl(), new HttpEntity<>(request, defaultHeaders()), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Idempotency-Key", UUID.randomUUID().toString());
        return headers;
    }

    private String baseUrl() {
        return "http://localhost:" + port + "/payment_methods";
    }
}
