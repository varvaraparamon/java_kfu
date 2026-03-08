package com.example.models;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "promo_codes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromoCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String code;

    @Enumerated(EnumType.STRING)
    private PromoType type;
    
    private Double value;

    @Enumerated(EnumType.STRING)
    private PromoUsageType usageType;
    
    private Boolean active;

    private LocalDateTime expiresAt;
}