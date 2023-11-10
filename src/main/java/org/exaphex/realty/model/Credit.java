package org.exaphex.realty.model;

import java.util.UUID;

public class Credit {
    private String id;
    private String unitId;
    private String name;
    private String description;
    private float interestRate;
    private float redemptionRate;
    private String startDate;
    private String endDate;
    private float amount;

    public Credit(String id, String unitId, String name, String description, float interestRate, float redemptionRate, String startDate, String endDate, float amount) {
        this.id = id;
        this.unitId = unitId;
        this.name = name;
        this.description = description;
        this.interestRate = interestRate;
        this.redemptionRate = redemptionRate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.amount = amount;
    }

    public Credit(String name, String unitId, String description, float interestRate, float redemptionRate, String startDate, String endDate, float amount) {
        this.id = UUID.randomUUID().toString();
        this.unitId = unitId;
        this.name = name;
        this.description = description;
        this.interestRate = interestRate;
        this.redemptionRate = redemptionRate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public float getInterestRate() {
        return interestRate;
    }

    public float getRedemptionRate() {
        return redemptionRate;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getId() {
        return id;
    }

    public String getUnitId() {
        return unitId;
    }

    public float getAmount() {
        return amount;
    }
}
