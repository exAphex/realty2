package org.exaphex.realty.model;

import java.util.Objects;
import java.util.UUID;

public class Credit {
    private final String id;
    private final String unitId;
    private final String name;
    private final String description;
    private final float interestRate;
    private final float redemptionRate;
    private final String startDate;
    private final String endDate;
    private final float amount;
    private float repaidAmount;

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
        this.repaidAmount = 0;
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
        this.repaidAmount = 0;
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

    public float getInstallmentAmount() {
        return ((this.interestRate + this.redemptionRate) * this.amount) / 12;
    }

    public float getRepaidAmount() {
        return repaidAmount;
    }

    public void setRepaidAmount(float repaidAmount) {
        this.repaidAmount = repaidAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Credit credit = (Credit) o;
        return Objects.equals(id, credit.id);
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
