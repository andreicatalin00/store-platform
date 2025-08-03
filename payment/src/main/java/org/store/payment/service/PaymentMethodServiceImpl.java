package org.store.payment.service;

import java.util.Optional;
import org.springframework.stereotype.Service;
import org.store.payment.dto.CreatePaymentMethodRequest;
import org.store.payment.entity.PaymentMethod;
import org.store.payment.repository.PaymentMethodRepository;

@Service
public class PaymentMethodServiceImpl implements PaymentMethodService {
  private final PaymentMethodRepository repository;

  public PaymentMethodServiceImpl(PaymentMethodRepository paymentMethodRepository) {
    repository = paymentMethodRepository;
  }

  @Override
  public PaymentMethod create(CreatePaymentMethodRequest createPaymentMethodRequest) {
    final var paymentMethod =
        PaymentMethod.builder()
            .coreUserId(createPaymentMethodRequest.getCoreUserId())
            .cardNumber(createPaymentMethodRequest.getCardNumber())
            .cardExpirationMonth(createPaymentMethodRequest.getCardExpirationMonth())
            .cardExpirationYear(createPaymentMethodRequest.getCardExpirationYear())
            .cardCVC(createPaymentMethodRequest.getCardCVC())
            .build();

    return repository.save(paymentMethod);
  }

  @Override
  public Optional<PaymentMethod> get(long paymentMethodId) {
    return repository.findById(paymentMethodId);
  }

  @Override
  public void delete(long paymentMethodId) {
      repository.deleteById(paymentMethodId);
  }
}
