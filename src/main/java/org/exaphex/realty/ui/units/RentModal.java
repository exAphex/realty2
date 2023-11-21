package org.exaphex.realty.ui.units;

import org.exaphex.realty.model.Rent;
import org.exaphex.realty.model.Unit;

import javax.swing.*;
import javax.swing.text.DateFormatter;
import javax.swing.text.DefaultFormatterFactory;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import static org.exaphex.realty.util.PriceUtils.validatePrice;

public class RentModal {
    private final ResourceBundle res = ResourceBundle.getBundle("i18n");
    private Unit unit;
    private Rent selectedRent;
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
        setupCreateUI();
        setupListeners();
    }

    public RentModal(UnitWindow uw, Rent rent) {
        this.uw = uw;
        this.selectedRent = rent;
        setupEditUI();
        setupListeners();
    }

    private void setupCreateUI() {
        setupDialog();

        SimpleDateFormat DateFor = new SimpleDateFormat("dd-MM-yyyy");
        txtStartDate.setText(DateFor.format(new Date()));
        txtEndDate.setText("30-12-9999");

        txtRentalPrice.setText("0.00");
        txtExtraCost.setText("0.00");
        txtDeposit.setText("0.00");
    }

    private void setupEditUI() {
        setupDialog();

        txtFirstName.setText(this.selectedRent.getFirstName());
        txtLastName.setText(this.selectedRent.getLastName());

        txtStartDate.setText(this.selectedRent.getStartDate());
        txtEndDate.setText(this.selectedRent.getEndDate());

        txtRentalPrice.setText(this.selectedRent.getRentalPrice()+"");
        txtExtraCost.setText(this.selectedRent.getExtraCosts()+"");
        txtDeposit.setText(this.selectedRent.getDeposit()+"");
    }

    private void setupDialog() {
        DateFormatter df = new DateFormatter(new SimpleDateFormat("dd-MM-yyyy"));
        DefaultFormatterFactory dff = new DefaultFormatterFactory(df, df, df, df);
        txtStartDate.setFormatterFactory(dff);
        txtEndDate.setFormatterFactory(dff);

        this.dialog = new JDialog();
        dialog.setTitle(res.getString("titleAddRent"));
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
                        JOptionPane.showMessageDialog(new JFrame(), res.getString("msgFirstNameInvalid"), res.getString("msgError"),
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (txtLastName.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(new JFrame(), res.getString("msgLastNameInvalid"), res.getString("msgError"),
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

                    Float fRentalPrice = validatePrice(txtRentalPrice.getText(), res.getString("msgRentalPrice"));
                    Float fExtraCosts = validatePrice(txtExtraCost.getText(), res.getString("msgExtraCosts"));
                    Float fDeposit = validatePrice(txtDeposit.getText(), res.getString("msgDeposit"));
                    if (fRentalPrice == null || fExtraCosts == null || fDeposit == null) {
                        return;
                    }
                    if (this.selectedRent != null) {
                        uw.eventEditRent(new Rent(this.selectedRent.getId(), txtFirstName.getText(), txtLastName.getText(), this.selectedRent.getUnitId(), txtStartDate.getText(), txtEndDate.getText(), fRentalPrice, fExtraCosts, fDeposit));
                    } else {
                        uw.eventAddNewRent(new Rent(txtFirstName.getText(), txtLastName.getText(), this.unit.getId(), txtStartDate.getText(), txtEndDate.getText(), fRentalPrice, fExtraCosts, fDeposit));
                    }

                    dialog.dispose();
                });
    }

}
