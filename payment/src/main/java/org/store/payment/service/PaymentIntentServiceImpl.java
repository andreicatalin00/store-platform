package org.store.payment.service;

import static org.store.payment.converter.PaymentIntentConverter.toDomain;

import java.util.Optional;
import org.springframework.stereotype.Service;
import org.store.payment.converter.PaymentIntentConverter;
import org.store.payment.domain.PaymentIntent;
import org.store.payment.domain.PaymentStatus;
import org.store.payment.entity.PaymentIntentEntity;
import org.store.payment.repository.PaymentIntentRepository;
import org.store.payment.repository.PaymentMethodRepository;

@Service
public class PaymentIntentServiceImpl implements PaymentIntentService {
  private final PaymentIntentRepository paymentIntentRepository;
  private final PaymentMethodRepository paymentMethodRepository;

  public PaymentIntentServiceImpl(
      PaymentIntentRepository paymentIntentRepository,
      PaymentMethodRepository paymentMethodRepository) {
    this.paymentIntentRepository = paymentIntentRepository;
    this.paymentMethodRepository = paymentMethodRepository;
  }

  public PaymentIntent create(Long amount, String currency) {
    final var entity = PaymentIntentEntity.builder().amount(amount).currency(currency).status(PaymentStatus.CREATED).build();

    final var entityAfterSave = paymentIntentRepository.save(entity);
    return toDomain(entityAfterSave);
  }

  public Optional<PaymentIntent> findById(Long id) {
    return paymentIntentRepository.findById(id).map(PaymentIntentConverter::toDomain);
  }

  public Optional<PaymentIntent> confirm(Long id, Long paymentMethodId) {
    final var paymentIntentOptional = paymentIntentRepository.findById(id);
    return paymentIntentOptional
        .map(
            pi ->
                paymentMethodRepository
                    .findById(paymentMethodId)
                    .map(
                        paymentMethod -> {
                          // TODO: Check if the paymentMethod is owned by the coreUserId
                          pi.setStatus(PaymentStatus.SUCCEEDED);
                          return pi;
                        })
                    .orElseGet(
                        () -> {
                          pi.setStatus(PaymentStatus.FAILED);
                          return pi;
                        }))
        .map(paymentIntentRepository::save).map(PaymentIntentConverter::toDomain);
  }

  public Optional<PaymentIntent> cancel(Long id) {
    final var paymentIntentOptional = paymentIntentRepository.findById(id);
    return paymentIntentOptional
        .map(
            pi -> {
              pi.setStatus(PaymentStatus.CANCELED);
              return pi;
            })
        .map(paymentIntentRepository::save).map(PaymentIntentConverter::toDomain);
  }
}
