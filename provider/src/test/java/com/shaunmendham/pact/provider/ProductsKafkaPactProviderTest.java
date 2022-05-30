package com.shaunmendham.pact.provider;

import static org.mockito.Mockito.verify;

import au.com.dius.pact.provider.MessageAndMetadata;
import au.com.dius.pact.provider.PactVerifyProvider;
import au.com.dius.pact.provider.junit5.MessageTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.loader.PactFilter;
import au.com.dius.pact.provider.junitsupport.loader.PactFolder;
import com.shaunmendham.pact.provider.model.Product;
import com.shaunmendham.pact.provider.service.ProductService;
import java.util.HashMap;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {ProviderApplication.class}, webEnvironment = WebEnvironment.RANDOM_PORT)
@Provider("product-provider")
@PactFolder("../pacts")
@PactFilter(filter = ByInteractionType.class, value = "Asynchronous/Messages")
class ProductsKafkaPactProviderTest {

    @MockBean
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ProductService productService;

    @Captor
    private ArgumentCaptor<Message<String>> messageArgumentCaptor;


    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider.class)
    void pactVerificationTestTemplate(PactVerificationContext context) {
        context.verifyInteraction();
    }

    @BeforeEach
    void before(PactVerificationContext context) {
        context.setTarget(new MessageTestTarget());
    }

    @PactVerifyProvider("a product created event")
    public MessageAndMetadata orderCreatedEvent() {

        productService.publishProductCreation(new Product(UUID.randomUUID(), "Name", "Description"));

        verify(kafkaTemplate).send(messageArgumentCaptor.capture());
        Message<String> message = messageArgumentCaptor.getValue();

        return generateMessageAndMetadata(message);
    }

    private MessageAndMetadata generateMessageAndMetadata(Message<String> message) {
        HashMap<String, Object> metadata = new HashMap<>(message.getHeaders());
        return new MessageAndMetadata(message.getPayload().getBytes(), metadata);
    }
}