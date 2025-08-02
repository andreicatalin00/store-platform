package com.store.order.service;

import com.store.order.entity.OutboxEvent;
import com.store.order.kafka.KafkaPublisher;
import com.store.order.repository.OutboxEventRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OutboxProcessorService {
    private final OutboxEventRepository outboxRepository;
    private final KafkaPublisher kafkaPublisher;

    public OutboxProcessorService(OutboxEventRepository outboxRepository, KafkaPublisher kafkaPublisher) {
        this.outboxRepository = outboxRepository;
        this.kafkaPublisher = kafkaPublisher;
    }

    @Scheduled(fixedDelay = 10_000)
    public void publishOutboxEvents() {
        List<OutboxEvent> events = outboxRepository.findByStatus(OutboxEvent.Status.PENDING);

        for (OutboxEvent event : events) {
            publishEvent(event);
        }
    }

    private void publishEvent(OutboxEvent event) {
        try{
        kafkaPublisher.publish("order-events", event.getPayloadJson());
        event.setStatus(OutboxEvent.Status.COMPLETED);
        outboxRepository.save(event);
        } catch (Exception e){
            log.error("The order event has not been published", e);
        }
    }
}
