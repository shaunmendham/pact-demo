package com.shaunmendham.pact.provider;

import static org.mockito.Mockito.when;

import au.com.dius.pact.provider.junit5.HttpTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import au.com.dius.pact.provider.junitsupport.Consumer;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.loader.PactFolder;
import com.shaunmendham.pact.provider.model.Product;
import com.shaunmendham.pact.provider.service.ProductService;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@Provider("product-provider")
@SpringBootTest(classes = ProviderApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@PactFolder("../pacts")
@Consumer("product-consumer-rest")
class ProductsRestPactProviderTest {

    @LocalServerPort
    int randomServerPort;

    @MockBean
    ProductService productService;

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider.class)
    void pactVerificationTestTemplate(PactVerificationContext context) {
        if (context != null) {
            context.verifyInteraction();
        }
    }

    @BeforeEach
    void before(PactVerificationContext context) {
         context.setTarget(new HttpTestTarget("localhost", randomServerPort, "/"));
    }

    @State("Products exist")
    public void givenProductsExist() {
        when(productService.getProducts()).thenReturn(List.of(
            new Product(UUID.randomUUID(), "Product 1", "Description 1"),
            new Product(UUID.randomUUID(), "Product 2", "Description 2"),
            new Product(UUID.randomUUID(), "Product 3", "Description 3")
        ));
    }
}