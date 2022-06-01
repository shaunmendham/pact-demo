package com.shaunmendham.pact.consumer.consumer;

import com.shaunmendham.pact.consumer.model.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableKafka
@RequiredArgsConstructor
public class ProductConsumer {

    private static final String TOPIC = "products";

    @KafkaListener(topics = TOPIC)
    public void consume(Product product) {
        log.info("Received product event: {}", product);
    }
}