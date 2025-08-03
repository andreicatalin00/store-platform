package org.store.payment.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.store.payment.dto.CreatePaymentMethodRequest;
import org.store.payment.entity.PaymentMethod;
import org.store.payment.service.PaymentMethodService;

@Slf4j
@RestController
@RequestMapping("/payment_methods")
public class PaymentMethodController {
    private final PaymentMethodService paymentMethodService;

    public PaymentMethodController(PaymentMethodService paymentMethodService) {
        this.paymentMethodService = paymentMethodService;
    }

    @PostMapping
    public ResponseEntity<PaymentMethod> create(@Valid @RequestBody CreatePaymentMethodRequest request) {
        log.error("abc {}", request);
        final var paymentMethod = paymentMethodService.create(request);
        return ResponseEntity.ok(paymentMethod);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentMethod> get(@PathVariable Long id) {
        return paymentMethodService.get(id).map(ResponseEntity::ok).orElse(ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        paymentMethodService.delete(id);

        return ResponseEntity.ok().build();
    }
}
