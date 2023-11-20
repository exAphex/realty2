package org.exaphex.realty.db.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.exaphex.realty.db.DatabaseConnector;
import org.exaphex.realty.model.Building;
import org.exaphex.realty.model.Unit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UnitService {
    protected static final Logger logger = LogManager.getLogger();

    public static List<Unit> getUnits(Building b) {
        Connection conn = null;
        PreparedStatement statement = null;
        List<Unit> retUnits = new ArrayList<>();
        try {
            conn = DatabaseConnector.getConnection();
            String query = "select * from units";
            if (b != null) {
                statement = conn.prepareStatement(query + " where buildingid = ?");
                statement.setString(1, b.getId());
            } else {
                statement = conn.prepareStatement(query);
            }

            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Unit tmpUnit = new Unit(rs.getString("id"), rs.getString("buildingid"), rs.getString("name"), rs.getFloat("area"));
                retUnits.add(tmpUnit);
            }
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            DatabaseConnector.closeStatement(statement);
            DatabaseConnector.closeDatabase(conn);
        }
        return retUnits;
    }

    public static void addUnit(Unit unit) {
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = DatabaseConnector.getConnection();
            statement = conn.prepareStatement("INSERT INTO units (id, buildingid, name, area) VALUES (?,?,?,?)");
            statement.setString(1, unit.getId());
            statement.setString(2, unit.getBuildingId());
            statement.setString(3, unit.getName());
            statement.setFloat(4, unit.getArea());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            DatabaseConnector.closeStatement(statement);
            DatabaseConnector.closeDatabase(conn);
        }
    }

    public static void addUnits(List<Unit> units) {
        for (Unit u : units) {
            addUnit(u);
        }
    }

    public static void deleteUnit(Unit unit) {
        RentService.deleteRent(unit);
        ValuationService.deleteValuation(unit);
        TransactionService.deleteTransactions(unit);
        CreditService.deleteCredit(unit);
        deleteUnitInternal(unit);
    }

    private static void deleteUnitInternal(Unit unit) {
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = DatabaseConnector.getConnection();
            statement = conn.prepareStatement("DELETE FROM units WHERE id = ?");
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