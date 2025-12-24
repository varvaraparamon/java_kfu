package com.example.services;

import com.example.models.Cart;
import com.example.models.PromoCode;
import com.example.models.PromoUsageType;
import com.example.repositories.CartRepository;
import com.example.repositories.PromoCodeRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public class CartPromoService {

    private final CartRepository cartRepository;
    private final PromoCodeRepository promoCodeRepository;

    public CartPromoService(CartRepository cartRepository, PromoCodeRepository promoCodeRepository) {
        this.cartRepository = cartRepository;
        this.promoCodeRepository = promoCodeRepository;
    }

    public boolean applyPromoCode(Long cartId, String code) {
        Optional<Cart> cartOpt = cartRepository.findById(cartId);
        Optional<PromoCode> promoOpt = promoCodeRepository.findByCode(code);

        if (cartOpt.isEmpty() || promoOpt.isEmpty()) return false;

        PromoCode promo = promoOpt.get();

        if (!promo.getActive() || promo.getExpiresAt().isBefore(LocalDateTime.now())) {
            return false;
        }

        Cart cart = cartOpt.get();
        cart.setAppliedPromoCodeId(promo.getId()); 
        cartRepository.update(cart);

        if (promo.getUsageType().equals(PromoUsageType.SINGLE_USE)) {
            promo.setActive(false);
            promoCodeRepository.update(promo);
        }

        return true;
    }

    public void removePromoCode(Long cartId) {
        Optional<Cart> cartOpt = cartRepository.findById(cartId);
        if (cartOpt.isEmpty()) return;

        Cart cart = cartOpt.get();
        cart.setAppliedPromoCodeId(null);
        cartRepository.update(cart);
    }
}
