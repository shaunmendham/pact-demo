package com.shaunmendham.pact.consumer.model;

import java.util.UUID;
import lombok.Data;

@Data
public class Product {

    UUID id;
    String name;
    String description;
}
