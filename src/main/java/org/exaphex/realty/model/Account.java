package org.exaphex.realty.model;

import java.util.UUID;

public class Account {
    private final String id;
    private final String name;
    private final String iban;
    private final String bic;
    private float balance;

    public Account(String id, String name, String iban, String bic) {
        this.id = id;
        this.name = name;
        this.iban = iban;
        this.bic = bic;
        this.balance = 0;
    }

    public Account(String name, String iban, String bic) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.iban = iban;
        this.bic = bic;
        this.balance = 0;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIban() {
        return iban;
    }

    public String getBic() {
        return bic;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Account that))
            return false;
        // check for null keys if you need to
        return this.id.equals(that.id);
    }

    @Override
    public String toString() {
        return this.name;
    }
}
