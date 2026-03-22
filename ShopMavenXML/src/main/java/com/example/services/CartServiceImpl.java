package com.example.services;

import com.example.models.Cart;
import com.example.models.CartProduct;
import com.example.repositories.CartProductRepository;
import com.example.repositories.CartRepository;
import com.example.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final CartProductRepository cartProductRepository;
    private final CartCalculationService calculationService;
    private final CartPromoService promoService;

    public Cart createCartForUser(Long userId) {
        Optional<Cart> existing = cartRepository.findByUserId(userId);
        if (existing.isPresent()) {
            return existing.get();
        }
        Cart cart = Cart.builder()
                .userId(userId)
                .build();
        return cartRepository.save(cart);
    }

    public Optional<Cart> getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId);
    }

    public Cart addProductToCart(Long userId, Long productId, Integer count) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));

        Optional<CartProduct> existing = cartProductRepository
                .findByCartIdAndProductId(cart.getId(), productId);

        if (existing.isPresent()) {
            CartProduct cp = existing.get();
            cp.setCount(cp.getCount() + count);
            cartProductRepository.save(cp);
        } else {
            CartProduct cartProduct = CartProduct.builder()
                    .cartId(cart.getId())
                    .productId(productId)
                    .count(count)
                    .build();
            cartProductRepository.save(cartProduct);
        }

        return cart;
    }

    @Override
    public Cart removeProductFromCart(Long userId, Long productId) {
        Cart cart = getOrCreateCart(userId);
        cartProductRepository.findByCartIdAndProductId(cart.getId(), productId)
                .ifPresent(cartProductRepository::delete);
        return cart;
    }

    @Override
    public Cart updateProductCount(Long userId, Long productId, Integer count) {
        Cart cart = getOrCreateCart(userId);

        if (count <= 0) {
            return removeProductFromCart(userId, productId);
        }

        cartProductRepository.findByCartIdAndProductId(cart.getId(), productId)
                .ifPresent(cp -> {
                    cp.setCount(count);
                    cartProductRepository.save(cp);
                });

        return cart;
    }

    @Override
    public void clearCart(Long userId) {
        cartRepository.findByUserId(userId)
                .ifPresent(cart -> cartProductRepository.deleteByCartId(cart.getId()));
    }

    @Override
    public List<CartProduct> getCartProducts(Long cartId) {
        return cartProductRepository.findByCartId(cartId);
    }

    public double getCartTotal(Long userId) {
        return cartRepository.findByUserId(userId)
                .map(cart -> calculationService.calculateCartTotal(cart.getId()))
                .orElse(0.0);
    }

    public boolean applyPromoToCart(Long userId, String promoCode) {
        return cartRepository.findByUserId(userId)
                .map(cart -> promoService.applyPromoCode(cart.getId(), promoCode))
                .orElse(false);
    }

    public void removePromoFromCart(Long userId) {
        cartRepository.findByUserId(userId)
                .ifPresent(cart -> promoService.removePromoCode(cart.getId()));
    }

    private Cart getOrCreateCart(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> createCartForUser(userId));
    }
}
