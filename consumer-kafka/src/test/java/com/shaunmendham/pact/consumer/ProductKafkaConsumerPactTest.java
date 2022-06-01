package com.shaunmendham.pact.consumer;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import au.com.dius.pact.consumer.dsl.PactBuilder;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.consumer.junit5.ProviderType;
import au.com.dius.pact.core.model.PactSpecVersion;
import au.com.dius.pact.core.model.V4Interaction;
import au.com.dius.pact.core.model.V4Pact;
import au.com.dius.pact.core.model.annotations.Pact;
import au.com.dius.pact.core.model.annotations.PactDirectory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shaunmendham.pact.consumer.consumer.ProductConsumer;
import com.shaunmendham.pact.consumer.model.Product;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@ExtendWith(PactConsumerTestExt.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@PactTestFor(providerName = "product-provider", providerType = ProviderType.ASYNCH, pactVersion = PactSpecVersion.V4)
@PactDirectory("../pacts")
class ProductKafkaConsumerPactTest {

    @Autowired
    private ProductConsumer consumer;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Pact(consumer = "product-consumer-kafka")
    V4Pact productMessage(PactBuilder builder) {
        PactDslJsonBody body = new PactDslJsonBody();
        body.uuid("id");
        body.stringType("name", "Product 1");
        body.stringType("description", "Description 1");

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("Content-Type", "application/json");
        metadata.put("kafka_topic", "products");

        return builder.usingLegacyMessageDsl().expectsToReceive("a product created event").withMetadata(metadata).withContent(body).toPact();
    }

    @Test
    @PactTestFor(pactMethod = "productMessage")
    void shouldReceiveProductMessage(List<V4Interaction.AsynchronousMessage> messages) throws JsonProcessingException {
        Product product = objectMapper.readValue(messages.get(0).asV3Interaction().contentsAsString(), Product.class);

        assertNotNull(product.getId());
        assertEquals("Product 1", product.getName());
        assertEquals("Description 1", product.getDescription());

        assertDoesNotThrow(() -> consumer.consume(product));
    }
}
