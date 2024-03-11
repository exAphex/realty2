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

@SuppressWarnings("SpellCheckingInspection")
public class CreditService {
    protected static final Logger logger = LogManager.getLogger();
    public static List<Credit> _getCredit(String id) {
        Connection conn = null;
        PreparedStatement statement = null;
        List<Credit> retCredits = new ArrayList<>();
        try {
            conn = DatabaseConnector.getConnection();

            String query = "select * from credits";
            if (id != null) {
                query += " where objectid = ?";
                statement = conn.prepareStatement(query);
                statement.setString(1, id);
            } else {
                statement = conn.prepareStatement(query);
            }

            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Credit tmpCredit = new Credit(rs.getString("id"), rs.getString("objectid"), rs.getString("name"), rs.getString("description"), rs.getFloat("interestrate"), rs.getFloat("redemptionrate"), rs.getString("startdate"), rs.getString("enddate"), rs.getFloat("amount"));
                retCredits.add(tmpCredit);
            }
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            DatabaseConnector.closeStatement(statement);
            DatabaseConnector.closeDatabase(conn);
        }
        return retCredits;
    }

    public static List<Credit> getCredit() {
        return _getCredit(null);
    }

    public static List<Credit> getCredit(Building building) {
        return _getCredit(building.getId());
    }

    public static List<Credit> getCredit(Unit unit) {
        return _getCredit(unit.getId());
    }

    public static void addCredit(Credit credit) {
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = DatabaseConnector.getConnection();
            statement = conn.prepareStatement("INSERT INTO credits (id, objectid, name, description, interestrate, redemptionrate, startdate, enddate, amount) VALUES (?,?,?,?,?,?,?,?,?)");
            statement.setString(1, credit.getId());
            statement.setString(2, credit.getObjectId());
            statement.setString(3, credit.getName());
            statement.setString(4, credit.getDescription());
            statement.setFloat(5, credit.getInterestRate());
            statement.setFloat(6, credit.getRedemptionRate());
            statement.setString(7, credit.getStartDate());
            statement.setString(8, credit.getEndDate());
            statement.setFloat(9, credit.getAmount());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            DatabaseConnector.closeStatement(statement);
            DatabaseConnector.closeDatabase(conn);
        }
    }

    public static void addCredits(List<Credit> credits) {
        for (Credit c : credits) {
            addCredit(c);
        }
    }

    public static void deleteCredit(Credit credit) {
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = DatabaseConnector.getConnection();
            statement = conn.prepareStatement("DELETE FROM credits WHERE id = ?");
            statement.setString(1, credit.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            DatabaseConnector.closeStatement(statement);
            DatabaseConnector.closeDatabase(conn);
        }
    }

    private static void _deleteCredit(String id) {
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = DatabaseConnector.getConnection();
            statement = conn.prepareStatement("DELETE FROM credits WHERE objectid = ?");
            statement.setString(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            DatabaseConnector.closeStatement(statement);
            DatabaseConnector.closeDatabase(conn);
        }
    }

    public static void deleteCredit(Unit unit) {
        _deleteCredit(unit.getId());
    }

    public static void deleteCredit(Building building) {
        _deleteCredit(building.getId());
    }
}
