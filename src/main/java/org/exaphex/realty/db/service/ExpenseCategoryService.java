package org.exaphex.realty.db.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.exaphex.realty.db.DatabaseConnector;
import org.exaphex.realty.model.ExpenseCategory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExpenseCategoryService {
    protected static final Logger logger = LogManager.getLogger();

    public static List<ExpenseCategory> getCategories() {
        Connection conn = null;
        PreparedStatement statement = null;
        List<ExpenseCategory> retCategories = new ArrayList<>();
        try {
            conn = DatabaseConnector.getConnection();

            String query = "select * from expensecategory";
            statement = conn.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                ExpenseCategory tmpExpenseCategory = new ExpenseCategory(rs.getString("id"), rs.getString("name"), rs.getInt("wrapable") >= 1, rs.getInt("calculationtype"));
                retCategories.add(tmpExpenseCategory);
            }
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            DatabaseConnector.closeStatement(statement);
            DatabaseConnector.closeDatabase(conn);
        }
        return retCategories;
    }

    public static void addExpenseCategories(List<ExpenseCategory> categories) {
        for (ExpenseCategory c : categories) {
            addExpenseCategory(c);
        }
    }

    public static void addExpenseCategory(ExpenseCategory category) {
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = DatabaseConnector.getConnection();
            statement = conn.prepareStatement("INSERT INTO expensecategory (id, name, wrapable, calculationtype) VALUES (?,?,?,?)");
            statement.setString(1, category.getId());
            statement.setString(2, category.getName());
            statement.setInt(3, (category.isWrapable() ? 1 : 0));
            statement.setInt(4, category.getCalculationType());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            DatabaseConnector.closeStatement(statement);
            DatabaseConnector.closeDatabase(conn);
        }
    }

    public static void updateExpenseCategory(ExpenseCategory category) {
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = DatabaseConnector.getConnection();
            statement = conn.prepareStatement("UPDATE expensecategory SET name = ?, wrapable = ?, calculationtype = ? where id = ?");
            statement.setString(1, category.getName());
            statement.setInt(2, (category.isWrapable() ? 1 : 0));
            statement.setInt(3, category.getCalculationType());
            statement.setString(4, category.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            DatabaseConnector.closeStatement(statement);
            DatabaseConnector.closeDatabase(conn);
        }
    }

    public static void deleteCategory(ExpenseCategory category) {
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = DatabaseConnector.getConnection();
            statement = conn.prepareStatement("DELETE FROM expensecategory where id = ?");
            statement.setString(1, category.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            DatabaseConnector.closeStatement(statement);
            DatabaseConnector.closeDatabase(conn);
        }
    }
}
