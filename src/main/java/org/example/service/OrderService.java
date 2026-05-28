package org.example.service;

import org.example.data.Drink;
import org.example.data.GarlicKnots;
import org.example.data.Order;
import org.example.data.Pizza;

public class OrderService {
    private Order currentOrder;

    public void startNewOrder() {
        currentOrder = new Order();
    }

    public void cancelOrder() {
        currentOrder = null;
    }

    public boolean hasActiveOrder() {
        return currentOrder != null;
    }

    public Order getCurrentOrder() {
        return currentOrder;
    }

    public void addPizzaToOrder(Pizza pizza) {
        validateActiveOrder();
        currentOrder.addPizza(pizza);
    }

    public void addDrinkToOrder(Drink drink) {
        validateActiveOrder();
        currentOrder.addDrink(drink);
    }

    public void addGarlicKnotsToOrder(GarlicKnots garlicKnots) {
        validateActiveOrder();
        currentOrder.addGarlicKnots(garlicKnots);
    }

    public String validateForCheckout() {
        validateActiveOrder();
        if (currentOrder.isEmpty()) {
            return "Your order is empty. Please add at least one item.";
        }
        if (!currentOrder.isValid()) {
            return "Orders with no pizza must include at least one drink or garlic knots.";
        }
        return null;
    }

    private void validateActiveOrder() {
        if (currentOrder == null) {
            throw new IllegalStateException("No active order. Start a new order first.");
        }
    }
}
