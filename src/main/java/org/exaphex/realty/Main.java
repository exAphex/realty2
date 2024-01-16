package org.exaphex.realty;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import org.exaphex.realty.db.DatabaseConnector;
import org.exaphex.realty.ui.MainWindow;

import javax.swing.*;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {

    public static void setupDatabase() throws SQLException {
        try (Connection conn = DatabaseConnector.getConnection()) {
            DatabaseConnector.initializeDatabase(conn);
            DatabaseConnector.updateDatabase(conn);
        }
    }

    public static void main(String[] args) throws SQLException {
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setProperty("apple.awt.application.appearance", "system");
        System.setProperty("apple.awt.application.name", "Realty");
        FlatMacDarkLaf.setup();
        setupDatabase();

        JFrame frame = new MainWindow();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}