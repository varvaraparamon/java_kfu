package com.example.services;

import com.example.models.PromoCode;

import java.util.List;
import java.util.Optional;

public interface PromoCodeService {

    void create(PromoCode promoCode);
    void update(PromoCode promoCode);
    void delete(Long id);

    Optional<PromoCode> getById(Long id);
    Optional<PromoCode> getByCode(String code);
    List<PromoCode> getAll();
}