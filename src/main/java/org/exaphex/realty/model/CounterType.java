package org.exaphex.realty.model;

import java.util.Objects;
import java.util.UUID;

public class CounterType {
    private final String id;
    private final String name;

    public CounterType(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public CounterType(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CounterType counterType = (CounterType) o;
        return Objects.equals(id, counterType.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return this.name;
    }
}
