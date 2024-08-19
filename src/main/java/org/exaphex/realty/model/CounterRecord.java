package org.exaphex.realty.model;

import java.util.UUID;

public class CounterRecord {
    private final String id;
    private final String counterTypeId;
    private final String objectId;
    private final String date;
    private final String value;
    private final String description;

    public CounterRecord(String id, String counterType, String objectId, String date, String value, String description) {
        this.id = id;
        this.counterTypeId = counterType;
        this.objectId = objectId;
        this.date = date;
        this.value = value;
        this.description = description;
    }

    public CounterRecord(String counterType, String objectId, String date, String value, String description) {
        this.id = UUID.randomUUID().toString();
        this.counterTypeId = counterType;
        this.objectId = objectId;
        this.date = date;
        this.value = value;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String getCounterTypeId() {
        return counterTypeId;
    }

    public String getObjectId() {
        return objectId;
    }

    public String getDate() {
        return date;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }
}
