package com.example.repositories;

import com.example.models.PromoCode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PromoCodeRepository extends CrudRepository<PromoCode, Long> {
    Optional<PromoCode> findByCode(String code);
    List<PromoCode> findByActiveTrueAndExpiresAtAfter(LocalDateTime dateTime);
}
