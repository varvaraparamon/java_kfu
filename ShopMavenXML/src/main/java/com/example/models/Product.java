package com.example.models;

import lombok.*;

@EqualsAndHashCode
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    private Long id;
    private String name;
    private String description;
    private Double price;

    @Override
    public String toString() {
        return name + " " + description + " " + price;
    }
}
