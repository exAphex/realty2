package org.exaphex.realty.db.service;

import org.exaphex.realty.db.DatabaseConnector;
import org.exaphex.realty.model.Building;
import org.exaphex.realty.model.Transaction;
import org.exaphex.realty.model.Unit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransactionService {

    public static List<Transaction> getTransactions(Unit u) {
        Connection conn = null;
        PreparedStatement statement = null;
        List<Transaction> retTransactions = new ArrayList<>();
        try {
            conn = DatabaseConnector.getConnection();
            statement = conn.prepareStatement("select * from transactions where unitid = ?");
            statement.setString(1, u.getId());
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Transaction tmpTransaction = new Transaction(rs.getString("id"), rs.getString("date"), rs.getInt("type"), rs.getString("unitid"), rs.getFloat("amount"), rs.getFloat("secondaryamount"));
                retTransactions.add(tmpTransaction);
            }
        } catch (SQLException e) {
            // TODO: proper logging
        } finally {
            DatabaseConnector.closeStatement(statement);
            DatabaseConnector.closeDatabase(conn);
        }
        return retTransactions;
    }

    public static void addTransaction(Transaction transaction) {
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = DatabaseConnector.getConnection();
            statement = conn.prepareStatement("INSERT INTO transactions VALUES (?,?,?,?,?,?)");
            statement.setString(1, transaction.getId());
            statement.setString(2, transaction.getDate());
            statement.setInt(3, transaction.getType());
            statement.setString(4, transaction.getUnitId());
            statement.setFloat(5, transaction.getAmount());
            statement.setFloat(6, transaction.getSecondaryAmount());
            statement.executeUpdate();
        } catch (SQLException e) {
            // TODO: proper logging
        } finally {
            DatabaseConnector.closeStatement(statement);
            DatabaseConnector.closeDatabase(conn);
        }
    }

    public static void updateTransaction(Transaction transaction) {
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = DatabaseConnector.getConnection();
            statement = conn.prepareStatement("UPDATE transactions SET date = ?, type = ?, unitid = ?, amount = ?, secondaryamount = ? where id = ?");
            statement.setString(1, transaction.getDate());
            statement.setInt(2, transaction.getType());
            statement.setString(3, transaction.getUnitId());
            statement.setFloat(4, transaction.getAmount());
            statement.setFloat(5, transaction.getSecondaryAmount());
            statement.setString(6, transaction.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            // TODO: proper logging
        } finally {
            DatabaseConnector.closeStatement(statement);
            DatabaseConnector.closeDatabase(conn);
        }
    }

    public static void deleteTransaction(Transaction transaction) {
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = DatabaseConnector.getConnection();
            statement = conn.prepareStatement("DELETE FROM transactions where id = ?");
            statement.setString(1, transaction.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            // TODO: proper logging
        } finally {
            DatabaseConnector.closeStatement(statement);
            DatabaseConnector.closeDatabase(conn);
        }
    }

    public static void deleteTransactions(Unit u) {
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = DatabaseConnector.getConnection();
            statement = conn.prepareStatement("DELETE FROM transactions where unitid = ?");
            statement.setString(1, u.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            // TODO: proper logging
        } finally {
            DatabaseConnector.closeStatement(statement);
            DatabaseConnector.closeDatabase(conn);
        }
    }
}
