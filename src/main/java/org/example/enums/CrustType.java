package org.example.enums;

import java.math.BigDecimal;

public enum CrustType {
    THIN        ("Thin",        new BigDecimal("1.00")),
    REGULAR     ("Regular",     new BigDecimal("1.50")),
    THICK       ("Thick",       new BigDecimal("2.00")),
    CAULIFLOWER ("Cauliflower", new BigDecimal("2.50"));

    private final String displayName;
    private final BigDecimal extraCost;

    CrustType(String displayName, BigDecimal extraCost) {
        this.displayName = displayName;
        this.extraCost = extraCost;
    }

    public String getDisplayName() {
        return displayName;
    }

    public BigDecimal getExtraCost() {
        return extraCost;
    }

    @Override
    public String toString() {
        return String.format("%s (+%.2f)", displayName, extraCost);
    }
}
