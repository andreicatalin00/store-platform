package org.store.payment.service;

import static org.store.payment.converter.PaymentMethodConverter.toDomain;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.store.payment.converter.PaymentMethodConverter;
import org.store.payment.domain.PaymentMethod;
import org.store.payment.dto.CreatePaymentMethodRequest;
import org.store.payment.entity.PaymentMethodEntity;
import org.store.payment.repository.PaymentMethodRepository;
import org.store.payment.validation.PaymentMethodValidator;

@Service
public class PaymentMethodServiceImpl implements PaymentMethodService {
    private final List<PaymentMethodValidator> validators;
    private final PaymentMethodRepository paymentMethodRepository;

  public PaymentMethodServiceImpl(List<PaymentMethodValidator> validators, PaymentMethodRepository paymentMethodRepository) {
      this.validators = validators;
      this.paymentMethodRepository = paymentMethodRepository;
  }

  @Override
  public PaymentMethod create(CreatePaymentMethodRequest request) {
    final var validatorOptional = validators.stream().filter(validator -> validator.supports(request.getType())).findFirst();

    if (validatorOptional.isEmpty()) {
      throw new IllegalArgumentException("No validator found for type " + request.getType());
    }
    validatorOptional.get().validate(request);

    final var paymentMethod =
        PaymentMethodEntity.builder()
            .coreUserId(request.getCoreUserId())
            .cardNumber(request.getCardNumber())
            .cardExpirationMonth(request.getCardExpirationMonth())
            .cardExpirationYear(request.getCardExpirationYear())
            .cardCVC(request.getCardCVC())
            .applePayToken(request.getApplePayToken())
            .type(request.getType())
            .build();

    final var entityAfterSave = paymentMethodRepository.save(paymentMethod);
    return toDomain(entityAfterSave);
  }

  @Override
  public Optional<PaymentMethod> get(long paymentMethodId) {
    return paymentMethodRepository.findById(paymentMethodId).map(PaymentMethodConverter::toDomain);
  }

  @Override
  public void delete(long paymentMethodId) {
    paymentMethodRepository.deleteById(paymentMethodId);
  }
}
