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

    public String productToString() {
        return name + " " + description + " " + price;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Product that)) return false;
        return name.equals(that.name)
                && description.equals(that.description)
                && price.equals(that.price);
    }
}
