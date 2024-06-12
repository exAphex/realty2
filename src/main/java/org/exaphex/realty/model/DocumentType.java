package org.exaphex.realty.model;

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
}
