package org.example.data;

import org.example.enums.CrustType;
import org.example.enums.PizzaSize;

public class SignaturePizza extends Pizza {
    private final String signaturePizzaName;
    private String description;

    public SignaturePizza(String name, String description,PizzaSize size, CrustType crust, String sauce) {
        super(size, crust, sauce);
        this.signaturePizzaName = name;
        this.description = description;
        super.markAsSignature(name);
    }

    public SignaturePizza(SignaturePizza template, PizzaSize newSize) {
        super(newSize, template.getCrust(), template.getSauce());
        this.signaturePizzaName = template.signaturePizzaName;
        this.description        = template.description;
        super.markAsSignature(signaturePizzaName);

        for (var entry : template.getToppings().entrySet()) {
            for (int i = 0; i < entry.getValue(); i++) {
                this.addTopping(entry.getKey());
            }
        }
    }

    public String getSignaturePizzaName() {
        return signaturePizzaName;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "[SIGNATURE: " + signaturePizzaName + "] " + super.toString();
    }
}
