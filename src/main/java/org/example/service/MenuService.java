package org.example.service;

import org.example.data.SignaturePizza;
import org.example.data.Topping;
import org.example.enums.CrustType;
import org.example.enums.PizzaSize;
import org.example.enums.ToppingCategory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MenuService {
    private static final List<String> SAUCES = Arrays.asList(
            "Marinara", "Alfredo", "Pesto", "BBQ", "Buffalo", "Olive Oil"
    );

    private static final List<String> SIDES = Arrays.asList(
            "Red Pepper", "Parmesan"
    );

    private static final List<String> DRINK_FLAVORS = Arrays.asList(
            "Coca-Cola", "Diet Coke", "Sprite", "Dr Pepper",
            "Lemonade", "Iced Tea", "Root Beer", "Orange Soda"
    );

    private final List<Topping> standardToppings;
    private final List<Topping> meatToppings;
    private final List<Topping> cheeseToppings;
    private final List<SignaturePizza> signaturePizzas;

    public MenuService() {
        standardToppings = buildStandardToppings();
        meatToppings = buildMeatToppings();
        cheeseToppings = buildCheeseToppings();
        signaturePizzas = buildSignaturePizzas();
    }

    private List<Topping> buildStandardToppings() {
        List<Topping> list = new ArrayList<>();
        for (String name : Arrays.asList(
                "Onions", "Mushrooms", "Bell Peppers", "Olives",
                "Tomatoes", "Spinach", "Basil", "Pineapple", "Anchovies")) {
            list.add(new Topping(name, ToppingCategory.STANDARD));
        }
        return list;
    }

    private List<Topping> buildMeatToppings() {
        List<Topping> list = new ArrayList<>();
        for (String name : Arrays.asList(
                "Pepperoni", "Sausage", "Ham", "Bacon", "Chicken", "Meatball")) {
            list.add(new Topping(name, ToppingCategory.MEAT));
        }
        return list;
    }

    private List<Topping> buildCheeseToppings() {
        List<Topping> list = new ArrayList<>();
        for (String name : Arrays.asList(
                "Mozzarella", "Parmesan", "Ricotta", "Goat Cheese", "Buffalo")) {
            list.add(new Topping(name, ToppingCategory.CHEESE));
        }
        return list;
    }

    private List<SignaturePizza> buildSignaturePizzas() {
        List<SignaturePizza> list = new ArrayList<>();

        SignaturePizza margherita = new SignaturePizza(
                "Margherita",
                "Classic mozzarella, tomatoes & basil on marinara + olive oil",
                PizzaSize.MEDIUM, CrustType.REGULAR, "Marinara"
        );
        margherita.addTopping(getToppingByName("Mozzarella"));
        margherita.addTopping(getToppingByName("Tomatoes"));
        margherita.addTopping(getToppingByName("Basil"));
        list.add(margherita);

        SignaturePizza veggie = new SignaturePizza(
                "Veggie",
                "Bell peppers, spinach, olives & onions on marinara with mozzarella",
                PizzaSize.PERSONAL, CrustType.REGULAR, "Marinara"
        );
        veggie.addTopping(getToppingByName("Bell Peppers"));
        veggie.addTopping(getToppingByName("Spinach"));
        veggie.addTopping(getToppingByName("Olives"));
        veggie.addTopping(getToppingByName("Onions"));
        veggie.addTopping(getToppingByName("Mozzarella"));
        list.add(veggie);

        SignaturePizza meatLover = new SignaturePizza(
                "Meat Lover",
                "Pepperoni, sausage, bacon & ham on marinara",
                PizzaSize.LARGE, CrustType.REGULAR, "Marinara"
        );
        meatLover.addTopping(getToppingByName("Pepperoni"));
        meatLover.addTopping(getToppingByName("Sausage"));
        meatLover.addTopping(getToppingByName("Bacon"));
        meatLover.addTopping(getToppingByName("Ham"));
        list.add(meatLover);
        return list;
    }

    public List<Topping> getStandardToppings() {
        return new ArrayList<>(standardToppings);
    }

    public List<Topping> getMeatToppings() {
        return new ArrayList<>(meatToppings);
    }

    public List<Topping> getCheeseToppings() {
        return new ArrayList<>(cheeseToppings);
    }

    public List<String> getSauces() {
        return new ArrayList<>(SAUCES);
    }

    public List<String> getSides() {
        return new ArrayList<>(SIDES);
    }

    public List<String>  getDrinkFlavors() {
        return new ArrayList<>(DRINK_FLAVORS);
    }

    public List<SignaturePizza> getSignaturePizzas() {
        return new ArrayList<>(signaturePizzas);
    }

    public Topping getToppingByName(String name) {
        for (List<Topping> group : Arrays.asList(standardToppings, meatToppings, cheeseToppings)) {
            for (Topping t : group) {
                if (t.getName().equalsIgnoreCase(name)) return t;
            }
        }
        return null;
    }
}
