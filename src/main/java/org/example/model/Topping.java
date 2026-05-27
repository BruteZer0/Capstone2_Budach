package org.example.model;

import org.example.enums.ToppingCategory;

import java.math.BigDecimal;

public class Topping {
    private String name;
    private ToppingCategory category;
    private BigDecimal pricePerUnit;

    public Topping(String name, ToppingCategory category, BigDecimal pricePerUnit) {
        this.name = name;
        this.category = category;
        this.pricePerUnit = pricePerUnit;
    }

    public String getName() {
        return name;
    }

    public ToppingCategory getCategory() {
        return category;
    }

    public BigDecimal getPricePerUnit() {
        return pricePerUnit;
    }

    @Override
    public String toString() {
        return name;
    }
}
