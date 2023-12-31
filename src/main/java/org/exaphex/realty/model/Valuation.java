package org.exaphex.realty.model;

public class Valuation {
    private final String id;
    private final String date;
    private final float value;

    public Valuation(String id, String date, float value) {
        this.id = id;
        this.date = date;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public float getValue() {
        return value;
    }
}
