package org.exaphex.realty.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.h2.jdbcx.JdbcConnectionPool;

public class DatabaseConnector {
    public static JdbcConnectionPool cp = JdbcConnectionPool.create(
            "jdbc:h2:./test", "sa", "sa");

    public static Connection getConnection() throws SQLException {
        return cp.getConnection();
    }

    public static void initializeDatabase(Connection conn) throws SQLException {
        String sqlVersionTable = "CREATE TABLE IF NOT EXISTS VERSION(ID INT PRIMARY KEY)";
        executeSQL(conn, sqlVersionTable);
    }

    public static void executeSQL(Connection conn, String sql) throws SQLException {
        Statement st = null;
        try {
            st = conn.createStatement();
            st.executeUpdate(sql);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    public static int getDatabaseVersion(Connection conn) throws SQLException {
        int retVersion = 0;
        Statement st = null;
        try {
            st = conn.createStatement();
            ResultSet rs = st.executeQuery("select * from VERSION order by ID desc");
            if (rs.next()) {
                retVersion = rs.getInt("ID");
            }
        } finally {
            if (st != null) {
                st.close();
            }
        }
        return retVersion;
    }

    public static void updateDatabase(Connection conn) throws SQLException {
        int databaseVersion = getDatabaseVersion(conn);
        switch (databaseVersion) {
            case 0:
                executeSQL(conn,
                        "CREATE TABLE IF NOT EXISTS buildings(id VARCHAR(255) PRIMARY KEY, name VARCHAR(255), street VARCHAR(255), number  VARCHAR(255), postalCode  VARCHAR(255), city  VARCHAR(255))");
                executeSQL(conn,
                        "CREATE TABLE IF NOT EXISTS accounts(id VARCHAR(255) PRIMARY KEY, name VARCHAR(255), bankname VARCHAR(255), iban  VARCHAR(255), bic  VARCHAR(255))");
                executeSQL(conn, "INSERT INTO VERSION VALUES (1)");
                updateDatabase(conn);
                break;
            case 1:
                executeSQL(conn,
                        "CREATE TABLE IF NOT EXISTS units(id VARCHAR(255) PRIMARY KEY, buildingid VARCHAR(255), name VARCHAR(255))");
                executeSQL(conn, "INSERT INTO VERSION VALUES (2)");
                updateDatabase(conn);
                break;
            case 2:
                executeSQL(conn,
                        "CREATE TABLE IF NOT EXISTS valuations(id VARCHAR(255), val DOUBLE, date VARCHAR(10))");
                executeSQL(conn, "INSERT INTO VERSION VALUES (3)");
                break;
            case 3:
                executeSQL(conn,
                        "CREATE TABLE IF NOT EXISTS rents(id VARCHAR(255), firstname VARCHAR(255), lastname VARCHAR(255), unitid VARCHAR(255), startdate VARCHAR(10), enddate VARCHAR(10), rentalprice DOUBLE, extracosts DOUBLE, deposit DOUBLE)");
                executeSQL(conn, "INSERT INTO VERSION VALUES (4)");
                break;
            case 4:
                executeSQL(conn,
                        "CREATE TABLE IF NOT EXISTS transactions(id VARCHAR(255), date VARCHAR(10), type INT, unitid VARCHAR(255), amount DOUBLE)");
                executeSQL(conn, "INSERT INTO VERSION VALUES (5)");
                break;
            case 5:
                executeSQL(conn,
                        "ALTER TABLE transactions ADD secondaryamount DOUBLE;");
                executeSQL(conn, "INSERT INTO VERSION VALUES (6)");
                break;

            default:
        }
    }

    public static void closeDatabase(Connection conn) {
        try {
            if (conn != null)
                conn.close();
        } catch (SQLException e) {
            // TODO: better logging
        }
    }

    public static void closeStatement(Statement statement) {
        try {
            if (statement != null)
                statement.close();
        } catch (SQLException e) {
            // TODO: better logging
        }
    }
}
