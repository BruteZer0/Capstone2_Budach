package org.example.ui;

import org.example.data.*;
import org.example.enums.CrustType;
import org.example.enums.DrinkSize;
import org.example.enums.PizzaSize;
import org.example.service.MenuService;
import org.example.service.OrderService;
import org.example.service.ReceiptService;
import org.example.util.Input;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class UserInterface {

    private final OrderService   orderService;
    private final MenuService    menuService;
    private final ReceiptService receiptService;

    public UserInterface() {
        this.menuService    = new MenuService();
        this.orderService   = new OrderService();
        this.receiptService = new ReceiptService();
    }

    public void run() {
        printWelcome();

        while (true) {
            Input.printHeader("Pizza House — Home");
            Input.println("  1. New Order");
            Input.println("  0. Exit");

            int choice = Input.readInt("\nSelect: ", 0, 1);

            if (choice == 1) {
                showOrderScreen();
            } else {
                Input.printSuccess("\n  Thanks for using Pizza House POS. Goodbye!\n");
                Input.dispose();
                break;
            }
        }
    }

    private void printWelcome() {
        Input.printWithColor("╔══════════════════════════════════════╗", "yellow");
        Input.printWithColor("║         Welcome to PIZZA HOUSE       ║", "yellow");
        Input.printWithColor("║          Point of Sale System        ║", "yellow");
        Input.printWithColor("╚══════════════════════════════════════╝", "yellow");
    }

    private void showOrderScreen() {
        Input.printHeader("New Order");
        Input.println("  Please enter the customer name below.");
        String customerName = Input.readLine("Customer Name: ");
        orderService.startNewOrder(customerName);
        Input.printSuccess("  Welcome, " + customerName + "!");

        while (true) {
            Input.printHeader("Order Screen");
            printCurrentOrderSummary();
            Input.printDivider();
            Input.println("  1. Add Pizza");
            Input.println("  2. Add Drink");
            Input.println("  3. Add Garlic Knots");
            Input.println("  4. Checkout");
            Input.println("  0. Cancel Order");

            int choice = Input.readInt("\nSelect: ", 0, 4);

            switch (choice) {
                case 1:
                    handleAddPizza();
                    break;
                case 2:
                    handleAddDrink();
                    break;
                case 3:
                    handleAddGarlicKnots();
                    break;
                case 4:
                    if (handleCheckout()) return;
                    break;
                case 0:
                    if (handleCancelOrder()) return;
                    break;
            }
        }
    }

    private void printCurrentOrderSummary() {
        Order order = orderService.getCurrentOrder();

        if (order.isEmpty()) {
            Input.println("\n  (No items yet)\n");
            return;
        }

        Input.printHighlight("\n  Current Order (newest first):");
        Input.printDivider();

        List<Pizza> pizzas = order.getPizzas();
        if (!pizzas.isEmpty()) {
            Input.println("  PIZZAS:");
            for (Pizza p : pizzas) {
                Input.printf("    - %s  $%.2f%n", p, p.calculatePrice());
            }
        }

        List<Drink> drinks = order.getDrinks();
        if (!drinks.isEmpty()) {
            Input.println("  DRINKS:");
            for (Drink d : drinks) {
                Input.printf("    - %s  $%.2f%n", d, d.getPrice());
            }
        }

        List<GarlicKnots> knots = order.getGarlicKnotsList();
        if (!knots.isEmpty()) {
            Input.println("  GARLIC KNOTS:");
            for (GarlicKnots g : knots) {
                Input.printf("    - %s  $%.2f%n", g, g.getPrice());
            }
        }

        Input.printHighlight(String.format("%n  Running Total: $%.2f", order.getTotal()));
    }

    private void handleAddPizza() {
        Pizza pizza = showAddPizza();
        if (pizza != null) {
            orderService.addPizzaToOrder(pizza);
        }
    }

    private void handleAddDrink() {
        Drink drink = showAddDrink();
        if (drink != null) {
            orderService.addDrinkToOrder(drink);
        }
    }

    private void handleAddGarlicKnots() {
        GarlicKnots knots = showAddGarlicKnots();
        if (knots != null) {
            orderService.addGarlicKnotsToOrder(knots);
        }
    }

    private boolean handleCheckout() {
        String error = orderService.validateForCheckout();
        if (error != null) {
            Input.printError("\n  Cannot checkout: " + error);
            Input.pressEnterToContinue();
            return false;
        }
        return showCheckout(orderService.getCurrentOrder());
    }

    private boolean handleCancelOrder() {
        if (Input.readYesNo("\nAre you sure you want to cancel this order?")) {
            orderService.cancelOrder();
            Input.printError("  Order cancelled.");
            Input.pressEnterToContinue();
            return true;
        }
        return false;
    }

    private Pizza showAddPizza() {
        Input.printHeader("Add Pizza");
        Input.println("  1. Build a custom pizza");
        Input.println("  2. Choose a signature pizza");
        Input.println("  0. Back");

        int choice = Input.readInt("Select: ", 0, 2);

        Pizza result = null;
        switch (choice) {
            case 1:
                result = buildCustomPizza();
                break;
            case 2:
                result = buildSignaturePizza();
                break;
        }
        return result;
    }

    private Pizza buildCustomPizza() {
        Input.printHeader("Select Crust Type");
        CrustType crust = Input.pickFromList("", Arrays.asList(CrustType.values()));

        Input.printHeader("Select Pizza Size");
        PizzaSize size = Input.pickFromList("", Arrays.asList(PizzaSize.values()));

        Input.printHeader("Select Sauce");
        String sauce = Input.pickFromList("", menuService.getSauces());

        Pizza pizza = new Pizza(size, crust, sauce);

        showToppingsFlow(pizza);

        if (Input.readYesNo("\nWould you like stuffed crust? (+$2.00)")) {
            pizza.setStuffedCrust(true);
        }

        showSidesFlow(pizza);

        Input.printSuccess(String.format("%nPizza added!  Subtotal: $%.2f", pizza.calculatePrice()));
        return pizza;
    }

    private Pizza buildSignaturePizza() {
        Input.printHeader("Signature Pizzas");

        List<SignaturePizza> sigs = menuService.getSignaturePizzas();
        for (int i = 0; i < sigs.size(); i++) {
            SignaturePizza s = sigs.get(i);
            Input.printf("  %d. %-20s%n     %s%n     Base price: $%.2f%n%n",
                    i + 1, s.getSignaturePizzaName(), s.getDescription(), s.calculatePrice());
        }
        Input.println("  0. Back");

        int pick = Input.readInt("Choose: ", 0, sigs.size());
        if (pick == 0) return null;

        SignaturePizza template = sigs.get(pick - 1);

        Input.printHeader("Select Size for \"" + template.getSignaturePizzaName() + "\"");
        PizzaSize size = Input.pickFromList("", Arrays.asList(PizzaSize.values()));

        SignaturePizza pizza = new SignaturePizza(template, size);

        Input.println("\nDefault toppings:");
        for (Map.Entry<Topping, Integer> entry : pizza.getToppings().entrySet()) {
            Input.printf("  - %s x%d%n", entry.getKey().getName(), entry.getValue());
        }

        if (Input.readYesNo("\nWould you like to customize the toppings?")) {
            showToppingsFlow(pizza);
        }

        if (Input.readYesNo("\nWould you like stuffed crust? (+$2.00)")) {
            pizza.setStuffedCrust(true);
        }

        showSidesFlow(pizza);

        Input.printSuccess(String.format("%nSignature pizza added!  Subtotal: $%.2f", pizza.calculatePrice()));
        return pizza;
    }

    private void showToppingsFlow(Pizza pizza) {
        Input.printHeader("Meat Toppings  (Premium)");
        showToppingGroup(pizza, menuService.getMeatToppings());

        Input.printHeader("Cheese Toppings  (Premium)");
        showToppingGroup(pizza, menuService.getCheeseToppings());

        Input.printHeader("Other Toppings  (Included)");
        showToppingGroup(pizza, menuService.getStandardToppings());
    }

    private void showToppingGroup(Pizza pizza, List<Topping> options) {
        while (true) {
            Input.resetColor();
            for (int i = 0; i < options.size(); i++) {
                Topping t = options.get(i);
                int qty = pizza.getToppings().getOrDefault(t, 0);

                String qtyNote   = qty > 0 ? " [x" + qty + " added]" : "";
                String priceNote = t.isPremium()
                        ? String.format(" (+$%.2f / extra $%.2f)",
                        t.getFirstUnitPrice(pizza.getSize()),
                        t.getExtraUnitPrice(pizza.getSize()))
                        : " (free)";

                Input.printf("  %d. %s%s%s%n", i + 1, t.getName(), priceNote, qtyNote);
            }
            Input.println("  0. Done");

            int choice = Input.readInt("\nAdd topping (0 to finish): ", 0, options.size());
            if (choice == 0) break;

            Topping selected = options.get(choice - 1);
            pizza.addTopping(selected);
            Input.printSuccess("Added: " + selected.getName() + ". Select again for extra.");
            Input.printDivider();
        }
    }

    private void showSidesFlow(Pizza pizza) {
        Input.printHeader("Sides  (Included Free)");
        List<String> sides = menuService.getSides();

        while (true) {
            Input.resetColor();
            for (int i = 0; i < sides.size(); i++) {
                Input.printf("  %d. %s%n", i + 1, sides.get(i));
            }
            Input.println("  0. No more sides");

            int choice = Input.readInt("Add side: ", 0, sides.size());
            if (choice == 0) break;
            pizza.addSide(sides.get(choice - 1));
            Input.printSuccess("  Added: " + sides.get(choice - 1));
        }
    }

    private Drink showAddDrink() {
        Input.printHeader("Add Drink");
        Input.println("Select drink size:");

        DrinkSize[] sizes = DrinkSize.values();
        for (int i = 0; i < sizes.length; i++) {
            Input.printf("  %d. %-10s $%.2f%n", i + 1, sizes[i].getDisplayName(), sizes[i].getPrice());
        }
        Input.println("  0. Back");

        int sizeChoice = Input.readInt("Select: ", 0, sizes.length);
        if (sizeChoice == 0) return null;

        DrinkSize size   = sizes[sizeChoice - 1];
        String    flavor = Input.pickFromList("\nSelect flavor:", menuService.getDrinkFlavors());

        Drink drink = new Drink(size, flavor);
        Input.printSuccess(String.format("%nDrink added: %s  $%.2f", drink, drink.getPrice()));
        return drink;
    }

    private GarlicKnots showAddGarlicKnots() {
        Input.printHeader("Add Garlic Knots");
        Input.printf("  Garlic Knots — $%.2f per order%n", GarlicKnots.PRICE_PER_ORDER);
        Input.println("  0. Back");

        int qty = Input.readInt("How many orders? (0 to go back): ", 0, 10);
        if (qty == 0) return null;

        GarlicKnots knots = new GarlicKnots(qty);
        Input.printSuccess(String.format("%nAdded: %s  $%.2f", knots, knots.getPrice()));
        return knots;
    }

    private boolean showCheckout(Order order) {
        Input.printHeader("Checkout — Order Summary");
        Input.println(order.toReceiptString());

        Input.println("  1. Confirm Order");
        Input.println("  0. Cancel Order");

        int choice = Input.readInt("Select: ", 0, 1);

        if (choice == 1) {
            String path = receiptService.saveReceipt(order);
            if (path != null) {
                Input.printSuccess("\n  Order confirmed! Receipt saved to: " + path);
            } else {
                Input.printSuccess("\n  Order confirmed! (Receipt could not be saved.)");
            }
            Input.printSuccess("  Returning to main menu...");
        } else {
            orderService.cancelOrder();
            Input.printError("\n  Order cancelled. Returning to main menu...");
        }

        Input.pressEnterToContinue();
        return true;
    }
}
