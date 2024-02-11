package org.exaphex.realty.db.migration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.exaphex.realty.db.DatabaseConnector;
import org.exaphex.realty.db.service.ContactService;
import org.exaphex.realty.model.Contact;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RentMigration {
    protected static final Logger logger = LogManager.getLogger();
    public static void migrateContactsOfRents() {
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = DatabaseConnector.getConnection();
            String query = "select * from rents";
            statement = conn.prepareStatement(query);

            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String id = rs.getString("id");
                String firstName = rs.getString("firstname");
                String lastName = rs.getString("lastname");
                Contact contact = new Contact(firstName, lastName, "","");
                ContactService.addContact(contact);
                updateContactId(id, contact.getId());
            }
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            DatabaseConnector.closeStatement(statement);
            DatabaseConnector.closeDatabase(conn);
        }
    }

    private static void updateContactId(String id, String contactId) {
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = DatabaseConnector.getConnection();
            statement = conn.prepareStatement("UPDATE rents SET contactid = ? where id = ?");
            statement.setString(1, contactId);
            statement.setString(2, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            DatabaseConnector.closeStatement(statement);
            DatabaseConnector.closeDatabase(conn);
        }
    }
}
