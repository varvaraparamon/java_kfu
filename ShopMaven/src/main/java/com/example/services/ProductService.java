package com.example.services;

import java.util.Optional;

import com.example.models.Product;

public interface ProductService {
    void create(Product product);
    Optional<Product> getById(Long id);
}
