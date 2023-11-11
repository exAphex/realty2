package org.exaphex.realty.model;

public class PaymentCheck {
    private String name;
    private float amount;
    private String date;
    private float paidAmount;
    private Transaction transaction;

    public PaymentCheck(String name, float amount, String date, float paidAmount) {
        this.name = name;
        this.amount = amount;
        this.date = date;
        this.paidAmount = paidAmount;
    }

    public String getName() {
        return name;
    }

    public float getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    public float getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(float paidAmount) {
        this.paidAmount = paidAmount;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
}
