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
    private Long[] cartProductsId = new Long[50];

    @Builder.Default
    private Integer currentSize = 0;

    @Builder.Default
    private Double currentSum = 0.0;

    public void addProduct(Long cartProductId) {
        cartProductsId[currentSize++] = cartProductId;
    }
}
