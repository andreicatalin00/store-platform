package org.store.payment.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.store.payment.domain.PaymentIntent;
import org.store.payment.dto.ConfirmPaymentIntentRequest;
import org.store.payment.dto.CreatePaymentIntentRequest;
import org.store.payment.service.PaymentIntentService;

@RestController
@RequestMapping("/payment_intents")
public class PaymentIntentController {
    private final PaymentIntentService paymentIntentService;

    public PaymentIntentController(PaymentIntentService paymentIntentService) {
        this.paymentIntentService = paymentIntentService;
    }

    @PostMapping
    public ResponseEntity<PaymentIntent> create(@Valid @RequestBody CreatePaymentIntentRequest request) {
        final var paymentIntent = paymentIntentService.create(request.getAmount(), request.getCurrency());
        return ResponseEntity.ok(paymentIntent);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentIntent> get(@PathVariable Long id) {
        return paymentIntentService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.badRequest().build());
    }

    @PostMapping("/{id}/confirm")
    public ResponseEntity<PaymentIntent> confirm(@PathVariable Long id, @Valid @RequestBody ConfirmPaymentIntentRequest request) {
    return paymentIntentService
        .confirm(id, request.getPaymentMethodId())
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.badRequest().build());
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<PaymentIntent> cancel(@PathVariable Long id) {
        return paymentIntentService.cancel(id).map(ResponseEntity::ok).orElse(ResponseEntity.badRequest().build());
    }
}
