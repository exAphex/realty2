package org.exaphex.realty.model;

import java.util.UUID;

public class Unit {
    private String id;
    private String name;

    public Unit(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Unit(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
