package org.example.enums;

import java.math.BigDecimal;

public enum DrinkSize {
    SMALL("Small", new BigDecimal("2.00")),
    MEDIUM("Medium", new BigDecimal("2.50")),
    LARGE("Large", new BigDecimal("3.00"));

    private final String displayName;
    private final BigDecimal price;

    DrinkSize(String displayName, BigDecimal price) {
        this.displayName = displayName;
        this.price = price;
    }

    public String getDisplayName() {
        return displayName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
