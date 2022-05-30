package com.shaunmendham.pact.provider.model;

import java.util.UUID;
import lombok.Value;

@Value
public class Product {

    UUID id;
    String name;
    String description;
}
