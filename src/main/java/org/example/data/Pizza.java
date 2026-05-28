package org.example.data;

import org.example.enums.CrustType;
import org.example.enums.PizzaSize;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Pizza {
    private static final BigDecimal STUFFED_CRUST_PRICE = new BigDecimal("2.00");

    private final PizzaSize size;
    private final CrustType crust;
    private final String sauce;
    private boolean stuffedCrust;
    private boolean isSignature;
    private String signatureName;

    private final Map<Topping, Integer> toppings;
    private final List<String> sides;

    public Pizza(PizzaSize size, CrustType crust, String sauce) {
        this.size = size;
        this.crust = crust;
        this.sauce = sauce;
        this.stuffedCrust = false;
        this.isSignature = false;
        this.toppings = new LinkedHashMap<>(); //toppings can be display in the order they were added instead of using hashmap
        this.sides = new ArrayList<>();
    }

    public void addTopping(Topping topping) {
        if (toppings.containsKey(topping)) {
            toppings.put(topping, toppings.get(topping) + 1);
        } else {
            toppings.put(topping, 1);
        }
    }
    // if removing last topping, deletes entry. if more than just remove 1 at a time
    public void removeTopping(Topping topping) {
        if (!toppings.containsKey(topping)) return;
        int qty = toppings.get(topping);
        if (qty <= 1) toppings.remove(topping);
        else toppings.put(topping, qty - 1);
    }

    //create a defensive copy in order to stop unauthorized modification to toppings quantity
    public Map<Topping, Integer> getToppings() {
        return new LinkedHashMap<>(toppings);
    }

    public void addSide(String side) {
        sides.add(side);
    }

    public List<String> getSides() {
        return new ArrayList<>(sides);
    }

    public boolean isStuffedCrust() {
        return stuffedCrust;
    }

    public void setStuffedCrust(boolean stuffedCrust) {
        this.stuffedCrust = stuffedCrust;
    }

    public void markAsSignature(String name) {
        this.isSignature = true;
        this.signatureName = name;
    }

    public boolean isSignature() {
        return isSignature;
    }

    public String getSignatureName() {
        return signatureName;
    }

    public PizzaSize getSize() {
        return size;
    }

    public CrustType getCrust() {
        return crust;
    }

    public String getSauce() {
        return sauce;
    }

    public BigDecimal calculatePrice() {
        BigDecimal total = size.getBasePrice();

        total = total.add(crust.getExtraCost());

        if (stuffedCrust) {
            total = total.add(STUFFED_CRUST_PRICE);
        }

        for (Map.Entry<Topping, Integer> entry : toppings.entrySet()) {
            Topping topping = entry.getKey();

            int qty = entry.getValue();

            if (qty >= 1) {
                total = total.add(topping.getFirstUnitPrice(size));
            }
            if (qty > 1) {
                BigDecimal extraCost = topping.getExtraUnitPrice(size)
                        .multiply(new BigDecimal(qty - 1));
                total = total.add(extraCost);
            }
        }

        return total.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public String toString() {
        return (isSignature ? "[" + signatureName + "] " : "")
                + size.getDisplayName() + " | " + crust.getDisplayName()
                + (stuffedCrust ? " + Stuffed Crust" : "")
                + " | Sauce: " + sauce;
    }
    //using StringBuilder to build a string
    public String toReceiptString() {
        StringBuilder sb = new StringBuilder();
        sb.append("  ").append(this).append("\n");

        if (!toppings.isEmpty()) {
            sb.append("    Toppings:\n");
            for (Map.Entry<Topping, Integer> entry : toppings.entrySet()) {
                Topping t = entry.getKey();
                int qty = entry.getValue();
                //ternary operator if quantity greater than 1
                String qtyLabel = qty > 1 ? " x" + qty : "";

                String priceLabel = "";
                if (t.isPremium()) {
                    BigDecimal cost = t.getFirstUnitPrice(size);
                    if (qty > 1) {
                        cost = cost.add(
                                t.getExtraUnitPrice(size).multiply(new BigDecimal(qty - 1))
                        );
                    }
                    priceLabel = " (+" + String.format("$%.2f", cost) + ")";
                }

                sb.append("      - ")
                        .append(t.getName())
                        .append(qtyLabel)
                        .append(priceLabel)
                        .append("\n");
            }
        }

        if (!sides.isEmpty()) {
            sb.append("    Sides: ").append(String.join(", ", sides)).append("\n");
        }

        sb.append(String.format("    Pizza Subtotal: $%.2f\n", calculatePrice()));
        return sb.toString();
    }
}

