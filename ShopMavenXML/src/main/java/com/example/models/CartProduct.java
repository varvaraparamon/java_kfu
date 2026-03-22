package com.example.models;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartProduct {
    private Long id;
    private Long cartId;
    private Long productId;
    private Integer count;
}