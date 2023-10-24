package org.exaphex.realty.model;

import java.util.UUID;

public class Building {
    private final String id;
    private String name;
    private String address;
    private String number;
    private String postalCode;
    private String city;

    public Building(String name, String address, String number, String postalCode, String city) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.address = address;
        this.number = number;
        this.postalCode = postalCode;
        this.city = city;
    }

    public Building(String id, String name, String address, String number, String postalCode, String city) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.number = number;
        this.postalCode = postalCode;
        this.city = city;
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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Building))
            return false;
        Building that = (Building) o;
        // check for null keys if you need to
        return this.name.equals(that.name);
    }

    public String getId() {
        return this.id;
    }
}