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
import org.store.payment.dto.CreatePaymentMethodRequest;
import org.store.payment.entity.PaymentMethod;
import org.store.payment.repository.PaymentMethodRepository;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PaymentMethodControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Test
    void create_createsPaymentMethod() {
        CreatePaymentMethodRequest request = new CreatePaymentMethodRequest(
                "user-123", "4111111111111111", 12, 2030, 123);

        ResponseEntity<PaymentMethod> response = restTemplate.postForEntity(
                baseUrl(), new HttpEntity<>(request, defaultHeaders()), PaymentMethod.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        PaymentMethod created = response.getBody();

        assertThat(created).isNotNull();
        assertThat(created.getCoreUserId()).isEqualTo("user-123");
        assertThat(created.getCardNumber()).isEqualTo("4111111111111111");
    }

    @Test
    void get_returnsPaymentMethod() {
        PaymentMethod saved = PaymentMethod.builder()
                .coreUserId("user-456")
                .cardNumber("4111111111111111")
                .cardExpirationMonth(6)
                .cardExpirationYear(2029)
                .cardCVC(321)
                .build();

        saved = paymentMethodRepository.save(saved);

        ResponseEntity<PaymentMethod> response = restTemplate.getForEntity(
                baseUrl() + "/" + saved.getId(), PaymentMethod.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCoreUserId()).isEqualTo("user-456");
    }

    @Test
    void get_notFound() {
        ResponseEntity<PaymentMethod> response = restTemplate.getForEntity(
                baseUrl() + "/999999", PaymentMethod.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void delete_removesPaymentMethod() {
        PaymentMethod saved = PaymentMethod.builder()
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
                null, "4111111111111111", 12, 2030, 123);

        ResponseEntity<String> response = restTemplate.postForEntity(
                baseUrl(), new HttpEntity<>(request, defaultHeaders()), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void create_missingCardNumber_returnsBadRequest() {
        CreatePaymentMethodRequest request = new CreatePaymentMethodRequest(
                "user-1", null, 12, 2030, 123);

        ResponseEntity<String> response = restTemplate.postForEntity(
                baseUrl(), new HttpEntity<>(request, defaultHeaders()), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void create_missingExpirationMonth_returnsBadRequest() {
        CreatePaymentMethodRequest request = new CreatePaymentMethodRequest(
                "user-2", "4111111111111111", null, 2030, 123); // month out of range

        ResponseEntity<String> response = restTemplate.postForEntity(
                baseUrl(), new HttpEntity<>(request, defaultHeaders()), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void create_missingCardCVC_returnsBadRequest() {
        CreatePaymentMethodRequest request = new CreatePaymentMethodRequest(
                "user-3", "4111111111111111", 5, 2030, null);

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
