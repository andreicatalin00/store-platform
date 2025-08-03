package org.store.payment.service;

import java.util.Optional;
import org.springframework.stereotype.Service;
import org.store.payment.converter.PaymentMethodConverter;
import org.store.payment.domain.PaymentMethod;
import org.store.payment.dto.CreatePaymentMethodRequest;
import org.store.payment.entity.PaymentMethodEntity;
import org.store.payment.repository.PaymentMethodRepository;

import static org.store.payment.converter.PaymentMethodConverter.toDomain;

@Service
public class PaymentMethodServiceImpl implements PaymentMethodService {
  private final PaymentMethodRepository repository;

  public PaymentMethodServiceImpl(PaymentMethodRepository paymentMethodRepository) {
    repository = paymentMethodRepository;
  }

  @Override
  public PaymentMethod create(CreatePaymentMethodRequest createPaymentMethodRequest) {
    final var paymentMethod =
        PaymentMethodEntity.builder()
            .coreUserId(createPaymentMethodRequest.getCoreUserId())
            .cardNumber(createPaymentMethodRequest.getCardNumber())
            .cardExpirationMonth(createPaymentMethodRequest.getCardExpirationMonth())
            .cardExpirationYear(createPaymentMethodRequest.getCardExpirationYear())
            .cardCVC(createPaymentMethodRequest.getCardCVC())
            .build();

    final var entityAfterSave = repository.save(paymentMethod);
    return toDomain(entityAfterSave);
  }

  @Override
  public Optional<PaymentMethod> get(long paymentMethodId) {
    return repository.findById(paymentMethodId).map(PaymentMethodConverter::toDomain);
  }

  @Override
  public void delete(long paymentMethodId) {
      repository.deleteById(paymentMethodId);
  }
}
