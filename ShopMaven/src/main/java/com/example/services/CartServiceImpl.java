package com.example.services;

import java.util.Optional;

import com.example.models.Cart;
import com.example.models.CartProduct;
import com.example.repositories.CartProductRepository;
import com.example.repositories.CartRepository;

public class CartServiceImpl implements CartService{

    private CartRepository cartRepository;
    private CartProductRepository cartProductRepository;

    public CartServiceImpl(CartRepository cartRepository, CartProductRepository cartProductRepository) {
        this.cartRepository = cartRepository;
        this.cartProductRepository = cartProductRepository;
    }

    @Override
    public void create(Long userId) {
        Cart cart = Cart.builder().userId(userId).build();
        cartRepository.save(cart);
    }

    @Override
    public Optional<Cart> getById(Long id) {
        return cartRepository.findById(id);
    }

    @Override
    public void addProduct(Long cartId, Long productId, Integer amount) {

        cartRepository.findById(cartId)
            .orElseThrow(() ->
                    new IllegalArgumentException("Cart not found with id = " + cartId));

        CartProduct cartProduct = CartProduct.builder()
                .cartId(cartId)
                .productId(productId)
                .count(amount)
                .build();

        cartProductRepository.save(cartProduct);

    }

    

}
