package com.example.services;

import com.example.models.Cart;
import com.example.models.CartProduct;
import java.util.List;
import java.util.Optional;

public interface CartService {
    Cart createCartForUser(Long userId);
    Optional<Cart> getCartByUserId(Long userId);
    Cart addProductToCart(Long userId, Long productId, Integer count);
    Cart removeProductFromCart(Long userId, Long productId);
    Cart updateProductCount(Long userId, Long productId, Integer count);
    void clearCart(Long userId);
    List<CartProduct> getCartProducts(Long cartId);
}
