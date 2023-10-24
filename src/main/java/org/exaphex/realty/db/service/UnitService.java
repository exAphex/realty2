package org.exaphex.realty.db.service;

import org.exaphex.realty.db.DatabaseConnector;
import org.exaphex.realty.model.Building;
import org.exaphex.realty.model.Unit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UnitService {

    public static List<Unit> getUnits(Building b) {
        Connection conn = null;
        PreparedStatement statement = null;
        List<Unit> retUnits = new ArrayList<>();
        try {
            conn = DatabaseConnector.getConnection();
            statement = conn.prepareStatement("select * from units where buildingid = ?");
            statement.setString(1, b.getId());
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Unit tmpUnit = new Unit(rs.getString("id"), rs.getString("buildingid"), rs.getString("name"));
                retUnits.add(tmpUnit);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnector.closeStatement(statement);
            DatabaseConnector.closeDatabase(conn);
        }
        return retUnits;
    }

    public static Unit addUnit(Unit unit) {
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = DatabaseConnector.getConnection();
            statement = conn.prepareStatement("INSERT INTO units VALUES (?,?,?)");
            statement.setString(1, unit.getId());
            statement.setString(2, unit.getBuildingId());
            statement.setString(3, unit.getName());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnector.closeStatement(statement);
            DatabaseConnector.closeDatabase(conn);
        }
        return unit;
    }
}