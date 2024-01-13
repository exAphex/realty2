package org.exaphex.realty.model;

import java.util.Objects;
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
    private final int numOfTentants;

    public Rent(String id, String firstName, String lastName, String unitId, String startDate, String endDate, float rentalPrice, float extraCosts, float deposit, int numOfTentants) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.unitId = unitId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.rentalPrice = rentalPrice;
        this.extraCosts = extraCosts;
        this.deposit = deposit;
        this.numOfTentants = numOfTentants;
    }

    public Rent(String firstName, String lastName, String unitId, String startDate, String endDate, float rentalPrice, float extraCosts, float deposit, int numOfTentants) {
        this.id = UUID.randomUUID().toString();
        this.firstName = firstName;
        this.lastName = lastName;
        this.unitId = unitId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.rentalPrice = rentalPrice;
        this.extraCosts = extraCosts;
        this.deposit = deposit;
        this.numOfTentants = numOfTentants;
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

    public int getNumOfTentants() {
        return numOfTentants;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rent rent = (Rent) o;
        return Objects.equals(id, rent.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Rent: " + this.firstName + " " + this.lastName;
    }
}
