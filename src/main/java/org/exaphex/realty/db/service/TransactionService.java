package org.exaphex.realty.db.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.exaphex.realty.db.DatabaseConnector;
import org.exaphex.realty.model.Account;
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
    protected static final Logger logger = LogManager.getLogger();

    private static List<Transaction> _getTransactions(String id) {
        Connection conn = null;
        PreparedStatement statement = null;
        List<Transaction> retTransactions = new ArrayList<>();
        try {
            conn = DatabaseConnector.getConnection();

            String query = "select * from transactions";
            if (id != null) {
                statement = conn.prepareStatement(query + " where objectid = ?");
                statement.setString(1, id);
            } else {
                statement = conn.prepareStatement(query);
            }

            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Transaction tmpTransaction = new Transaction(rs.getString("id"), rs.getString("description"), rs.getString("reference"), rs.getString("date"), rs.getInt("type"), rs.getString("objectid"), rs.getFloat("amount"), rs.getFloat("secondaryamount"), rs.getString("expensecategory"), rs.getString("accountid"));
                retTransactions.add(tmpTransaction);
            }
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            DatabaseConnector.closeStatement(statement);
            DatabaseConnector.closeDatabase(conn);
        }
        return retTransactions;
    }

    public static List<Transaction> getTransactions() {
        return _getTransactions(null);
    }

    public static List<Transaction> getTransactions(Unit unit) {
        return _getTransactions(unit.getId());
    }

    public static List<Transaction> getTransactions(Building building) {
        return _getTransactions(building.getId());
    }

    public static List<Transaction> getTransactionsByAccount(Account account) {
        Connection conn = null;
        PreparedStatement statement = null;
        List<Transaction> retTransactions = new ArrayList<>();
        try {
            conn = DatabaseConnector.getConnection();

            statement = conn.prepareStatement("select * from transactions where accountid = ?");
            statement.setString(1, account.getId());

            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Transaction tmpTransaction = new Transaction(rs.getString("id"), rs.getString("description"), rs.getString("reference"), rs.getString("date"), rs.getInt("type"), rs.getString("objectid"), rs.getFloat("amount"), rs.getFloat("secondaryamount"), rs.getString("expensecategory"), rs.getString("accountid"));
                retTransactions.add(tmpTransaction);
            }
        } catch (SQLException e) {
            logger.error(e);
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
            statement = conn.prepareStatement("INSERT INTO transactions (id, date, type, objectid, amount, secondaryamount, description, reference, expensecategory, accountid) VALUES (?,?,?,?,?,?,?,?,?,?)");
            statement.setString(1, transaction.getId());
            statement.setString(2, transaction.getDate());
            statement.setInt(3, transaction.getType());
            statement.setString(4, transaction.getObjectId());
            statement.setFloat(5, transaction.getAmount());
            statement.setFloat(6, transaction.getSecondaryAmount());
            statement.setString(7, transaction.getDescription());
            statement.setString(8, transaction.getReference());
            statement.setString(9, transaction.getExpenseCategory());
            statement.setString(10, transaction.getAccountId());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            DatabaseConnector.closeStatement(statement);
            DatabaseConnector.closeDatabase(conn);
        }
    }

    public static void addTransactions(List<Transaction> transactions) {
        for (Transaction t : transactions) {
            addTransaction(t);
        }
    }

    public static void updateTransaction(Transaction transaction) {
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = DatabaseConnector.getConnection();
            statement = conn.prepareStatement("UPDATE transactions SET date = ?, type = ?, objectid = ?, amount = ?, secondaryamount = ?, description = ?, reference = ?, expensecategory = ?, accountid = ? where id = ?");
            statement.setString(1, transaction.getDate());
            statement.setInt(2, transaction.getType());
            statement.setString(3, transaction.getObjectId());
            statement.setFloat(4, transaction.getAmount());
            statement.setFloat(5, transaction.getSecondaryAmount());
            statement.setString(6, transaction.getDescription());
            statement.setString(7, transaction.getReference());
            statement.setString(8, transaction.getExpenseCategory());
            statement.setString(9, transaction.getAccountId());
            statement.setString(10, transaction.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
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
            logger.error(e);
        } finally {
            DatabaseConnector.closeStatement(statement);
            DatabaseConnector.closeDatabase(conn);
        }
    }

    private static void _deleteTransactions(String id) {
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = DatabaseConnector.getConnection();
            statement = conn.prepareStatement("DELETE FROM transactions where objectid = ?");
            statement.setString(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            DatabaseConnector.closeStatement(statement);
            DatabaseConnector.closeDatabase(conn);
        }
    }

    public static void deleteTransactions(Unit unit) {
        _deleteTransactions(unit.getId());
    }

    public static void deleteTransactions(Building building) {
        _deleteTransactions(building.getId());
    }
}
