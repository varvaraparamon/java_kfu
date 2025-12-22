package com.example.models;


import lombok.*;

@EqualsAndHashCode
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cart {

    private Long id;
    private Long userId;
    
    @Builder.Default
    private Long appliedPromoCodeId = null;

}
