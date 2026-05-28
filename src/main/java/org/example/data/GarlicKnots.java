package org.example.data;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class GarlicKnots {
    public static final BigDecimal PRICE_PER_ORDER = new BigDecimal("1.50");

    private final int quantity;

    public GarlicKnots(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return PRICE_PER_ORDER.multiply(new BigDecimal(quantity)).
                setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public String toString() {
        return "Garlic Knots x" + quantity;
    }

    public String toReceiptString() {
        return String.format("  %-30s $%.2f%n", this, getPrice());
    }
}
