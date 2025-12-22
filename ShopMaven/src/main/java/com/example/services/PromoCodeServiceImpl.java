package com.example.services;

import com.example.models.PromoCode;
import com.example.repositories.PromoCodeRepository;

import java.util.List;
import java.util.Optional;

public class PromoCodeServiceImpl implements PromoCodeService {

    private final PromoCodeRepository promoCodeRepository;

    public PromoCodeServiceImpl(PromoCodeRepository promoCodeRepository) {
        this.promoCodeRepository = promoCodeRepository;
    }

    @Override
    public void create(PromoCode promoCode) {
        promoCodeRepository.save(promoCode);
    }

    @Override
    public void update(PromoCode promoCode) {
        promoCodeRepository.update(promoCode);
    }

    @Override
    public void delete(Long id) {
        promoCodeRepository.deleteById(id);
    }

    @Override
    public Optional<PromoCode> getById(Long id) {
        return promoCodeRepository.findById(id);
    }

    @Override
    public Optional<PromoCode> getByCode(String code) {
        return promoCodeRepository.findByCode(code);
    }

    @Override
    public List<PromoCode> getAll() {
        return promoCodeRepository.findAll();
    }
}
