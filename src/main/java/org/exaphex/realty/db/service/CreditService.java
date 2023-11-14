package org.exaphex.realty.db.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.exaphex.realty.db.DatabaseConnector;
import org.exaphex.realty.model.Credit;
import org.exaphex.realty.model.Unit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CreditService {
    protected static final Logger logger = LogManager.getLogger();
    public static List<Credit> getCredit(Unit u) {
        Connection conn = null;
        PreparedStatement statement = null;
        List<Credit> retCredits = new ArrayList<>();
        try {
            conn = DatabaseConnector.getConnection();
            statement = conn.prepareStatement("select * from credits where unitid = ?");
            statement.setString(1, u.getId());
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Credit tmpCredit = new Credit(rs.getString("id"), rs.getString("unitid"), rs.getString("name"), rs.getString("description"), rs.getFloat("interestrate"), rs.getFloat("redemptionrate"), rs.getString("startdate"), rs.getString("enddate"), rs.getFloat("amount"));
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

    public static void addCredit(Credit credit) {
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = DatabaseConnector.getConnection();
            statement = conn.prepareStatement("INSERT INTO credits (id, unitid, name, description, interestrate, redemptionrate, startdate, enddate, amount) VALUES (?,?,?,?,?,?,?,?,?)");
            statement.setString(1, credit.getId());
            statement.setString(2, credit.getUnitId());
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

    public static void deleteCredit(Unit unit) {
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = DatabaseConnector.getConnection();
            statement = conn.prepareStatement("DELETE FROM credits WHERE unitid = ?");
            statement.setString(1, unit.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            DatabaseConnector.closeStatement(statement);
            DatabaseConnector.closeDatabase(conn);
        }
    }
}
