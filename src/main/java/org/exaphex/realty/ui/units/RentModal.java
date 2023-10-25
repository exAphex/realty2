package org.exaphex.realty.ui.units;

import org.exaphex.realty.model.Rent;
import org.exaphex.realty.model.Unit;

import javax.swing.*;
import javax.swing.text.DateFormatter;
import javax.swing.text.DefaultFormatterFactory;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RentModal {
    private final Unit unit;
    private final UnitWindow uw;
    private JDialog dialog;
    private JTextField txtFirstName;
    private JTextField txtLastName;
    private JFormattedTextField txtStartDate;
    private JTextField txtRentalPrice;
    private JTextField txtExtraCost;
    private JTextField txtDeposit;
    private JButton btnSave;
    private JPanel mainPanel;
    private JFormattedTextField txtEndDate;

    public RentModal(UnitWindow uw, Unit u) {
        this.unit = u;
        this.uw = uw;
        setupUI();
        setupListeners();
    }

    private void setupUI() {
        DateFormatter df = new DateFormatter(new SimpleDateFormat("dd-MM-yyyy"));
        DefaultFormatterFactory dff = new DefaultFormatterFactory(df, df, df, df);
        txtStartDate.setFormatterFactory(dff);
        txtEndDate.setFormatterFactory(dff);

        SimpleDateFormat DateFor = new SimpleDateFormat("dd-MM-yyyy");
        txtStartDate.setText(DateFor.format(new Date()));
        txtEndDate.setText("30-12-9999");

        txtRentalPrice.setText("0.00");
        txtExtraCost.setText("0.00");
        txtDeposit.setText("0.00");


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
                    if (txtFirstName.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(new JFrame(), "Firstname is not valid!", "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (txtLastName.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(new JFrame(), "Lastname is not valid!", "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (txtStartDate.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(new JFrame(), "Startdate is not valid!", "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (txtEndDate.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(new JFrame(), "Enddate is not valid!", "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    Float fRentalPrice = validatePrice(txtRentalPrice.getText(), "Rental price");
                    Float fExtraCosts = validatePrice(txtExtraCost.getText(), "Extra costs");
                    Float fDeposit = validatePrice(txtDeposit.getText(), "Deposit");
                    if (fRentalPrice == null || fExtraCosts == null || fDeposit == null) {
                        return;
                    }

                    uw.eventAddNewRent(new Rent(txtFirstName.getText(), txtLastName.getText(), this.unit.getId(), txtStartDate.getText(), txtEndDate.getText(), fRentalPrice, fExtraCosts, fDeposit));
                    dialog.dispose();
                });
    }

    private Float validatePrice(String val, String fieldName) {
        try {
            return Float.parseFloat(val);
        } catch (NumberFormatException n) {
            JOptionPane.showMessageDialog(new JFrame(), fieldName + " is not valid!", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

}
