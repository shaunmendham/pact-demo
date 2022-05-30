package com.shaunmendham.pact.provider.api;

import com.shaunmendham.pact.provider.model.Product;
import com.shaunmendham.pact.provider.service.ProductService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/all")
    public List<Product> getProducts() {
        return productService.getProducts();
    }
}
