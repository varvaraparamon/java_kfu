package com.example.services;

import java.util.Optional;

import com.example.models.Cart;

public interface CartService {
    void create(Long userId);
    Optional<Cart> getById(Long id);
    void addProduct(Long cartId, Long productId, Integer amount);
}
