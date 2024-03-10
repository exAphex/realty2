package org.exaphex.realty.model;

import java.util.Objects;
import java.util.UUID;

public class Unit {
    private final String id;
    private final String name;
    private final String buildingId;
    private final float area;
    private final float shares;

    public Unit(String id, String buildingId, String name, float area, float shares) {
        this.id = id;
        this.buildingId = buildingId;
        this.name = name;
        this.area = area;
        this.shares = shares;
    }

    public Unit(String buildingId, String name, float area, float shares) {
        this.id = UUID.randomUUID().toString();
        this.buildingId = buildingId;
        this.name = name;
        this.area = area;
        this.shares = shares;
    }

    public float getShares() {
        return shares;
    }

    public String getId() {
        return id;
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

    public float getArea() {
        return area;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Unit unit = (Unit) o;
        return Objects.equals(id, unit.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
