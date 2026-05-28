package org.example.data;

import org.example.enums.PizzaSize;
import org.example.enums.ToppingCategory;

import java.math.BigDecimal;

public class Topping {
    private final String name;
    private final ToppingCategory category;

    public Topping(String name, ToppingCategory category) {
        this.name = name;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public ToppingCategory getCategory() {
        return category;
    }

    public boolean isPremium() {
        return category.isPremium();
    }

    public BigDecimal getFirstUnitPrice(PizzaSize size) {
        return category.getFirstUnitPrice(size);
    }

    public BigDecimal getExtraUnitPrice(PizzaSize size) {
        return  category.getExtraUnitPrice(size);
    }

    @Override
    public String toString() {
        return name;
    }
}
