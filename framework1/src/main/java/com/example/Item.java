package com.example;

import com.example.framework.Column;
import com.example.framework.DataModel;
import com.example.framework.DisabledColumn;
import com.example.framework.Table;

@Table(value = "items")
public class Item implements DataModel {
    private Integer itemId;

    @Column(value = "finish_price")
    @DisabledColumn
    private Double totalPrice;

    private String itemName;

    
    private String alternativeName;

    public Item() {
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + itemId +
                ", itemName= " + itemName +
                ", totalPrice=" + totalPrice +
                ", alternativeName=" + alternativeName +
                '}';
    }
}
