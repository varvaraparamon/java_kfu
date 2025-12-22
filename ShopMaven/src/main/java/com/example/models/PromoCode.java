package com.example.models;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromoCode {

    private Long id;
    private String code;

    private PromoType type;              
    private Double value;                

    private PromoUsageType usageType;    
    private Boolean active;

    private LocalDateTime expiresAt;
}