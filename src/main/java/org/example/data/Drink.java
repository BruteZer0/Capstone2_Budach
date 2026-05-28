package org.example.data;

import org.example.enums.DrinkSize;

import java.math.BigDecimal;

public class Drink {
    private final DrinkSize size;
    private final String flavor;

    public Drink(DrinkSize size, String flavor) {
        this.size = size;
        this.flavor = flavor;
    }

    public DrinkSize getSize() {
        return size;
    }

    public String getFlavor() {
        return flavor;
    }

    public BigDecimal getPrice() {
        return size.getPrice();
    }

    @Override
    public String toString() {
        return size.getDisplayName() + " " + flavor;
    }

    public String toReceiptString() {
        return String.format
                ("  %-30s $%.2f%n", this, getPrice());
    }
}
