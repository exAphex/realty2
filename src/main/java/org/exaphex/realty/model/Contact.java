package org.exaphex.realty.model;

import java.util.Objects;
import java.util.UUID;

public class Contact {
    private final String id;
    private final String firstName;
    private final String lastName;
    private final String telNumber;
    private final String mail;

    public Contact(String id, String firstName, String lastName, String telNumber, String mail) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.telNumber = telNumber;
        this.mail = mail;
    }

    public Contact(String firstName, String lastName, String telNumber, String mail) {
        this.id = UUID.randomUUID().toString();
        this.firstName = firstName;
        this.lastName = lastName;
        this.telNumber = telNumber;
        this.mail = mail;
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

    public String getTelNumber() {
        return telNumber;
    }

    public String getMail() {
        return mail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact rent = (Contact) o;
        return Objects.equals(id, rent.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return this.getFirstName() + " " + this.getLastName();
    }
}
