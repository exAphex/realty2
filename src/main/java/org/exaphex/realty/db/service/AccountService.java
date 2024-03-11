package org.exaphex.realty.db.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.exaphex.realty.db.DatabaseConnector;
import org.exaphex.realty.model.Account;
import org.exaphex.realty.model.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccountService {
    protected static final Logger logger = LogManager.getLogger();

    public static List<Account> getAccount(Account account) {
        Connection conn = null;
        PreparedStatement statement = null;
        List<Account> retAccounts = new ArrayList<>();
        try {
            conn = DatabaseConnector.getConnection();
            String query = "select * from accounts";
            if (account != null) {
                query += " where id = ?";
                statement = conn.prepareStatement(query);
                statement.setString(1, account.getId());
            } else {
                statement = conn.prepareStatement(query);
            }
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Account tmpAccount = new Account(rs.getString("id"), rs.getString("name"), rs.getString("iban"),
                        rs.getString("bic"));
                retAccounts.add(tmpAccount);
            }
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            DatabaseConnector.closeStatement(statement);
            DatabaseConnector.closeDatabase(conn);
        }
        return retAccounts;
    }

    public static List<Account> getAccounts() {
        return getAccount(null);
    }

    public static void addAccount(Account account) {
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = DatabaseConnector.getConnection();
            statement = conn.prepareStatement("INSERT INTO accounts (id, name, iban, bic) VALUES (?,?,?,?)");
            statement.setString(1, account.getId());
            statement.setString(2, account.getName());
            statement.setString(3, account.getIban());
            statement.setString(4, account.getBic());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            DatabaseConnector.closeStatement(statement);
            DatabaseConnector.closeDatabase(conn);
        }
    }

    public static void addAccounts(List<Account> accounts) {
        for (Account a : accounts) {
            addAccount(a);
        }
    }

    public static void updateAccount(Account account) {
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = DatabaseConnector.getConnection();
            statement = conn.prepareStatement("UPDATE accounts SET name = ?, iban = ?, bic = ? where id = ?");
            statement.setString(1, account.getName());
            statement.setString(2, account.getIban());
            statement.setString(3, account.getBic());
            statement.setString(4, account.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            DatabaseConnector.closeStatement(statement);
            DatabaseConnector.closeDatabase(conn);
        }
    }

    private static void _deleteAccount(Account account) {
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = DatabaseConnector.getConnection();
            statement = conn.prepareStatement("DELETE FROM accounts WHERE id = ?");
            statement.setString(1, account.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            DatabaseConnector.closeStatement(statement);
            DatabaseConnector.closeDatabase(conn);
        }
    }

    public static void deleteAccount(Account account) throws Exception {
        List<Transaction> foundTransactions = TransactionService.getTransactionsByAccount(account);
        if (foundTransactions.isEmpty()) {
            _deleteAccount(account);
        } else {
            throw new Exception("DeleteAccountFirstException");
        }
    }
}
