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
    private List<ExpenseCategory> expenseCategories;
    private List<Contact> contacts;
    private List<Account> accounts;
    private List<DocumentType> documentTypes;
    private List<Document> documents;
    private List<CounterType> counterTypes;
    private List<CounterRecord> counterRecords;

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

    public List<ExpenseCategory> getExpenseCategories() {
        return expenseCategories;
    }

    public void setExpenseCategories(List<ExpenseCategory> expenseCategories) {
        this.expenseCategories = expenseCategories;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public List<DocumentType> getDocumentTypes() {
        return documentTypes;
    }

    public void setDocumentTypes(List<DocumentType> documentTypes) {
        this.documentTypes = documentTypes;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    public List<CounterType> getCounterTypes() {
        return counterTypes;
    }

    public void setCounterTypes(List<CounterType> counterTypes) {
        this.counterTypes = counterTypes;
    }

    public List<CounterRecord> getCounterRecords() {
        return counterRecords;
    }

    public void setCounterRecords(List<CounterRecord> counterRecords) {
        this.counterRecords = counterRecords;
    }
}
