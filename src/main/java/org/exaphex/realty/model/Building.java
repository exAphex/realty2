package org.exaphex.realty.model;

import java.util.UUID;

public class Building {
    private final String id;
    private final String name;
    private final String address;
    private final String number;
    private final String postalCode;
    private final String city;
    private final float totalArea;

    public Building(String name, String address, String number, String postalCode, String city, float totalArea) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.address = address;
        this.number = number;
        this.postalCode = postalCode;
        this.city = city;
        this.totalArea = totalArea;
    }

    public Building(String id, String name, String address, String number, String postalCode, String city, float totalArea) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.number = number;
        this.postalCode = postalCode;
        this.city = city;
        this.totalArea = totalArea;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getNumber() {
        return number;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCity() {
        return city;
    }

    public float getTotalArea() {
        return totalArea;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Building that))
            return false;
        // check for null keys if you need to
        return this.id.equals(that.id);
    }

    public String getId() {
        return this.id;
    }
}