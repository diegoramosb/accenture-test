package com.diegoramosb.accenturetest;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

public class PurchaseLogic {

    public static final int DELIVERY_COST = 5000;

    public static final int MIN_COST = 70000;

    private PurchaseLogic() {
        throw new IllegalStateException();
    }

    public static double calculateProductCost(List<Product> products) {
        double cost = 0;
        for (Product product : products) {
            cost += product.getCost();
        }
        return cost;
    }

    public static double calculateIva(List<Product> products) {
        return calculateProductCost(products) * 0.19;
    }

    public static double calculateDeliveryCost(List<Product> products) {
        return calculateProductCost(products) <= 100000 ? DELIVERY_COST : 0;
    }

    public static double calculateTotalCost(Purchase purchase) {
        return purchase.getDeliveryCost() + purchase.getProductCost() + purchase.getIva();
    }

    public static Timestamp getTimestamp() {
        return new Timestamp(Instant.now().toEpochMilli());
    }

    public static boolean validateCost(double cost) {
        return cost >= MIN_COST;
    }

    public static boolean validateEdit(Purchase oldPurchase, Purchase newPurchase) {
        boolean valid = validateTimestampDifference(oldPurchase.getTimestamp(), 5);
        if (oldPurchase.getProductCost() > newPurchase.getProductCost()) {
            valid = false;
        }
        return valid;
    }

    public static boolean validateDelete(Purchase purchase) {
        return validateTimestampDifference(purchase.getTimestamp(), 12);
    }

    public static boolean validateTimestampDifference(Timestamp timestamp, int hours) {
        Timestamp conditionTimestamp = new Timestamp(timestamp.getTime() + hours * 60 * 60 * 1000);
        Timestamp newTimestamp = getTimestamp();
        return newTimestamp.before(conditionTimestamp);
    }
}
