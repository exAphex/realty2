package org.exaphex.realty.db.service;

import org.exaphex.realty.db.DatabaseConnector;
import org.exaphex.realty.model.Rent;
import org.exaphex.realty.model.Unit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RentService {

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
                Rent tmpRent = new Rent(rs.getString("id"), rs.getString("firstname"), rs.getString("lastname"), rs.getString("unitid"), rs.getString("startdate"), rs.getString("enddate"), rs.getFloat("rentalprice"), rs.getFloat("extracosts"),rs.getFloat("deposit"));
                retUnits.add(tmpRent);
            }
        } catch (SQLException e) {
            // TODO: proper logging
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
            statement = conn.prepareStatement("INSERT INTO rents VALUES (?,?,?,?,?,?,?,?,?)");
            statement.setString(1, rent.getId());
            statement.setString(2, rent.getFirstName());
            statement.setString(3, rent.getLastName());
            statement.setString(4, rent.getUnitId());
            statement.setString(5, rent.getStartDate());
            statement.setString(6, rent.getEndDate());
            statement.setFloat(7, rent.getRentalPrice());
            statement.setFloat(8, rent.getExtraCosts());
            statement.setFloat(9, rent.getDeposit());
            statement.executeUpdate();
        } catch (SQLException e) {
            // TODO: proper logging
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

    public static void deleteRent(Rent r) {
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = DatabaseConnector.getConnection();
            statement = conn.prepareStatement("DELETE FROM rents WHERE id = ?");
            statement.setString(1, r.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            // TODO: better logging
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
            // TODO: better logging
        } finally {
            DatabaseConnector.closeStatement(statement);
            DatabaseConnector.closeDatabase(conn);
        }
    }
}
