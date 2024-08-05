package org.exaphex.realty.db.service;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.exaphex.realty.db.DatabaseConnector;
import org.exaphex.realty.model.Building;
import org.exaphex.realty.model.Document;
import org.exaphex.realty.model.Unit;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class DocumentService {
    protected static final Logger logger = LogManager.getLogger();

    public static List<Document> _getDocument(String id, boolean withJSON) {
        Connection conn = null;
        PreparedStatement statement = null;
        List<Document> retDocuments = new ArrayList<>();
        try {
            conn = DatabaseConnector.getConnection();

            String query = "select * from documents";
            if (id != null) {
                query += " where objectid = ?";
                statement = conn.prepareStatement(query);
                statement.setString(1, id);
            } else {
                statement = conn.prepareStatement(query);
            }

            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Document tmpDocument = new Document(rs.getString("id"), rs.getString("name"), rs.getString("date"), rs.getString("description"), rs.getString("fileName"), rs.getString("createdOn"), rs.getString("lastModified"), rs.getInt("totalSize"), rs.getBoolean("isMarkdown"), rs.getString("objectId"), rs.getString("documentTypeId"));
                if (withJSON) {
                    InputStream input = rs.getBinaryStream("document");
                    byte[] sourceBytes = IOUtils.toByteArray(input);
                    String encodedString = Base64.getEncoder().encodeToString(sourceBytes);
                    tmpDocument.setDocumentJSON(encodedString);
                }
                retDocuments.add(tmpDocument);
            }
        } catch (SQLException | IOException e) {
            logger.error(e);
        } finally {
            DatabaseConnector.closeStatement(statement);
            DatabaseConnector.closeDatabase(conn);
        }
        return retDocuments;
    }

    public static List<Document> getDocument() {
        return _getDocument(null,false);
    }

    public static List<Document> getDocument(Building building) {
        return _getDocument(building.getId(),false);
    }

    public static List<Document> getDocument(Unit unit) {
        return _getDocument(unit.getId(),false);
    }

    public static List<Document> getDocumentsWithJSON() {
        return _getDocument(null, true);
    }

    public static void saveDocumentBinaryToFile(String id, File f) throws SQLException, IOException {
        Connection conn = null;
        PreparedStatement statement = null;
        FileOutputStream fileOutputStream= null;
        try {
            conn = DatabaseConnector.getConnection();

            String query = "select document from documents where id = ?";
            statement = conn.prepareStatement(query);
            statement.setString(1, id);
            ResultSet rs = statement.executeQuery();
            fileOutputStream = new FileOutputStream(f);
            while (rs.next()) {
                InputStream input = rs.getBinaryStream("document");
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = input.read(buffer)) > 0) {
                    fileOutputStream.write(buffer, 0, bytesRead);
                }
            }
        } finally {
            DatabaseConnector.closeStatement(statement);
            DatabaseConnector.closeDatabase(conn);
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
        }
    }

    public static void addDocument(Document document, byte[] bytes) {
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = DatabaseConnector.getConnection();
            statement = conn.prepareStatement("INSERT INTO documents (id, name, date, description, filename, createdon, lastmodified, totalsize, ismarkdown, objectid, documenttypeid, document) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)");
            statement.setString(1, document.getId());
            statement.setString(2, document.getName());
            statement.setString(3, document.getDate());
            statement.setString(4, document.getDescription());
            statement.setString(5, document.getFileName());
            statement.setString(6, document.getCreatedOn());
            statement.setString(7, document.getLastModified());
            statement.setLong(8, document.getTotalSize());
            statement.setBoolean(9, document.isMarkdown());
            statement.setString(10, document.getObjectId());
            statement.setString(11, document.getDocumentTypeId());
            ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
            statement.setBlob(12, inputStream);
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            DatabaseConnector.closeStatement(statement);
            DatabaseConnector.closeDatabase(conn);
        }
    }

    public static void addDocuments(List<Document> documents) {
        for (Document d : documents) {
            byte[] documentBytes = Base64.getDecoder().decode(d.getDocumentJSON());
            addDocument(d, documentBytes);
        }
    }

    public static void deleteDocument(Document document) {
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = DatabaseConnector.getConnection();
            statement = conn.prepareStatement("DELETE FROM documents WHERE id = ?");
            statement.setString(1, document.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            DatabaseConnector.closeStatement(statement);
            DatabaseConnector.closeDatabase(conn);
        }
    }

    private static void _deleteDocument(String id) {
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = DatabaseConnector.getConnection();
            statement = conn.prepareStatement("DELETE FROM documents WHERE objectid = ?");
            statement.setString(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            DatabaseConnector.closeStatement(statement);
            DatabaseConnector.closeDatabase(conn);
        }
    }

    public static void deleteDocument(Unit unit) {
        _deleteDocument(unit.getId());
    }

    public static void deleteDocument(Building building) {
        _deleteDocument(building.getId());
    }

}
