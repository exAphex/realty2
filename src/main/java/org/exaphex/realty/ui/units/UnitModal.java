package org.exaphex.realty.ui.units;

import org.exaphex.realty.model.Building;
import org.exaphex.realty.model.Unit;

import javax.swing.*;
import java.util.ResourceBundle;

public class UnitModal {
    private final ResourceBundle res = ResourceBundle.getBundle("i18n");
    private final UnitWindow uw;
    private final Building building;
    private JDialog dialog;
    private JTextField txtName;
    private JButton btnSave;
    private JPanel mainPanel;

    public UnitModal(UnitWindow uw, Building b) {
        this.uw = uw;
        this.building = b;
        setupUI();
        setupListeners();
    }

    private void setupUI() {
        this.dialog = new JDialog();
        dialog.setTitle(res.getString("titleAddUnit"));
        dialog.add(mainPanel);
        dialog.pack();
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    private void setupListeners() {
        btnSave.addActionListener(
                e -> {
                    uw.eventAddNewUnit(new Unit(this.building.getId(), txtName.getText()));
                    dialog.dispose();
                });
    }
}
