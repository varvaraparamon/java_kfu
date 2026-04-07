package com.example;

import com.example.framework.Column;
import com.example.framework.DataModel;

public class CartItem implements DataModel {
    @Column(value = "id")
    private Long cartItemId;

    private Double totalPrice;

    public CartItem() {
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "id=" + cartItemId +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
