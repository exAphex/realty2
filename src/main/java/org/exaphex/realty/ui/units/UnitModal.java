package org.exaphex.realty.ui.units;

import org.exaphex.realty.model.Unit;

import javax.swing.*;

public class UnitModal {

    private UnitWindow uw;
    private JDialog dialog;
    private JTextField txtName;
    private JButton btnSave;
    private JPanel mainPanel;

    public UnitModal(UnitWindow uw) {
        this.uw = uw;
        setupUI();
        setupListeners();
    }

    private void setupUI() {
        this.dialog = new JDialog();
        dialog.setTitle("Add Unit");
        dialog.add(mainPanel);
        dialog.pack();
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    private void setupListeners() {
        btnSave.addActionListener(
                e -> {
                    uw.eventAddNewUnit(new Unit(txtName.getText()));
                    dialog.dispose();
                });
    }
}
