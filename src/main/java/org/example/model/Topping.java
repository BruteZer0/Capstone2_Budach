package org.example.model;

import org.example.enums.ToppingCategory;

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

    @Override
    public String toString() {
        return name;
    }
}
