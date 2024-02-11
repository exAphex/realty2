package org.exaphex.realty.model;

import org.exaphex.realty.db.service.ContactService;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Rent {

    private final String id;
    private final String unitId;
    private final String startDate;
    private final String endDate;
    private final String contactId;
    private final float rentalPrice;
    private final float extraCosts;
    private final float deposit;
    private final int numOfTentants;
    private Contact contact = new Contact("","","","");

    public Rent(String id, String contactId, String unitId, String startDate, String endDate, float rentalPrice, float extraCosts, float deposit, int numOfTentants, boolean loadEager) {
        this.id = id;
        this.contactId = contactId;
        this.unitId = unitId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.rentalPrice = rentalPrice;
        this.extraCosts = extraCosts;
        this.deposit = deposit;
        this.numOfTentants = numOfTentants;
        if (loadEager) {
            loadEagerData();
        }
    }

    public Rent(String contactId, String unitId, String startDate, String endDate, float rentalPrice, float extraCosts, float deposit, int numOfTentants, boolean loadEager) {
        this.id = UUID.randomUUID().toString();
        this.contactId = contactId;
        this.unitId = unitId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.rentalPrice = rentalPrice;
        this.extraCosts = extraCosts;
        this.deposit = deposit;
        this.numOfTentants = numOfTentants;
        if (loadEager) {
            loadEagerData();
        }
    }

    private void loadEagerData() {
        List<Contact> contacts = ContactService.getContacts(this.contactId);
        if (!contacts.isEmpty()) {
            this.contact = contacts.get(0);
        }
    }

    public Contact getContact() {
        return contact;
    }

    public String getId() {
        return id;
    }
    public String getContactId() {
        return contactId;
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
        return "Rent: " + this.getContact().getFirstName() + " " + this.getContact().getLastName();
    }
}
