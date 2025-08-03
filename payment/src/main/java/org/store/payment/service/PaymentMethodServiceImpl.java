package org.store.payment.service;

import java.util.Optional;
import org.springframework.stereotype.Service;
import org.store.payment.dto.CreatePaymentMethodRequest;
import org.store.payment.entity.PaymentMethodEntity;
import org.store.payment.repository.PaymentMethodRepository;

@Service
public class PaymentMethodServiceImpl implements PaymentMethodService {
  private final PaymentMethodRepository repository;

  public PaymentMethodServiceImpl(PaymentMethodRepository paymentMethodRepository) {
    repository = paymentMethodRepository;
  }

  @Override
  public PaymentMethodEntity create(CreatePaymentMethodRequest createPaymentMethodRequest) {
    final var paymentMethod =
        PaymentMethodEntity.builder()
            .coreUserId(createPaymentMethodRequest.getCoreUserId())
            .cardNumber(createPaymentMethodRequest.getCardNumber())
            .cardExpirationMonth(createPaymentMethodRequest.getCardExpirationMonth())
            .cardExpirationYear(createPaymentMethodRequest.getCardExpirationYear())
            .cardCVC(createPaymentMethodRequest.getCardCVC())
            .build();

    return repository.save(paymentMethod);
  }

  @Override
  public Optional<PaymentMethodEntity> get(long paymentMethodId) {
    return repository.findById(paymentMethodId);
  }

  @Override
  public void delete(long paymentMethodId) {
      repository.deleteById(paymentMethodId);
  }
}
