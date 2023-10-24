package org.exaphex.realty.ui.buildings;

import org.exaphex.realty.model.Building;
import org.exaphex.realty.ui.MainWindow;

import javax.swing.*;

public class BuildingModal {
    private JPanel mainPanel;
    private JTextField textName;
    private JTextField textStreet;
    private JTextField textNumber;
    private JTextField textPostalCode;
    private JTextField textCity;
    private JButton saveButton;

    private MainWindow mw;

    private JDialog dialog;

    public BuildingModal(MainWindow mw) {
        this.mw = mw;
        setupUI();
        setupListeners();
    }

    private void setupUI() {
        this. dialog = new JDialog();
        dialog.setTitle("Add Building");
        dialog.add(mainPanel);
        dialog.pack();
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    private void setupListeners() {
        saveButton.addActionListener(
                e -> {
                    mw.eventAddNewBuilding(new Building(textName.getText(), textStreet.getText(), textNumber.getText(),
                            textPostalCode.getText(), textCity.getText()));
                    dialog.dispose();
                });
    }
}
