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

public class UserInterface {
    private final OrderService orderService;
    private final MenuService menuService;
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
            System.out.println("  1. New Order");
            System.out.println("  0. Exit");

            int choice = Input.readInt("\nSelect: ", 0, 1);

            if (choice == 1) {
                showOrderScreen();
            } else {
                System.out.println("\n  Thanks for using Pizza House POS. Goodbye!\n");
                break;
            }
        }
    }

    private void printWelcome() {
        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║         Welcome to PIZZA HOUSE       ║");
        System.out.println("║          Point of Sale System        ║");
        System.out.println("╚══════════════════════════════════════╝");
    }

    private void showOrderScreen() {
        orderService.startNewOrder();

        while (true) {
            Input.printHeader("Order Screen");
            printCurrentOrderSummary();
            Input.printDivider();
            System.out.println("  1. Add Pizza");
            System.out.println("  2. Add Drink");
            System.out.println("  3. Add Garlic Knots");
            System.out.println("  4. Checkout");
            System.out.println("  0. Cancel Order");

            int choice = Input.readInt("\nSelect: ", 0, 4);

            switch (choice) {
                case 1 -> handleAddPizza();
                case 2 -> handleAddDrink();
                case 3 -> handleAddGarlicKnots();
                case 4 -> { if (handleCheckout()) return; }
                case 0 -> { if (handleCancelOrder()) return; }
            }
        }
    }

    private void printCurrentOrderSummary() {
        Order order = orderService.getCurrentOrder();

        if (order.isEmpty()) {
            System.out.println("\n  (No items yet)\n");
            return;
        }

        System.out.println("\n  Current Order (newest first):");
        Input.printDivider();

        List<Pizza> pizzas = order.getPizzas();
        if (!pizzas.isEmpty()) {
            System.out.println("  PIZZAS:");
            for (Pizza p : pizzas) {
                System.out.printf("    - %s  $%.2f%n", p, p.calculatePrice());
            }
        }

        List<Drink> drinks = order.getDrinks();
        if (!drinks.isEmpty()) {
            System.out.println("  DRINKS:");
            for (Drink d : drinks) {
                System.out.printf("    - %s  $%.2f%n", d, d.getPrice());
            }
        }

        List<GarlicKnots> knots = order.getGarlicKnotsList();
        if (!knots.isEmpty()) {
            System.out.println("  GARLIC KNOTS:");
            for (GarlicKnots g : knots) {
                System.out.printf("    - %s  $%.2f%n", g, g.getPrice());
            }
        }

        System.out.printf("%n  Running Total: $%.2f%n", order.getTotal());
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
            System.out.println("\n  Cannot checkout: " + error);
            Input.pressEnterToContinue();
            return false;
        }
        return showCheckout(orderService.getCurrentOrder());
    }

    private boolean handleCancelOrder() {
        if (Input.readYesNo("\nAre you sure you want to cancel this order?")) {
            orderService.cancelOrder();
            System.out.println("  Order cancelled.");
            Input.pressEnterToContinue();
            return true;
        }
        return false;
    }

    private Pizza showAddPizza() {
        Input.printHeader("Add Pizza");
        System.out.println("  1. Build a custom pizza");
        System.out.println("  2. Choose a signature pizza");
        System.out.println("  0. Back");

        int choice = Input.readInt("Select: ", 0, 2);
        return switch (choice) {
            case 1  -> buildCustomPizza();
            case 2  -> buildSignaturePizza();
            default -> null;
        };
    }

    private Pizza buildCustomPizza() {
        Input.printHeader("Select Crust Type");
        CrustType crust = Input.pickFromList("", Arrays.asList(CrustType.values()));

        Input.printHeader("Select Pizza Size");
        PizzaSize size = Input.pickFromList("", Arrays.asList(PizzaSize.values()));

        Input.printHeader("Select Sauce");
        String sauce = Input.pickFromList("", menuService.getSauces());

        Pizza pizza = new Pizza(size, crust, sauce);

        // 4. Toppings
        showToppingsFlow(pizza);

        // 5. Stuffed crust
        if (Input.readYesNo("\nWould you like stuffed crust? (+$2.00)")) {
            pizza.setStuffedCrust(true);
        }

        // 6. Free sides
        showSidesFlow(pizza);

        System.out.printf("%nPizza added!  Subtotal: $%.2f%n", pizza.calculatePrice());
        return pizza;
    }

    private Pizza buildSignaturePizza() {
        Input.printHeader("Signature Pizzas");

        List<SignaturePizza> sigs = menuService.getSignaturePizzas();
        for (int i = 0; i < sigs.size(); i++) {
            SignaturePizza s = sigs.get(i);
            System.out.printf("  %d. %-20s%n     %s%n     Base price: $%.2f%n%n",
                    i + 1, s.getSignaturePizzaName(), s.getDescription(), s.calculatePrice());
        }
        System.out.println("  0. Back");

        int pick = Input.readInt("Choose: ", 0, sigs.size());
        if (pick == 0) return null;

        SignaturePizza template = sigs.get(pick - 1);

        Input.printHeader("Select Size for \"" + template.getSignaturePizzaName() + "\"");
        PizzaSize size = Input.pickFromList("", Arrays.asList(PizzaSize.values()));

        SignaturePizza pizza = new SignaturePizza(template, size);

        System.out.println("\nDefault toppings:");
        pizza.getToppings().forEach((t, qty) ->
                System.out.printf("  - %s x%d%n", t.getName(), qty));

        if (Input.readYesNo("\nWould you like to customize the toppings?")) {
            showToppingsFlow(pizza);
        }

        if (Input.readYesNo("\nWould you like stuffed crust? (+$2.00)")) {
            pizza.setStuffedCrust(true);
        }

        showSidesFlow(pizza);

        System.out.printf("%nSignature pizza added!  Subtotal: $%.2f%n", pizza.calculatePrice());
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
            for (int i = 0; i < options.size(); i++) {
                Topping t = options.get(i);
                int qty = pizza.getToppings().getOrDefault(t, 0);

                String qtyNote   = qty > 0 ? " [x" + qty + " added]" : "";
                String priceNote = t.isPremium()
                        ? String.format(" (+$%.2f / extra $%.2f)",
                        t.getFirstUnitPrice(pizza.getSize()),
                        t.getExtraUnitPrice(pizza.getSize()))
                        : " (free)";

                System.out.printf("  %d. %s%s%s%n", i + 1, t.getName(), priceNote, qtyNote);
            }
            System.out.println("  0. Done");

            int choice = Input.readInt("\nAdd topping (0 to finish): ", 0, options.size());
            if (choice == 0) break;

            Topping selected = options.get(choice - 1);
            pizza.addTopping(selected);
            System.out.println("Added: " + selected.getName() + ". Select again for extra.");
            Input.printDivider();
        }
    }

    private void showSidesFlow(Pizza pizza) {
        Input.printHeader("Sides  (Included Free)");
        List<String> sides = menuService.getSides();

        while (true) {
            for (int i = 0; i < sides.size(); i++) {
                System.out.printf("  %d. %s%n", i + 1, sides.get(i));
            }
            System.out.println("  0. No more sides");

            int choice = Input.readInt("\nAdd side: ", 0, sides.size());
            if (choice == 0) break;
            pizza.addSide(sides.get(choice - 1));
            System.out.println("Added: " + sides.get(choice - 1));
            Input.printDivider();
        }
    }

    private Drink showAddDrink() {
        Input.printHeader("\nAdd Drink");
        System.out.println("Select drink size:");
        DrinkSize[] sizes = DrinkSize.values();
        for (int i = 0; i < sizes.length; i++) {
            System.out.printf("  %d. %-10s $%.2f%n", i + 1, sizes[i].getDisplayName(), sizes[i].getPrice());
        }
        System.out.println("  0. Back");

        int sizeChoice = Input.readInt("Select: ", 0, sizes.length);
        if (sizeChoice == 0) return null;

        DrinkSize size   = sizes[sizeChoice - 1];
        String    flavor = Input.pickFromList("\nSelect flavor:", menuService.getDrinkFlavors());

        Drink drink = new Drink(size, flavor);
        System.out.printf("%nDrink added: %s  $%.2f%n", drink, drink.getPrice());
        Input.printDivider();
        return drink;
    }

    private GarlicKnots showAddGarlicKnots() {
        Input.printHeader("\nAdd Garlic Knots");
        System.out.printf("  Garlic Knots — $%.2f per order%n", GarlicKnots.PRICE_PER_ORDER);
        System.out.println("  0. Back");

        int qty = Input.readInt("How many orders? (0 to go back): ", 0, 10);
        if (qty == 0) return null;

        GarlicKnots knots = new GarlicKnots(qty);
        System.out.printf("%nAdded: %s  $%.2f%n", knots, knots.getPrice());
        Input.printDivider();
        return knots;
    }

    private boolean showCheckout(Order order) {
        Input.printHeader("Checkout — Order Summary");
        System.out.println(order.toReceiptString());

        System.out.println("  1. Confirm Order");
        System.out.println("  0. Cancel Order");

        int choice = Input.readInt("Select: ", 0, 1);

        if (choice == 1) {
            String path = receiptService.saveReceipt(order);
            if (path != null) {
                System.out.println("\n  Order confirmed! Receipt saved to: " + path);
            } else {
                System.out.println("\n  Order confirmed! (Receipt could not be saved.)");
            }
            System.out.println("  Returning to main menu...");
        } else {
            orderService.cancelOrder();
            System.out.println("\n  Order cancelled. Returning to main menu...");
        }

        Input.pressEnterToContinue();
        return true;
    }

}
