package com.example.services;

import com.example.models.PromoCode;
import com.example.repositories.PromoCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PromoCodeServiceImpl implements PromoCodeService {
    private final PromoCodeRepository promoCodeRepository;

    @Override
    public PromoCode createPromoCode(PromoCode promoCode) {
        return promoCodeRepository.save(promoCode);
    }

    @Override
    public PromoCode updatePromoCode(Long id, PromoCode promoCode) {
        promoCode.setId(id);
        return promoCodeRepository.save(promoCode);
    }

    @Override
    public void deletePromoCode(Long id) {
        promoCodeRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PromoCode> getPromoCodeById(Long id) {
        return promoCodeRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PromoCode> getPromoCodeByCode(String code) {
        return promoCodeRepository.findByCode(code);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PromoCode> getAllActivePromoCodes() {
        return promoCodeRepository.findByActiveTrueAndExpiresAtAfter(LocalDateTime.now());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isValidPromoCode(String code) {
        return promoCodeRepository.findByCode(code)
                .filter(promo -> promo.getActive() && 
                        (promo.getExpiresAt() == null || promo.getExpiresAt().isAfter(LocalDateTime.now())))
                .isPresent();
    }
}
