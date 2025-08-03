package org.store.payment.service;

import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.store.payment.domain.PaymentStatus;
import org.store.payment.entity.PaymentIntentEntity;
import org.store.payment.repository.PaymentIntentRepository;
import org.store.payment.repository.PaymentMethodRepository;

@Service
public class PaymentIntentServiceImpl implements PaymentIntentService{
  private final PaymentIntentRepository paymentIntentRepository;
  private final PaymentMethodRepository paymentMethodRepository;

  public PaymentIntentServiceImpl(
      PaymentIntentRepository paymentIntentRepository,
      PaymentMethodRepository paymentMethodRepository) {
    this.paymentIntentRepository = paymentIntentRepository;
    this.paymentMethodRepository = paymentMethodRepository;
  }

  public PaymentIntentEntity create(Long amount, String currency) {
    PaymentIntentEntity pi = new PaymentIntentEntity();
    pi.setAmount(amount);
    pi.setCurrency(currency);
    pi.setStatus(PaymentStatus.CREATED);
    pi.setCreatedAt(LocalDateTime.now());

    return paymentIntentRepository.save(pi);
  }

  public Optional<PaymentIntentEntity> findById(Long id) {
    return paymentIntentRepository.findById(id);
  }

  public Optional<PaymentIntentEntity> confirm(Long id, Long paymentMethodId) {
    final var paymentIntentOptional = paymentIntentRepository.findById(id);
    return paymentIntentOptional
        .map(
            pi ->
                paymentMethodRepository
                    .findById(paymentMethodId)
                    .map(
                        paymentMethod -> {
                            //TODO: Check if the paymentMethod is owned by the coreUserId 
                          pi.setStatus(PaymentStatus.SUCCEEDED);
                          return pi;
                        })
                    .orElseGet(
                        () -> {
                          pi.setStatus(PaymentStatus.FAILED);
                          return pi;
                        }))
        .map(paymentIntentRepository::save);
  }

  public Optional<PaymentIntentEntity> cancel(Long id) {
    final var paymentIntentOptional = paymentIntentRepository.findById(id);
    return paymentIntentOptional
        .map(
            pi -> {
              pi.setStatus(PaymentStatus.CANCELED);
              return pi;
            })
        .map(paymentIntentRepository::save);
  }
}
