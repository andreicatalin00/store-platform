package org.store.payment.service;

import org.springframework.stereotype.Service;
import org.store.payment.domain.PaymentStatus;
import org.store.payment.entity.PaymentIntent;
import org.store.payment.repository.PaymentIntentRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PaymentIntentService {
    private final PaymentIntentRepository repository;

    public PaymentIntentService(PaymentIntentRepository repository) {
        this.repository = repository;
    }

    public PaymentIntent create(Long amount, String currency) {

        PaymentIntent pi = new PaymentIntent();
        pi.setAmount(amount);
        pi.setCurrency(currency);
        pi.setStatus(PaymentStatus.CREATED);
        pi.setCreatedAt(LocalDateTime.now());

        return repository.save(pi);
    }

    public Optional<PaymentIntent> findById(Long id) {
        return repository.findById(id);
    }

    public Optional<PaymentIntent> confirm(Long id) {
        final var paymentIntentOptional = repository.findById(id);
        return paymentIntentOptional.map(pi -> {
            // simulate success
            pi.setStatus(PaymentStatus.SUCCEEDED);
            return pi;
        }).map(repository::save);
    }

    public Optional<PaymentIntent> cancel(Long id) {
        final var paymentIntentOptional = repository.findById(id);
        return paymentIntentOptional.map(pi -> {
            pi.setStatus(PaymentStatus.CANCELED);
            return pi;
        }).map(repository::save);
    }
}
