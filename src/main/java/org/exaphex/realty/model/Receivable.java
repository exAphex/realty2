package org.exaphex.realty.model;

public class Receivable {
    private String name;
    private float amount;
    private String due;
    private int type;
    private Transaction transaction;

    public Receivable(String name, float amount, String due, int type) {
        this.name = name;
        this.amount = amount;
        this.due = due;
        this.type = type;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public String getName() {
        return name;
    }

    public float getAmount() {
        return amount;
    }

    public String getDue() {
        return due;
    }

    public int getType() {
        return type;
    }
}
