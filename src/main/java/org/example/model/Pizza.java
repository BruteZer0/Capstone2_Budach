package org.example.model;

import org.example.enums.CrustType;
import org.example.enums.PizzaSize;

import java.math.BigDecimal;

public class Pizza {
    private static final BigDecimal STUFF_CRUST_PRICE = new BigDecimal("2.00");

    private PizzaSize size;
    private CrustType type;
    private boolean stuffedCrust;
    private String sauce;
    private boolean isSignature;
    private String signatureName;


}
