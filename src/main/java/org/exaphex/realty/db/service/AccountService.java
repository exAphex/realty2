package org.exaphex.realty.db.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.exaphex.realty.db.DatabaseConnector;
import org.exaphex.realty.model.Account;

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
}
