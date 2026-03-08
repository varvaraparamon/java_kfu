package com.example.models;

import lombok.*;
import javax.persistence.*;

@Entity
@Table(name = "products")
@EqualsAndHashCode
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    private String description;
    
    @Column(nullable = false)
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