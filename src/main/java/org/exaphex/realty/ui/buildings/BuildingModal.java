package org.exaphex.realty.ui.buildings;

import org.exaphex.realty.model.Building;
import org.exaphex.realty.ui.MainWindow;

import javax.swing.*;
import java.util.ResourceBundle;

import static org.exaphex.realty.util.PriceUtils.validatePrice;

public class BuildingModal {
    private final ResourceBundle res = ResourceBundle.getBundle("i18n");
    private JPanel mainPanel;
    private JTextField textName;
    private JTextField textStreet;
    private JTextField textNumber;
    private JTextField textPostalCode;
    private JTextField textCity;
    private JButton saveButton;
    private JTextField txtTotalArea;

    private final MainWindow mw;

    private JDialog dialog;

    public BuildingModal(MainWindow mw) {
        this.mw = mw;
        setupUI();
        setupListeners();
    }

    private void setupUI() {
        txtTotalArea.setText("1.0");
        this.dialog = new JDialog();
        dialog.setTitle(res.getString("titleAddBuilding"));
        dialog.add(mainPanel);
        dialog.pack();
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    private void setupListeners() {
        saveButton.addActionListener(
                e -> {
                    Float fArea = validatePrice(txtTotalArea.getText(), res.getString("msgArea"));
                    if (fArea == null || fArea <= 0) {
                        return;
                    }

                    mw.eventAddNewBuilding(new Building(textName.getText(), textStreet.getText(), textNumber.getText(),
                            textPostalCode.getText(), textCity.getText(),fArea));
                    dialog.dispose();
                });
    }
}
