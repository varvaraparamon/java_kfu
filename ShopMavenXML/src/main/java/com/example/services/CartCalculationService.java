package com.example.services;

import com.example.models.Cart;
import com.example.models.CartProduct;
import com.example.models.Product;
import com.example.models.PromoCode;
import com.example.repositories.CartProductRepository;
import com.example.repositories.CartRepository;
import com.example.repositories.ProductRepository;
import com.example.repositories.PromoCodeRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class CartCalculationService {
    private final CartRepository cartRepository;
    private final CartProductRepository cartProductRepository;
    private final ProductRepository productRepository;
    private final PromoCodeRepository promoCodeRepository;

    public double calculateCartTotal(Long cartId) {
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null) return 0.0;

        double total = 0.0;
        List<CartProduct> items = cartProductRepository.findByCartId(cart.getId());

        for (CartProduct item : items) {
            Product product = productRepository.findById(item.getProductId()).orElse(null);
            if (product != null) {
                total += product.getPrice() * item.getCount();
            }
        }

        if (cart.getAppliedPromoCodeId() != null) {
            PromoCode promo = promoCodeRepository.findById(cart.getAppliedPromoCodeId()).orElse(null);
            if (promo != null && isPromoCodeValid(promo)) {
                total = applyPromoDiscount(total, promo);
            }
        }

        return Math.max(total, 0.0);
    }

    public double calculateSubtotal(Long cartId) {
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null) return 0.0;

        return cartProductRepository.findByCartId(cart.getId()).stream()
                .mapToDouble(item -> {
                    Product product = productRepository.findById(item.getProductId()).orElse(null);
                    return product != null ? product.getPrice() * item.getCount() : 0;
                })
                .sum();
    }

    public int getTotalItemsCount(Long cartId) {
        return cartProductRepository.findByCartId(cartId).stream()
                .mapToInt(CartProduct::getCount)
                .sum();
    }

    private boolean isPromoCodeValid(PromoCode promo) {
        return promo.getExpiresAt() == null || promo.getExpiresAt().isAfter(LocalDateTime.now());
    }

    private double applyPromoDiscount(double total, PromoCode promo) {
        switch (promo.getType()) {
            case PERCENT:
                return total * (1.0 - promo.getValue() / 100.0);
            case FIXED:
                return total - promo.getValue();
            default:
                return total;
        }
    }
}
