package org.exaphex.realty.ui.units;

import org.exaphex.realty.model.Credit;
import org.exaphex.realty.model.Unit;

import javax.swing.*;
import javax.swing.text.DateFormatter;
import javax.swing.text.DefaultFormatterFactory;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.exaphex.realty.util.PriceUtils.validatePrice;

public class CreditModal {
    private JDialog dialog;
    private final Unit unit;
    private final UnitWindow uw;
    private JTextField txtName;
    private JTextField txtDescription;
    private JFormattedTextField txtStartDate;
    private JTextField txtInterestRate;
    private JTextField txtRedemptionRate;
    private JButton btnSave;
    private JPanel mainPanel;
    private JFormattedTextField txtEndDate;
    private JTextField txtAmount;

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
        txtEndDate.setFormatterFactory(dff);


        SimpleDateFormat DateFor = new SimpleDateFormat("dd-MM-yyyy");
        txtStartDate.setText(DateFor.format(new Date()));
        txtEndDate.setText("30-12-9999");

        txtInterestRate.setText("0.00");
        txtRedemptionRate.setText("0.00");
        txtAmount.setText("0.00");

        this.dialog = new JDialog();
        dialog.setTitle("Add Credit");
        dialog.add(mainPanel);
        dialog.pack();
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    private void setupListeners() {
        btnSave.addActionListener(
                e -> {
                    if (txtName.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(new JFrame(), "Name is mandatory!", "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (txtStartDate.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(new JFrame(), "Date is not valid!", "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (txtEndDate.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(new JFrame(), "Date is not valid!", "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    Float fInterestRate = validatePrice(txtInterestRate.getText(), "Interest rate");
                    if (fInterestRate == null) {
                        return;
                    }

                    Float fRedemptionRate = validatePrice(txtRedemptionRate.getText(), "Redemption rate");
                    if (fRedemptionRate == null) {
                        return;
                    }

                    Float fAmount = validatePrice(txtAmount.getText(), "Amount");
                    if (fAmount == null) {
                        return;
                    }

                    uw.eventAddNewCredit(new Credit(txtName.getText(), unit.getId(), txtDescription.getText(),fInterestRate/100, fRedemptionRate/100, txtStartDate.getText(), txtEndDate.getText(), fAmount));
                    dialog.dispose();
                });
    }
}
