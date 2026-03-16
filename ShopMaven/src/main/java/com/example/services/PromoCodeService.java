package com.example.services;

import com.example.models.PromoCode;
import java.util.List;
import java.util.Optional;

public interface PromoCodeService {
    PromoCode createPromoCode(PromoCode promoCode);
    PromoCode updatePromoCode(Long id, PromoCode promoCode);
    void deletePromoCode(Long id);
    Optional<PromoCode> getPromoCodeById(Long id);
    Optional<PromoCode> getPromoCodeByCode(String code);
    List<PromoCode> getAllActivePromoCodes();
    boolean isValidPromoCode(String code);
}
