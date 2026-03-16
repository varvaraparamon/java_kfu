package com.example.repositories;

import com.example.models.Product;
import java.util.List;

public interface ProductRepository extends CrudRepository<Product, Long> {
    List<Product> findByNameContainingIgnoreCase(String name);
}
