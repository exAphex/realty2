package org.exaphex.realty.model;

import java.util.UUID;

public class Transaction {
    public static final int RENT_PAYMENT = 0;
    public static final int DEPOSIT = 1;
    public static final int CREDIT_PAYMENT = 2;
    public static final int EXPENSE = 3;
    private final String id;
    private final String date;
    private final int type;
    private final String unitId;
    private final float amount;
    private final float secondaryAmount;
    private final String description;
    private final String reference;
    private final String expenseCategory;
    private String accountId;

    public Transaction(String id, String description, String reference, String date, int type, String unitId, float amount, String expenseCategory, String accountId) {
        this.id = id;
        this.description = description;
        this.reference = reference;
        this.date = date;
        this.type = type;
        this.unitId = unitId;
        this.amount = amount;
        this.secondaryAmount = 0;
        this.expenseCategory = expenseCategory;
        this.accountId = accountId;
    }

    public Transaction(String id, String description, String reference, String date, int type, String unitId, float amount, float secondaryAmount, String expenseCategory, String accountId) {
        this.id = id;
        this.description = description;
        this.reference = reference;
        this.date = date;
        this.type = type;
        this.unitId = unitId;
        this.amount = amount;
        this.secondaryAmount = secondaryAmount;
        this.expenseCategory = expenseCategory;
        this.accountId = accountId;
    }

    public Transaction(String description, String reference, String date, int type, String unitId, float amount, String expenseCategory, String accountId) {
        this.id = UUID.randomUUID().toString();
        this.description = description;
        this.reference = reference;
        this.date = date;
        this.type = type;
        this.unitId = unitId;
        this.amount = amount;
        this.secondaryAmount = 0;
        this.expenseCategory = expenseCategory;
        this.accountId = accountId;
    }

    public Transaction(String description, String reference, String date, int type, String unitId, float amount, float secondaryAmount, String expenseCategory, String accountId) {
        this.id = UUID.randomUUID().toString();
        this.description = description;
        this.reference = reference;
        this.date = date;
        this.type = type;
        this.unitId = unitId;
        this.amount = amount;
        this.secondaryAmount = secondaryAmount;
        this.expenseCategory = expenseCategory;
        this.accountId = accountId;
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

    public float getSecondaryAmount() {
        return secondaryAmount;
    }

    public static String formatTransactionType(int type) {
        return switch (type) {
            case RENT_PAYMENT -> "Rent";
            case DEPOSIT -> "Deposit";
            case CREDIT_PAYMENT -> "Credit payment";
            case EXPENSE -> "Expense";
            default -> "Unknown";
        };
    }

    public static int getTypeIdByTransactionType(String type) {
        return switch (type) {
            case "Rent" -> RENT_PAYMENT;
            case "Deposit" -> DEPOSIT;
            case "Credit payment" -> CREDIT_PAYMENT;
            case "Expense" -> EXPENSE;
            default -> -1;
        };
    }

    public static boolean isExpense(int type) {
        return switch (type) {
            case CREDIT_PAYMENT, EXPENSE -> true;
            default -> false;
        };
    }

    public static boolean isIncome(int type) {
        return switch (type) {
            case RENT_PAYMENT, DEPOSIT -> true;
            default -> false;
        };
    }

    public String getDescription() {
        return description;
    }

    public String getReference() {
        return reference;
    }

    public String getExpenseCategory() {return expenseCategory; }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
