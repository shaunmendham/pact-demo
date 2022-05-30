package com.shaunmendham.pact.provider.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shaunmendham.pact.provider.model.Product;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class ProductService {

    private static final String TOPIC = "products";
    private static final String HEADER_CONTENT_TYPE = "Content-Type";
    private static final String HEADER_CONTENT_TYPE_VALUE = "application/json; charset=utf-8";


    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void publishProductCreation(Product product) {
        try {
            String productJson = objectMapper.writeValueAsString(product);

            Message<String> message = MessageBuilder.withPayload(productJson)
                .setHeader(KafkaHeaders.TOPIC, TOPIC)
                .setHeader(HEADER_CONTENT_TYPE, HEADER_CONTENT_TYPE_VALUE)
                .build();

            kafkaTemplate.send(message);
        } catch (JsonProcessingException e) {
            log.error("Error while publishing product creation", e);
        }
    }

    public List<Product> getProducts() {
        return List.of(
            new Product(UUID.randomUUID(), "Product 1", "Description 1"),
            new Product(UUID.randomUUID(), "Product 2", "Description 2"),
            new Product(UUID.randomUUID(), "Product 3", "Description 3")
        );
    }
}
