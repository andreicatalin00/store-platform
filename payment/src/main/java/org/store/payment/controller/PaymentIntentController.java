package org.store.payment.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.store.payment.dto.ConfirmPaymentIntentRequest;
import org.store.payment.dto.CreatePaymentIntentRequest;
import org.store.payment.entity.PaymentIntentEntity;
import org.store.payment.service.PaymentIntentService;

@RestController
@RequestMapping("/payment_intents")
public class PaymentIntentController {
    private final PaymentIntentService paymentIntentService;

    public PaymentIntentController(PaymentIntentService paymentIntentService) {
        this.paymentIntentService = paymentIntentService;
    }

    @PostMapping
    public ResponseEntity<PaymentIntentEntity> create(@Valid @RequestBody CreatePaymentIntentRequest request) {
        final var paymentIntent = paymentIntentService.create(request.getAmount(), request.getCurrency());
        return ResponseEntity.ok(paymentIntent);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentIntentEntity> get(@PathVariable Long id) {
        return paymentIntentService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.badRequest().build());
    }

    @PostMapping("/{id}/confirm")
    public ResponseEntity<PaymentIntentEntity> confirm(@PathVariable Long id, @Valid @RequestBody ConfirmPaymentIntentRequest request) {
    return paymentIntentService
        .confirm(id, request.getPaymentMethodId())
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.badRequest().build());
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<PaymentIntentEntity> cancel(@PathVariable Long id) {
        return paymentIntentService.cancel(id).map(ResponseEntity::ok).orElse(ResponseEntity.badRequest().build());
    }
}
