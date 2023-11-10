package org.exaphex.realty.db.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.exaphex.realty.db.DatabaseConnector;
import org.exaphex.realty.model.Unit;
import org.exaphex.realty.model.Valuation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ValuationService {
    protected static final Logger logger = LogManager.getLogger();
    public static List<Valuation> getValuations(Unit u) {
        Connection conn = null;
        PreparedStatement statement = null;
        List<Valuation> retValuations = new ArrayList<>();
        try {
            conn = DatabaseConnector.getConnection();
            statement = conn.prepareStatement("select * from valuations where id = ?");
            statement.setString(1, u.getId());
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Valuation tmpValuation = new Valuation(rs.getString("id"), rs.getString("date"), rs.getFloat("val"));
                retValuations.add(tmpValuation);
            }
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            DatabaseConnector.closeStatement(statement);
            DatabaseConnector.closeDatabase(conn);
        }
        return retValuations;
    }

    public static List<Valuation> getValuationByDate(Valuation v) {
        Connection conn = null;
        PreparedStatement statement = null;
        List<Valuation> retValuations = new ArrayList<>();
        try {
            conn = DatabaseConnector.getConnection();
            statement = conn.prepareStatement("select * from valuations where id = ? and date = ?");
            statement.setString(1, v.getId());
            statement.setString(2, v.getDate());
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Valuation tmpValuation = new Valuation(rs.getString("id"), rs.getString("date"), rs.getFloat("val"));
                retValuations.add(tmpValuation);
            }
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            DatabaseConnector.closeStatement(statement);
            DatabaseConnector.closeDatabase(conn);
        }
        return retValuations;
    }

    public static void addValuation(Valuation v) {
        Connection conn = null;
        PreparedStatement statement = null;

        if (!getValuationByDate(v).isEmpty()) {
            return;
        }

        try {
            conn = DatabaseConnector.getConnection();
            statement = conn.prepareStatement("INSERT INTO valuations VALUES (?,?,?)");
            statement.setString(1, v.getId());
            statement.setFloat(2, v.getValue());
            statement.setString(3,v.getDate());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            DatabaseConnector.closeStatement(statement);
            DatabaseConnector.closeDatabase(conn);
        }
    }

    public static void deleteValuation(Valuation valuation) {
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = DatabaseConnector.getConnection();
            statement = conn.prepareStatement("DELETE FROM valuations WHERE id = ? and date = ?");
            statement.setString(1, valuation.getId());
            statement.setString(2, valuation.getDate());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            DatabaseConnector.closeStatement(statement);
            DatabaseConnector.closeDatabase(conn);
        }
    }

    public static void deleteValuation(Unit unit) {
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = DatabaseConnector.getConnection();
            statement = conn.prepareStatement("DELETE FROM valuations WHERE id = ?");
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
