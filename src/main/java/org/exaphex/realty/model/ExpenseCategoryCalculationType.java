package org.exaphex.realty.model;

import java.util.Objects;

public class ExpenseCategoryCalculationType {
    private final int id;
    private final String name;
    private final String displayName;

    public ExpenseCategoryCalculationType(int id, String name, String displayName) {
        this.id = id;
        this.name = name;
        this.displayName = displayName;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExpenseCategoryCalculationType ecct = (ExpenseCategoryCalculationType) o;
        return Objects.equals(id, ecct.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return this.displayName;
    }
}
