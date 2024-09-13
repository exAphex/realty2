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

public class CounterRecordService {
    protected static final Logger logger = LogManager.getLogger();

    public static List<CounterRecord> _getCounterRecord(String id) {
        Connection conn = null;
        PreparedStatement statement = null;
        List<CounterRecord> retCounterRecords = new ArrayList<>();
        try {
            conn = DatabaseConnector.getConnection();

            String query = "select * from counterrecords";
            if (id != null) {
                query += " where objectid = ?";
                statement = conn.prepareStatement(query);
                statement.setString(1, id);
            } else {
                statement = conn.prepareStatement(query);
            }

            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                CounterRecord tmpCounterRecord = new CounterRecord(rs.getString("id"), rs.getString("countertypeid"), rs.getString("objectid"), rs.getString("date"), rs.getString("val"), rs.getString("description"));
                retCounterRecords.add(tmpCounterRecord);
            }
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            DatabaseConnector.closeStatement(statement);
            DatabaseConnector.closeDatabase(conn);
        }
        return retCounterRecords;
    }

    public static List<CounterRecord> getCounterRecords() {
        return _getCounterRecord(null);
    }

    public static List<CounterRecord> getCounterRecords(Building building) {
        return _getCounterRecord(building.getId());
    }

    public static List<CounterRecord> getCounterRecords(Unit unit) {
        return _getCounterRecord(unit.getId());
    }

    public static void addCounterRecord(CounterRecord counterRecord) {
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = DatabaseConnector.getConnection();
            statement = conn.prepareStatement("INSERT INTO counterrecords (id, countertypeid, objectid, date, val, description) VALUES (?,?,?,?,?,?)");
            statement.setString(1, counterRecord.getId());
            statement.setString(2, counterRecord.getCounterTypeId());
            statement.setString(3, counterRecord.getObjectId());
            statement.setString(4, counterRecord.getDate());
            statement.setString(5, counterRecord.getValue());
            statement.setString(6, counterRecord.getDescription());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            DatabaseConnector.closeStatement(statement);
            DatabaseConnector.closeDatabase(conn);
        }
    }

    public static void updateCounterRecord(CounterRecord counterRecord) {
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = DatabaseConnector.getConnection();
            statement = conn.prepareStatement("UPDATE counterrecords SET date = ?, val = ?, description = ?, countertypeid = ? where id = ?");
            statement.setString(1, counterRecord.getDate());
            statement.setString(2, counterRecord.getValue());
            statement.setString(3, counterRecord.getDescription());
            statement.setString(4, counterRecord.getCounterTypeId());
            statement.setString(5, counterRecord.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            DatabaseConnector.closeStatement(statement);
            DatabaseConnector.closeDatabase(conn);
        }
    }

    public static void addCounterRecords(List<CounterRecord> counterRecords) {
        for (CounterRecord cr : counterRecords) {
            addCounterRecord(cr);
        }
    }

    public static void deleteCounterRecord(CounterRecord counterRecord) {
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = DatabaseConnector.getConnection();
            statement = conn.prepareStatement("DELETE FROM counterrecords WHERE id = ?");
            statement.setString(1, counterRecord.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            DatabaseConnector.closeStatement(statement);
            DatabaseConnector.closeDatabase(conn);
        }
    }

    private static void _deleteCounterRecord(String id) {
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = DatabaseConnector.getConnection();
            statement = conn.prepareStatement("DELETE FROM counterrecords WHERE objectid = ?");
            statement.setString(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            DatabaseConnector.closeStatement(statement);
            DatabaseConnector.closeDatabase(conn);
        }
    }

    public static void deleteCounterRecord(CounterType counterType) {
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = DatabaseConnector.getConnection();
            statement = conn.prepareStatement("DELETE FROM counterrecords WHERE countertypeid = ?");
            statement.setString(1, counterType.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            DatabaseConnector.closeStatement(statement);
            DatabaseConnector.closeDatabase(conn);
        }
    }

    public static void deleteCounterRecord(Unit unit) {
        _deleteCounterRecord(unit.getId());
    }

    public static void deleteCounterRecord(Building building) {
        _deleteCounterRecord(building.getId());
    }

}
