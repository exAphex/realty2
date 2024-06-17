package org.exaphex.realty.db.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.exaphex.realty.db.DatabaseConnector;
import org.exaphex.realty.model.Document;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DocumentService {

    protected static final Logger logger = LogManager.getLogger();

    public static List<Document> getDocuments() {
        Connection conn = null;
        PreparedStatement statement = null;
        List<Document> retDocuments = new ArrayList<>();
        try {
            conn = DatabaseConnector.getConnection();

            String query = "select * from documents";
            statement = conn.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Document tmpDocument = new Document(rs.getString("id"), rs.getString("name"), rs.getString("date"), rs.getString("description"), rs.getString("filename"), rs.getString("createdon"), rs.getString("lastmodified"), rs.getInt("totalsize"), rs.getBoolean("ismarkdown"), rs.getString("objectid"), rs.getString("documenttypeid"));
                retDocuments.add(tmpDocument);
            }
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            DatabaseConnector.closeStatement(statement);
            DatabaseConnector.closeDatabase(conn);
        }
        return retDocuments;
    }

    public static Blob getDocumentBlob(String id) {
        Connection conn = null;
        PreparedStatement statement = null;
        Blob retFile = null;
        List<Document> retDocuments = new ArrayList<>();
        try {
            conn = DatabaseConnector.getConnection();
            String query = "select * from documents where id = ?";
            statement = conn.prepareStatement(query);
            statement.setString(0, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                retFile = rs.getBlob("fileData");
            }
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            DatabaseConnector.closeStatement(statement);
            DatabaseConnector.closeDatabase(conn);
        }
        return retFile;
    }



}
