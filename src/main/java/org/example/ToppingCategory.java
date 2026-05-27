package org.example;

import java.math.BigDecimal;

public enum ToppingCategory {
    //PIZZA SIZE:                   PERSONAL                           MEDIUM                    LARGE
    STANDARD(
            new BigDecimal[]{ new BigDecimal("0.00"), new BigDecimal("0.00"), new BigDecimal("0.00") },
            new BigDecimal[]{ new BigDecimal("0.00"), new BigDecimal("0.00"), new BigDecimal("0.00") }
    ),
    MEAT(
            new BigDecimal[]{ new BigDecimal("1.00"), new BigDecimal("2.00"), new BigDecimal("3.00") },
            new BigDecimal[]{ new BigDecimal("0.50"), new BigDecimal("1.00"), new BigDecimal("1.50") }
    ),
    CHEESE(
            new BigDecimal[]{ new BigDecimal("0.75"), new BigDecimal("1.50"), new BigDecimal("2.25") },
            new BigDecimal[]{ new BigDecimal("0.30"), new BigDecimal("0.60"), new BigDecimal("0.90") }
    );

    private final BigDecimal[] firstUnitPrice;
    private final BigDecimal[] extraUnitPrice;

    ToppingCategory(BigDecimal[] firstUnitPrice, BigDecimal[] extraUnitPrice) {
        this.firstUnitPrice = firstUnitPrice;
        this.extraUnitPrice = extraUnitPrice;
    }

    public BigDecimal getFirstUnitPrice(PizzaSize size) {
        return firstUnitPrice[size.ordinal()];
    }

    public BigDecimal getExtraUnitPrice(PizzaSize size) {
        return extraUnitPrice[size.ordinal()];
    }

    public boolean isPremium() {
        return this == MEAT || this == CHEESE;
    }

    @Override
    public String toString() {
        return name();
    }
}
