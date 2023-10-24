package org.exaphex.realty.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Valuation {
    private String id;
    private String date;
    private float value;

    public Valuation(String id, String date, float value) {
        this.id = id;
        this.date = date;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(Date date) {
        SimpleDateFormat DateFor = new SimpleDateFormat("dd-MM-yyyy");
        this.date = DateFor.format(date);
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
