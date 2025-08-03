package org.store.payment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.store.payment.dto.CreatePaymentIntentRequest;
import org.store.payment.entity.PaymentIntent;
import org.store.payment.service.PaymentIntentService;

@RestController
@RequestMapping("/payment_intents")
public class PaymentIntentController {
    private final PaymentIntentService service;

    public PaymentIntentController(PaymentIntentService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<PaymentIntent> create(@RequestBody CreatePaymentIntentRequest request) {
        if(request == null){
            return ResponseEntity.badRequest().build();
        }
        if(request.getAmount() == null){
            return ResponseEntity.badRequest().build();
        }
        if(request.getCurrency() == null){
            return ResponseEntity.badRequest().build();
        }

        final var paymentIntent = service.create(request.getAmount(), request.getCurrency());
        return ResponseEntity.ok(paymentIntent);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentIntent> get(@PathVariable Long id) {
        return service.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.badRequest().build());
    }

    @PostMapping("/{id}/confirm")
    public ResponseEntity<PaymentIntent> confirm(@PathVariable Long id) {
        return service.confirm(id).map(ResponseEntity::ok).orElse(ResponseEntity.badRequest().build());
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<PaymentIntent> cancel(@PathVariable Long id) {
        return service.cancel(id).map(ResponseEntity::ok).orElse(ResponseEntity.badRequest().build());
    }
}
