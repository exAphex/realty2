package org.exaphex.realty.ui.units;

import org.exaphex.realty.model.Building;
import org.exaphex.realty.model.Unit;

import javax.swing.*;
import java.util.ResourceBundle;

import static org.exaphex.realty.util.PriceUtils.validatePrice;

public class UnitModal {
    private final ResourceBundle res = ResourceBundle.getBundle("i18n");
    private final UnitWindow uw;
    private final Building building;
    private JDialog dialog;
    private JTextField txtName;
    private JButton btnSave;
    private JPanel mainPanel;
    private JTextField txtArea;
    private JTextField txtShares;

    public UnitModal(UnitWindow uw, Building b) {
        this.uw = uw;
        this.building = b;
        setupUI();
        setupListeners();
    }

    private void setupUI() {
        txtArea.setText("1.00");
        txtShares.setText("1");
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
                    Float fArea = validatePrice(txtArea.getText(), res.getString("msgArea"));
                    if (fArea == null || fArea <= 1) {
                        return;
                    }

                    Float fShares = validatePrice(txtShares.getText(), res.getString("msgShares"));
                    if (fShares == null || fShares <= 1) {
                        return;
                    }

                    uw.eventAddNewUnit(new Unit(this.building.getId(), txtName.getText(), fArea, fShares));
                    dialog.dispose();
                });
    }
}
