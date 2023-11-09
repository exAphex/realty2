package org.exaphex.realty.ui.units;

import org.exaphex.realty.model.Rent;
import org.exaphex.realty.model.Unit;

import javax.swing.*;
import javax.swing.text.DateFormatter;
import javax.swing.text.DefaultFormatterFactory;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CreditModal {
    private JDialog dialog;
    private Unit unit;
    private UnitWindow uw;
    private JTextField textField1;
    private JTextField textField2;
    private JFormattedTextField txtStartDate;
    private JTextField txtInterestRate;
    private JTextField txtRedemptionRate;
    private JButton btnSave;
    private JPanel mainPanel;

    public CreditModal(UnitWindow uw, Unit u) {
        this.unit = u;
        this.uw = uw;
        setupUI();
        setupListeners();
    }

    private void setupUI() {
        DateFormatter df = new DateFormatter(new SimpleDateFormat("dd-MM-yyyy"));
        DefaultFormatterFactory dff = new DefaultFormatterFactory(df, df, df, df);
        txtStartDate.setFormatterFactory(dff);

        SimpleDateFormat DateFor = new SimpleDateFormat("dd-MM-yyyy");
        txtStartDate.setText(DateFor.format(new Date()));

        txtInterestRate.setText("0.00");
        txtRedemptionRate.setText("0.00");

        this.dialog = new JDialog();
        dialog.setTitle("Add Rent");
        dialog.add(mainPanel);
        dialog.pack();
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    private void setupListeners() {
        btnSave.addActionListener(
                e -> {
                    //uw.eventAddNewRent(new Rent(txtFirstName.getText(), txtLastName.getText(), this.unit.getId(), txtStartDate.getText(), txtEndDate.getText(), fRentalPrice, fExtraCosts, fDeposit));
                    dialog.dispose();
                });
    }
}
