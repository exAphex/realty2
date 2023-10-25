package org.exaphex.realty.model;

import java.util.UUID;

public class Rent {

    private final String id;
    private final String firstName;
    private final String lastName;
    private final String unitId;
    private final String startDate;
    private final String endDate;
    private final float rentalPrice;
    private final float extraCosts;
    private final float deposit;

    public Rent(String id, String firstName, String lastName, String unitId, String startDate, String endDate, float rentalPrice, float extraCosts, float deposit) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.unitId = unitId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.rentalPrice = rentalPrice;
        this.extraCosts = extraCosts;
        this.deposit = deposit;
    }

    public Rent(String firstName, String lastName, String unitId, String startDate, String endDate, float rentalPrice, float extraCosts, float deposit) {
        this.id = UUID.randomUUID().toString();
        this.firstName = firstName;
        this.lastName = lastName;
        this.unitId = unitId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.rentalPrice = rentalPrice;
        this.extraCosts = extraCosts;
        this.deposit = deposit;
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUnitId() {
        return unitId;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public float getRentalPrice() {
        return rentalPrice;
    }

    public float getExtraCosts() {
        return extraCosts;
    }

    public float getDeposit() {
        return deposit;
    }
}
