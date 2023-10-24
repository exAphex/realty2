package org.exaphex.realty.model;

import java.util.UUID;

public class Unit {
    private String id;
    private final String name;
    private final String buildingId;

    public Unit(String id, String buildingId, String name) {
        this.id = id;
        this.buildingId = buildingId;
        this.name = name;
    }

    public Unit(String buildingId, String name) {
        this.id = UUID.randomUUID().toString();
        this.buildingId = buildingId;
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

    @Override
    public String toString() {
        return this.name;
    }

    public String getBuildingId() {
        return buildingId;
    }
}
