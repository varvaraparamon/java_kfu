package com.example.repositories;

import com.example.models.PromoCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PromoCodeRepository extends JpaRepository<PromoCode, Long> {

    void save(PromoCode promoCode);
    void update(PromoCode promoCode);
    void deleteById(Long id);

    Optional<PromoCode> findById(Long id);
    Optional<PromoCode> findByCode(String code);
    List<PromoCode> findAll();
}