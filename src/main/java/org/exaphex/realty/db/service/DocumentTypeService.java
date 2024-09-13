package org.exaphex.realty.db.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.exaphex.realty.db.DatabaseConnector;
import org.exaphex.realty.model.DocumentType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DocumentTypeService {

    protected static final Logger logger = LogManager.getLogger();

    public static List<DocumentType> getDocumentTypes() {
        Connection conn = null;
        PreparedStatement statement = null;
        List<DocumentType> retTypes = new ArrayList<>();
        try {
            conn = DatabaseConnector.getConnection();

            String query = "select * from documenttypes";
            statement = conn.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                DocumentType tmpDocumentType = new DocumentType(rs.getString("id"), rs.getString("name"));
                retTypes.add(tmpDocumentType);
            }
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            DatabaseConnector.closeStatement(statement);
            DatabaseConnector.closeDatabase(conn);
        }
        return retTypes;
    }

    public static void addDocumentTypes(List<DocumentType> documentTypes) {
        for (DocumentType dt : documentTypes) {
            addDocumentType(dt);
        }
    }

    public static void addDocumentType(DocumentType documentType) {
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = DatabaseConnector.getConnection();
            statement = conn.prepareStatement("INSERT INTO documenttypes (id, name) VALUES (?,?)");
            statement.setString(1, documentType.getId());
            statement.setString(2, documentType.getName());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            DatabaseConnector.closeStatement(statement);
            DatabaseConnector.closeDatabase(conn);
        }
    }

    public static void updateDocumentType(DocumentType documentType) {
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = DatabaseConnector.getConnection();
            statement = conn.prepareStatement("UPDATE documenttypes SET name = ? where id = ?");
            statement.setString(1, documentType.getName());
            statement.setString(2, documentType.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            DatabaseConnector.closeStatement(statement);
            DatabaseConnector.closeDatabase(conn);
        }
    }

    public static void deleteDocumentType(DocumentType documentType) {
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = DatabaseConnector.getConnection();
            statement = conn.prepareStatement("DELETE FROM documenttypes where id = ?");
            statement.setString(1, documentType.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            DatabaseConnector.closeStatement(statement);
            DatabaseConnector.closeDatabase(conn);
        }
    }

}
