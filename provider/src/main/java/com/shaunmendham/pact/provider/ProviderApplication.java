package com.shaunmendham.pact.provider;

import com.shaunmendham.pact.provider.model.Product;
import com.shaunmendham.pact.provider.service.ProductService;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@AllArgsConstructor
@SpringBootApplication
public class ProviderApplication {

    private final ProductService productService;

    public static void main(String[] args) {
        SpringApplication.run(ProviderApplication.class, args);
    }

}
