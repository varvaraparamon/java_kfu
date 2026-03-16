package com.example.services;

import com.example.models.Cart;
import com.example.models.PromoCode;
import com.example.models.PromoUsageType;
import com.example.repositories.CartRepository;
import com.example.repositories.PromoCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class CartPromoService {
    private final CartRepository cartRepository;
    private final PromoCodeRepository promoCodeRepository;

    public boolean applyPromoCode(Long cartId, String code) {
        Cart cart = cartRepository.findById(cartId).orElse(null);
        PromoCode promo = promoCodeRepository.findByCode(code).orElse(null);

        if (cart == null || promo == null) return false;

        if (!isPromoCodeValid(promo)) {
            return false;
        }

        cart.setAppliedPromoCodeId(promo.getId());
        cartRepository.save(cart);

        if (promo.getUsageType() == PromoUsageType.SINGLE_USE) {
            promo.setActive(false);
            promoCodeRepository.save(promo);
        }

        return true;
    }

    public void removePromoCode(Long cartId) {
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null) return;

        cart.setAppliedPromoCodeId(null);
        cartRepository.save(cart);
    }

    public boolean isPromoCodeValidForCart(Long cartId, String code) {
        PromoCode promo = promoCodeRepository.findByCode(code).orElse(null);
        if (promo == null) return false;

        return isPromoCodeValid(promo);
    }

    private boolean isPromoCodeValid(PromoCode promo) {
        return promo.getActive() && (promo.getExpiresAt() == null || promo.getExpiresAt().isAfter(LocalDateTime.now()));
    }
}