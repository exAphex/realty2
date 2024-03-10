package org.exaphex.realty.db.service;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.exaphex.realty.db.DatabaseConnector;
import org.exaphex.realty.model.Building;
import org.exaphex.realty.model.Unit;

public class BuildingService {
    protected static final Logger logger = LogManager.getLogger();

    public static List<Building> getBuilding(Building building) {
        Connection conn = null;
        PreparedStatement statement = null;
        List<Building> retBuildings = new ArrayList<>();
        try {
            conn = DatabaseConnector.getConnection();
            String query = "select * from buildings";
            if (building != null) {
                query += " where id = ?";
                statement = conn.prepareStatement(query);
                statement.setString(1, building.getId());
            } else {
                statement = conn.prepareStatement(query);
            }
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Building tmpBuilding = new Building(rs.getString("id"), rs.getString("name"), rs.getString("street"),
                        rs.getString("number"), rs.getString("postalCode"), rs.getString("city"), rs.getFloat("totalarea"), rs.getFloat("totalshares"));
                retBuildings.add(tmpBuilding);
            }
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            DatabaseConnector.closeStatement(statement);
            DatabaseConnector.closeDatabase(conn);
        }
        return retBuildings;
    }

    public static void addBuilding(Building building) {
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = DatabaseConnector.getConnection();
            statement = conn.prepareStatement("INSERT INTO buildings (id, name, street, number, postalCode, city, totalarea, totalshares) VALUES (?,?,?,?,?,?,?,?)");
            statement.setString(1, building.getId());
            statement.setString(2, building.getName());
            statement.setString(3, building.getAddress());
            statement.setString(4, building.getNumber());
            statement.setString(5, building.getPostalCode());
            statement.setString(6, building.getCity());
            statement.setFloat(7, building.getTotalArea());
            statement.setFloat(8, building.getTotalShares());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            DatabaseConnector.closeStatement(statement);
            DatabaseConnector.closeDatabase(conn);
        }
    }

    public static void addBuildings(List<Building> buildings) {
        for (Building b : buildings) {
            addBuilding(b);
        }
    }

    public static void updateBuilding(Building building) {
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = DatabaseConnector.getConnection();
            statement = conn.prepareStatement("UPDATE buildings SET name = ?, street = ?, number = ?, postalCode = ?, city = ?, totalarea = ?, totalshares = ? where id = ?");
            statement.setString(1, building.getName());
            statement.setString(2, building.getAddress());
            statement.setString(3, building.getNumber());
            statement.setString(4, building.getPostalCode());
            statement.setString(5, building.getCity());
            statement.setFloat(6, building.getTotalArea());
            statement.setFloat(7, building.getTotalShares());
            statement.setString(8, building.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            DatabaseConnector.closeStatement(statement);
            DatabaseConnector.closeDatabase(conn);
        }
    }

    private static void deleteBuildingInternal(Building building) {
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = DatabaseConnector.getConnection();
            statement = conn.prepareStatement("DELETE FROM buildings WHERE id = ?");
            statement.setString(1, building.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            DatabaseConnector.closeStatement(statement);
            DatabaseConnector.closeDatabase(conn);
        }
    }


    public static void deleteBuilding(Building building) {
        for (Unit u : UnitService.getUnits(building)) {
            UnitService.deleteUnit(u);
        }
        deleteBuildingInternal(building);
    }
}