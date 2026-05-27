package org.example;

import java.math.BigDecimal;

public enum PizzaSize {
    PERSONAL("Personal (8\")", new BigDecimal("8.99")),
    MEDIUM("Medium (12\")", new BigDecimal("12.00")),
    LARGE("Large (16\")", new BigDecimal("16.50"));

    private final String displayName;
    private final BigDecimal basePrice;

    PizzaSize(String displayName, BigDecimal basePrice) {
        this.displayName = displayName;
        this.basePrice = basePrice;
    }

    public String getDisplayName() {
        return displayName;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    @Override
    public String toString(){
        return displayName;
    }
}
