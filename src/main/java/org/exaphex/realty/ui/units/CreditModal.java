package org.exaphex.realty.ui.units;

import org.exaphex.realty.model.Credit;
import org.exaphex.realty.model.Unit;

import javax.swing.*;
import javax.swing.text.DateFormatter;
import javax.swing.text.DefaultFormatterFactory;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import static org.exaphex.realty.util.PriceUtils.validatePrice;

public class CreditModal {
    private final ResourceBundle res = ResourceBundle.getBundle("i18n");
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
        dialog.setTitle(res.getString("titleAddCredit"));
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
                        JOptionPane.showMessageDialog(new JFrame(), res.getString("msgNameMandatory"), res.getString("msgError"),
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (txtStartDate.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(new JFrame(), res.getString("msgStartDateInvalid"), res.getString("msgError"),
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (txtEndDate.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(new JFrame(), res.getString("msgEndDateInvalid"), res.getString("msgError"),
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    Float fInterestRate = validatePrice(txtInterestRate.getText(), res.getString("msgInterestRate"));
                    if (fInterestRate == null) {
                        return;
                    }

                    Float fRedemptionRate = validatePrice(txtRedemptionRate.getText(), res.getString("msgRedemptionRate"));
                    if (fRedemptionRate == null) {
                        return;
                    }

                    Float fAmount = validatePrice(txtAmount.getText(), res.getString("msgAmount"));
                    if (fAmount == null) {
                        return;
                    }

                    uw.eventAddNewCredit(new Credit(txtName.getText(), unit.getId(), txtDescription.getText(),fInterestRate/100, fRedemptionRate/100, txtStartDate.getText(), txtEndDate.getText(), fAmount));
                    dialog.dispose();
                });
    }
}
