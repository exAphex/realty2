package org.exaphex.realty.db.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.exaphex.realty.db.DatabaseConnector;
import org.exaphex.realty.model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ContactService {
    protected static final Logger logger = LogManager.getLogger();
    public static List<Contact> getContacts(String id) {
        Connection conn = null;
        PreparedStatement statement = null;
        List<Contact> retContacts = new ArrayList<>();
        try {
            conn = DatabaseConnector.getConnection();
            String query = "select * from contacts";
            if (id != null) {
                query += " where id = ?";
                statement = conn.prepareStatement(query);
                statement.setString(1, id);
            } else {
                statement = conn.prepareStatement(query);
            }

            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Contact tmpContact = new Contact(rs.getString("id"), rs.getString("firstname"), rs.getString("lastname"), rs.getString("telnumber"), rs.getString("mail"));
                retContacts.add(tmpContact);
            }
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            DatabaseConnector.closeStatement(statement);
            DatabaseConnector.closeDatabase(conn);
        }
        return retContacts;
    }

    public static List<Contact> getContacts() {
        return getContacts(null);
    }

    public static void addContact(Contact contact) {
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = DatabaseConnector.getConnection();
            statement = conn.prepareStatement("INSERT INTO contacts (id, firstname, lastname, mail, telnumber) VALUES (?,?,?,?,?)");
            statement.setString(1, contact.getId());
            statement.setString(2, contact.getFirstName());
            statement.setString(3, contact.getLastName());
            statement.setString(4, contact.getMail());
            statement.setString(5, contact.getTelNumber());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            DatabaseConnector.closeStatement(statement);
            DatabaseConnector.closeDatabase(conn);
        }
    }

    public static void updateContact(Contact contact) {
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = DatabaseConnector.getConnection();
            statement = conn.prepareStatement("UPDATE contacts SET firstname = ?, lastname = ?, mail = ?, telnumber = ? where id = ?");
            statement.setString(1, contact.getFirstName());
            statement.setString(2, contact.getLastName());
            statement.setString(3, contact.getMail());
            statement.setString(4, contact.getTelNumber());
            statement.setString(5, contact.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            DatabaseConnector.closeStatement(statement);
            DatabaseConnector.closeDatabase(conn);
        }
    }

    private static void _deleteContact(Contact contact) {
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = DatabaseConnector.getConnection();
            statement = conn.prepareStatement("DELETE FROM contacts WHERE id = ?");
            statement.setString(1, contact.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            DatabaseConnector.closeStatement(statement);
            DatabaseConnector.closeDatabase(conn);
        }
    }

    public static void deleteContact(Contact contact) throws Exception {
        List<Rent> foundRents = RentService.getRentsByContact(contact);
        if (foundRents.isEmpty()) {
            _deleteContact(contact);
        } else {
            throw new Exception("DeleteRentFirstException");
        }
    }
}
