package com.shaunmendham.pact.consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import au.com.dius.pact.consumer.dsl.PactBuilder;
import au.com.dius.pact.consumer.dsl.PactDslJsonArray;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.PactSpecVersion;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.V4Pact;
import au.com.dius.pact.core.model.annotations.Pact;
import au.com.dius.pact.core.model.annotations.PactDirectory;
import com.shaunmendham.pact.consumer.client.ProductClient;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(PactConsumerTestExt.class)
@ExtendWith(SpringExtension.class)
@PactTestFor(providerName = "product-provider", port = "8888", pactVersion = PactSpecVersion.V4)
@SpringBootTest(classes = ConsumerApplication.class, properties = {
    "product.url: http://localhost:8888/product"
})
@PactDirectory("../pacts")
class ProductRestConsumerPactTest {

    @Autowired
    private ProductClient productClient;

    @Pact(consumer = "product-consumer-rest")
    public V4Pact getAllProducts(PactBuilder builder) {
        return builder
            .usingLegacyDsl()
            .given("Products exist")
            .uponReceiving("A request to get products")
            .path("/product/all")
            .method("GET")
            .willRespondWith()
            .status(200)
            .body(PactDslJsonArray.arrayEachLike()
                .uuid("id", UUID.randomUUID())
                .stringValue("name", "Product 1")
                .stringValue("description", "Description 1"))
            .toPact(V4Pact.class);
    }

    @PactTestFor(pactMethod = "getAllProducts")
    @Test
    void shouldGetAllProducts_Pass() {
        var result = productClient.getProducts();
        assertNotNull(result);
        assertEquals(1, result.size());
    }
}