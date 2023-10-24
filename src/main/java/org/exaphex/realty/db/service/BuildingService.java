package org.exaphex.realty.db.service;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.exaphex.realty.db.DatabaseConnector;
import org.exaphex.realty.model.Building;

public class BuildingService {

    public static List<Building> getAllBuildings() {
        Connection conn = null;
        Statement statement = null;
        List<Building> retBuildings = new ArrayList<>();
        try {
            conn = DatabaseConnector.getConnection();
            statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("select * from buildings");
            while (rs.next()) {
                Building tmpBuilding = new Building(rs.getString("id"), rs.getString("name"), rs.getString("street"),
                        rs.getString("number"), rs.getString("postalCode"), rs.getString("city"));
                retBuildings.add(tmpBuilding);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnector.closeStatement(statement);
            DatabaseConnector.closeDatabase(conn);
        }
        return retBuildings;
    }

    public static Building addBuilding(Building building) {
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = DatabaseConnector.getConnection();
            statement = conn.prepareStatement("INSERT INTO buildings VALUES (?,?,?,?,?,?)");
            statement.setString(1, building.getId());
            statement.setString(2, building.getName());
            statement.setString(3, building.getAddress());
            statement.setString(4, building.getNumber());
            statement.setString(5, building.getPostalCode());
            statement.setString(6, building.getCity());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnector.closeStatement(statement);
            DatabaseConnector.closeDatabase(conn);
        }
        return building;
    }

    public static void deleteBuilding(Building building) {
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = DatabaseConnector.getConnection();
            statement = conn.prepareStatement("DELETE FROM buildings WHERE id = ?");
            statement.setString(1, building.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnector.closeStatement(statement);
            DatabaseConnector.closeDatabase(conn);
        }
    }
}