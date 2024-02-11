package org.exaphex.realty.db.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.exaphex.realty.db.DatabaseConnector;
import org.exaphex.realty.model.Contact;
import org.exaphex.realty.model.Rent;
import org.exaphex.realty.model.Unit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RentService {
    protected static final Logger logger = LogManager.getLogger();
    public static List<Rent> getRents(Unit u) {
        Connection conn = null;
        PreparedStatement statement = null;
        List<Rent> retUnits = new ArrayList<>();
        try {
            conn = DatabaseConnector.getConnection();

            String query = "select * from rents";
            if (u != null) {
                statement = conn.prepareStatement(query + " where unitid = ?");
                statement.setString(1, u.getId());
            } else {
                statement = conn.prepareStatement(query);
            }

            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Rent tmpRent = new Rent(rs.getString("id"), rs.getString("contactid"), rs.getString("unitid"), rs.getString("startdate"), rs.getString("enddate"), rs.getFloat("rentalprice"), rs.getFloat("extracosts"),rs.getFloat("deposit"), rs.getInt("numoftentants"), true);
                retUnits.add(tmpRent);
            }
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            DatabaseConnector.closeStatement(statement);
            DatabaseConnector.closeDatabase(conn);
        }
        return retUnits;
    }

    public static List<Rent> getRentsByContact(Contact c) {
        Connection conn = null;
        PreparedStatement statement = null;
        List<Rent> retUnits = new ArrayList<>();
        try {
            conn = DatabaseConnector.getConnection();
            statement = conn.prepareStatement("select * from rents where contactid = ?");
            statement.setString(1, c.getId());

            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Rent tmpRent = new Rent(rs.getString("id"), rs.getString("contactid"), rs.getString("unitid"), rs.getString("startdate"), rs.getString("enddate"), rs.getFloat("rentalprice"), rs.getFloat("extracosts"),rs.getFloat("deposit"), rs.getInt("numoftentants"), false);
                retUnits.add(tmpRent);
            }
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            DatabaseConnector.closeStatement(statement);
            DatabaseConnector.closeDatabase(conn);
        }
        return retUnits;
    }

    public static void addRent(Rent rent) {
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = DatabaseConnector.getConnection();
            statement = conn.prepareStatement("INSERT INTO rents (id, contactid, unitid, startdate, enddate, rentalprice, extracosts, deposit, numoftentants) VALUES (?,?,?,?,?,?,?,?,?)");
            statement.setString(1, rent.getId());
            statement.setString(2, rent.getContactId());
            statement.setString(3, rent.getUnitId());
            statement.setString(4, rent.getStartDate());
            statement.setString(5, rent.getEndDate());
            statement.setFloat(6, rent.getRentalPrice());
            statement.setFloat(7, rent.getExtraCosts());
            statement.setFloat(8, rent.getDeposit());
            statement.setInt(9, rent.getNumOfTentants());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            DatabaseConnector.closeStatement(statement);
            DatabaseConnector.closeDatabase(conn);
        }
    }

    public static void addRents(List<Rent> rents) {
        for (Rent r : rents) {
            addRent(r);
        }
    }

    public static void updateRent(Rent r) {
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = DatabaseConnector.getConnection();
            statement = conn.prepareStatement("UPDATE rents SET contactid = ?, unitid = ?, startdate = ?, enddate = ?, rentalprice = ?, extracosts = ?, deposit = ?, numoftentants = ? where id = ?");
            statement.setString(1, r.getContactId());
            statement.setString(2, r.getUnitId());
            statement.setString(3, r.getStartDate());
            statement.setString(4, r.getEndDate());
            statement.setFloat(5, r.getRentalPrice());
            statement.setFloat(6, r.getExtraCosts());
            statement.setFloat(7, r.getDeposit());
            statement.setInt(8, r.getNumOfTentants());
            statement.setString(9, r.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            DatabaseConnector.closeStatement(statement);
            DatabaseConnector.closeDatabase(conn);
        }
    }

    public static void deleteRent(Rent r) {
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = DatabaseConnector.getConnection();
            statement = conn.prepareStatement("DELETE FROM rents WHERE id = ?");
            statement.setString(1, r.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            DatabaseConnector.closeStatement(statement);
            DatabaseConnector.closeDatabase(conn);
        }
    }

    public static void deleteRent(Unit unit) {
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = DatabaseConnector.getConnection();
            statement = conn.prepareStatement("DELETE FROM rents WHERE unitid = ?");
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
