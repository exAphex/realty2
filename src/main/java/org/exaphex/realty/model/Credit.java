package org.exaphex.realty.model;

public class Credit {
    private String id;
    private String unitId;
    private String name;
    private String description;
    private float interestRate;
    private float redemptionRate;
    private String startDate;

    public Credit(String id, String unitId, String name, String description, float interestRate, float redemptionRate, String startDate) {
        this.id = id;
        this.unitId = unitId;
        this.name = name;
        this.description = description;
        this.interestRate = interestRate;
        this.redemptionRate = redemptionRate;
        this.startDate = startDate;
    }

    public Credit(String name, String unitId, String description, float interestRate, float redemptionRate, String startDate) {
        this.unitId = unitId;
        this.name = name;
        this.description = description;
        this.interestRate = interestRate;
        this.redemptionRate = redemptionRate;
        this.startDate = startDate;
    }
}
