package org.exaphex.realty.model.transport;

import org.exaphex.realty.model.*;

import java.util.List;

public class ExportModel {
    private String version;
    private List<Building> buildings;
    private List<Credit> credits;
    private List<Rent> rents;
    private List<Transaction> transactions;
    private List<Unit> units;
    private List<Valuation> valuations;

    public ExportModel() {
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<Building> getBuildings() {
        return buildings;
    }

    public void setBuildings(List<Building> buildings) {
        this.buildings = buildings;
    }

    public List<Credit> getCredits() {
        return credits;
    }

    public void setCredits(List<Credit> credits) {
        this.credits = credits;
    }

    public List<Rent> getRents() {
        return rents;
    }

    public void setRents(List<Rent> rents) {
        this.rents = rents;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public List<Unit> getUnits() {
        return units;
    }

    public void setUnits(List<Unit> units) {
        this.units = units;
    }

    public List<Valuation> getValuations() {
        return valuations;
    }

    public void setValuations(List<Valuation> valuations) {
        this.valuations = valuations;
    }
}
