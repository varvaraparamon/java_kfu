package com.example.models;


import lombok.*;
import javax.persistence.*;

@EqualsAndHashCode
@Table(name = "carts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id", unique = true)
    private Long userId;
    
    @Column(name = "applied_promo_code_id")
    @Builder.Default
    private Long appliedPromoCodeId = null;
}
