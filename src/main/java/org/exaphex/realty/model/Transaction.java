package org.exaphex.realty.model;

import java.util.UUID;

public class Transaction {
    public static final int RENT_PAYMENT = 0;
    public static final int DEPOSIT = 1;
    public static final int CREDIT_PAYMENT = 2;
    private final String id;
    private final String date;
    private final int type;
    private final String unitId;
    private float amount;
    private float secondaryAmount;
    private final String description;
    private final String reference;

    public Transaction(String id, String description, String reference, String date, int type, String unitId, float amount) {
        this.id = id;
        this.description = description;
        this.reference = reference;
        this.date = date;
        this.type = type;
        this.unitId = unitId;
        this.amount = amount;
        this.secondaryAmount = 0;
    }

    public Transaction(String id, String description, String reference, String date, int type, String unitId, float amount, float secondaryAmount) {
        this.id = id;
        this.description = description;
        this.reference = reference;
        this.date = date;
        this.type = type;
        this.unitId = unitId;
        this.amount = amount;
        this.secondaryAmount = secondaryAmount;
    }

    public Transaction(String description, String reference, String date, int type, String unitId, float amount) {
        this.id = UUID.randomUUID().toString();
        this.description = description;
        this.reference = reference;
        this.date = date;
        this.type = type;
        this.unitId = unitId;
        this.amount = amount;
        this.secondaryAmount = 0;
    }

    public Transaction(String description, String reference, String date, int type, String unitId, float amount, float secondaryAmount) {
        this.id = UUID.randomUUID().toString();
        this.description = description;
        this.reference = reference;
        this.date = date;
        this.type = type;
        this.unitId = unitId;
        this.amount = amount;
        this.secondaryAmount = secondaryAmount;
    }

    public String getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public String getUnitId() {
        return unitId;
    }

    public String getDate() {
        return date;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public float getSecondaryAmount() {
        return secondaryAmount;
    }

    public void setSecondaryAmount(float secondaryAmount) {
        this.secondaryAmount = secondaryAmount;
    }

    public static String formatTransactionType(int type) {
        return switch (type) {
            case RENT_PAYMENT -> "Rent";
            case DEPOSIT -> "Deposit";
            case CREDIT_PAYMENT -> "Credit payment";
            default -> "Unknown";
        };
    }

    public static int getTypeIdByTransactionType(String type) {
        return switch (type) {
            case "Rent" -> RENT_PAYMENT;
            case "Deposit" -> DEPOSIT;
            case "Credit payment" -> CREDIT_PAYMENT;
            default -> -1;
        };
    }

    public static boolean isExpense(int type) {
        return switch (type) {
            case RENT_PAYMENT -> false;
            case DEPOSIT -> false;
            case CREDIT_PAYMENT -> true;
            default -> false;
        };
    }

    public static boolean isIncome(int type) {
        return switch (type) {
            case RENT_PAYMENT -> true;
            case DEPOSIT -> true;
            case CREDIT_PAYMENT -> false;
            default -> false;
        };
    }

    public String getDescription() {
        return description;
    }

    public String getReference() {
        return reference;
    }
}
