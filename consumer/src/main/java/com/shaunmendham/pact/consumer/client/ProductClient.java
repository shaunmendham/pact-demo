package com.shaunmendham.pact.consumer.client;

import com.shaunmendham.pact.consumer.model.Product;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "product-client", url = "${product.url}")
public interface ProductClient {

    @GetMapping(value = "/all")
    List<Product> getProducts();
}