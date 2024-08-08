package org.exaphex.realty.db.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.exaphex.realty.db.DatabaseConnector;
import org.exaphex.realty.model.CounterType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CounterTypeService {

    protected static final Logger logger = LogManager.getLogger();

    public static List<CounterType> getCounterTypes() {
        Connection conn = null;
        PreparedStatement statement = null;
        List<CounterType> retTypes = new ArrayList<>();
        try {
            conn = DatabaseConnector.getConnection();

            String query = "select * from countertypes";
            statement = conn.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                CounterType tmpCounterType = new CounterType(rs.getString("id"), rs.getString("name"));
                retTypes.add(tmpCounterType);
            }
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            DatabaseConnector.closeStatement(statement);
            DatabaseConnector.closeDatabase(conn);
        }
        return retTypes;
    }

    public static void addCounterTypes(List<CounterType> counterTypes) {
        for (CounterType ct : counterTypes) {
            addCounterType(ct);
        }
    }

    public static void addCounterType(CounterType counterTypes) {
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = DatabaseConnector.getConnection();
            statement = conn.prepareStatement("INSERT INTO countertypes (id, name) VALUES (?,?)");
            statement.setString(1, counterTypes.getId());
            statement.setString(2, counterTypes.getName());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            DatabaseConnector.closeStatement(statement);
            DatabaseConnector.closeDatabase(conn);
        }
    }

    public static void updateCounterType(CounterType counterTypes) {
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = DatabaseConnector.getConnection();
            statement = conn.prepareStatement("UPDATE countertypes SET name = ? where id = ?");
            statement.setString(1, counterTypes.getName());
            statement.setString(2, counterTypes.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            DatabaseConnector.closeStatement(statement);
            DatabaseConnector.closeDatabase(conn);
        }
    }

    public static void deleteCounterType(CounterType counterTypes) {
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = DatabaseConnector.getConnection();
            statement = conn.prepareStatement("DELETE FROM countertypes where id = ?");
            statement.setString(1, counterTypes.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            DatabaseConnector.closeStatement(statement);
            DatabaseConnector.closeDatabase(conn);
        }
    }
}
