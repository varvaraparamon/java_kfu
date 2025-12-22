package com.example.services;

import com.example.models.Cart;
import com.example.models.CartProduct;
import com.example.models.Product;
import com.example.models.PromoCode;
import com.example.repositories.CartProductRepository;
import com.example.repositories.CartRepository;
import com.example.repositories.ProductRepository;
import com.example.repositories.PromoCodeRepository;

import java.util.List;
import java.util.Optional;

public class CartCalculationService {

    private final CartRepository cartRepository;
    private final CartProductRepository cartProductRepository;
    private final ProductRepository productRepository;
    private final PromoCodeRepository promoCodeRepository;

    public CartCalculationService(CartRepository cartRepository,
                                  CartProductRepository cartProductRepository,
                                  ProductRepository productRepository,
                                  PromoCodeRepository promoCodeRepository) {
        this.cartRepository = cartRepository;
        this.cartProductRepository = cartProductRepository;
        this.productRepository = productRepository;
        this.promoCodeRepository = promoCodeRepository;
    }

    public double calculateCartTotal(Long cartId) {
        Optional<Cart> cartOpt = cartRepository.findById(cartId);
        if (cartOpt.isEmpty()) return 0.0;

        Cart cart = cartOpt.get();

        double total = 0.0;
        List<CartProduct> items = cartProductRepository.findByCartId(cart.getId());
        for (CartProduct item : items) {
            Optional<Product> productOpt = productRepository.findById(item.getProductId());
            if (productOpt.isPresent()) {
                total += productOpt.get().getPrice() * item.getCount();
            }
        }

        if (cart.getAppliedPromoCodeId() != null) {
            Optional<PromoCode> promoOpt = promoCodeRepository.findById(cart.getAppliedPromoCodeId());
            if (promoOpt.isPresent()) {
                PromoCode promo = promoOpt.get();
                if (promo.getType().name().equals("PERCENT")) {
                    total = total * (1.0 - promo.getValue() / 100.0);
                } else if (promo.getType().name().equals("FIXED")) {
                    total = total - promo.getValue();
                }
            }
        }

        return Math.max(total, 0.0);
    }
}
