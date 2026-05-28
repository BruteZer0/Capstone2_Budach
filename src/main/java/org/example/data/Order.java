package org.example.data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Order {
    private static final String     SHOP_NAME = "Pizza House";
    private static final BigDecimal TAX_RATE  = new BigDecimal("0.08");

    private String customerName;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String name) {
        this.customerName = name;
    }

    private final LocalDateTime orderTime;
    private final List<Pizza> pizzas;
    private final List<Drink>       drinks;
    private final List<GarlicKnots> garlicKnotsList;

    public Order() {
        this.orderTime       = LocalDateTime.now();
        this.pizzas          = new ArrayList<>();
        this.drinks          = new ArrayList<>();
        this.garlicKnotsList = new ArrayList<>();
    }

    public void addPizza(Pizza pizza) {
        pizzas.add(0, pizza);
    }
    public void addDrink(Drink drink) {
        drinks.add(0, drink);
    }
    public void addGarlicKnots(GarlicKnots knots) {
        garlicKnotsList.add(0, knots);
    }

    public boolean isValid() {
        if (pizzas.isEmpty()) {
            return !drinks.isEmpty() || !garlicKnotsList.isEmpty();
        }
        return true;
    }

    public boolean isEmpty() {
        return pizzas.isEmpty() && drinks.isEmpty() && garlicKnotsList.isEmpty();
    }

    public BigDecimal getSubtotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (Pizza p       : pizzas)          total = total.add(p.calculatePrice());
        for (Drink d       : drinks)          total = total.add(d.getPrice());
        for (GarlicKnots g : garlicKnotsList) total = total.add(g.getPrice());
        return total.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getTax() {
        return getSubtotal().multiply(TAX_RATE).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getTotal() {
        return getSubtotal().add(getTax()).setScale(2, RoundingMode.HALF_UP);
    }

    public List<Pizza> getPizzas() {
        return Collections.unmodifiableList(pizzas);
    }

    public List<Drink> getDrinks() {
        return Collections.unmodifiableList(drinks);
    }

    public List<GarlicKnots> getGarlicKnotsList() {
        return Collections.unmodifiableList(garlicKnotsList);
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public String getReceiptFileName() {
        return orderTime.format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss")) + ".txt";
    }

    public String toReceiptString() {
        StringBuilder sb = new StringBuilder();
        String divider = "========================================\n";
        String thin    = "----------------------------------------\n";

        sb.append(divider);
        sb.append("           ").append(SHOP_NAME).append("\n");
        sb.append(divider);
        sb.append("Date: ")
                .append(orderTime.format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a")))
                .append("\n");
        sb.append("Customer: ").append(customerName != null ? customerName : "Guest").append("\n");

        sb.append(thin);

        if (!pizzas.isEmpty()) {
            sb.append("PIZZAS:\n");
            for (int i = 0; i < pizzas.size(); i++) {
                sb.append("  Pizza #").append(i + 1).append("\n");
                sb.append(pizzas.get(i).toReceiptString());
            }
        }

        if (!drinks.isEmpty()) {
            sb.append("DRINKS:\n");
            for (Drink d : drinks) sb.append(d.toReceiptString());
        }

        if (!garlicKnotsList.isEmpty()) {
            sb.append("GARLIC KNOTS:\n");
            for (GarlicKnots g : garlicKnotsList) sb.append(g.toReceiptString());
        }

        sb.append(thin);
        sb.append(String.format("  Subtotal:  $%6.2f%n", getSubtotal()));
        if (getTax().compareTo(BigDecimal.ZERO) > 0) {
            sb.append(String.format("  Tax:       $%6.2f%n", getTax()));
        }
        sb.append(String.format("  TOTAL:     $%6.2f%n", getTotal()));
        sb.append(divider);
        sb.append("   Thank you for choosing ").append(SHOP_NAME).append("!\n");
        sb.append(divider);

        return sb.toString();
    }
}




