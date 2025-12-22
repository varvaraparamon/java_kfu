package com.example.repositories;

import com.example.models.PromoCode;

import java.util.List;
import java.util.Optional;

public interface PromoCodeRepository {

    void save(PromoCode promoCode);
    void update(PromoCode promoCode);
    void deleteById(Long id);

    Optional<PromoCode> findById(Long id);
    Optional<PromoCode> findByCode(String code);
    List<PromoCode> findAll();
}