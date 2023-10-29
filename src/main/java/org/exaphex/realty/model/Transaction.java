package org.exaphex.realty.model;

import java.util.UUID;

public class Transaction {
    public static final int RENT_PAYMENT = 0;
    public static final int DEPOSIT = 1;

    private String id;
    private String date;
    private int type;
    private String unitId;
    private float amount;

    public Transaction(String id, String date, int type, String unitId, float amount) {
        this.id = id;
        this.date = date;
        this.type = type;
        this.unitId = unitId;
        this.amount = amount;
    }

    public Transaction(String date, int type, String unitId, float amount) {
        this.id = UUID.randomUUID().toString();
        this.date = date;
        this.type = type;
        this.unitId = unitId;
        this.amount = amount;
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

    public static String formatTransactionType(int type) {
        return switch (type) {
            case RENT_PAYMENT -> "Rent";
            case DEPOSIT -> "Deposit";
            default -> "Unknown";
        };
    }
}
