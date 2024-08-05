package org.exaphex.realty.model;

import java.util.Objects;
import java.util.UUID;

public class DocumentType {
    private final String id;
    private final String name;

    public DocumentType(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public DocumentType(String name) {
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
        DocumentType dct = (DocumentType) o;
        return Objects.equals(id, dct.id);
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
