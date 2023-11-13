package org.exaphex.realty.ui.units;

import org.exaphex.realty.db.service.CreditService;
import org.exaphex.realty.db.service.RentService;
import org.exaphex.realty.model.*;
import org.exaphex.realty.model.ui.cmb.CreditComboBoxModel;
import org.exaphex.realty.model.ui.cmb.RentComboBoxModel;

import javax.swing.*;
import javax.swing.text.DateFormatter;
import javax.swing.text.DefaultFormatterFactory;
import java.awt.event.ItemEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.exaphex.realty.model.Transaction.formatTransactionType;
import static org.exaphex.realty.model.Transaction.getTypeIdByTransactionType;
import static org.exaphex.realty.util.PriceUtils.validatePrice;

public class TransactionModal {
    private CreditComboBoxModel ccm = new CreditComboBoxModel(new ArrayList<>());
    private RentComboBoxModel rcm = new RentComboBoxModel(new ArrayList<>());
    private final UnitWindow uw;
    private final Unit unit;
    private JTextField txtAmount;
    private JFormattedTextField txtDate;
    private JButton btnSave;
    private JPanel mainPanel;
    private JDialog dialog;
    private JComboBox<String> cmbTypes;
    private JFormattedTextField txtSecondary;
    private JLabel lblSecondary;
    private JTextField txtDescription;
    private JLabel lblCreditReference;
    private JLabel lblRentReference;
    private JComboBox cmbCredit;
    private JComboBox cmbRent;

    public TransactionModal(UnitWindow uw, Unit u) {
        this.uw = uw;
        this.unit = u;
        setupUI();
        setupListeners();
        loadData();
    }

    private void loadData() {
        loadCredits(this.unit);
        loadRents(this.unit);
    }

    private void setupUI() {
        DateFormatter df = new DateFormatter(new SimpleDateFormat("dd-MM-yyyy"));
        DefaultFormatterFactory dff = new DefaultFormatterFactory(df, df, df, df);
        txtDate.setFormatterFactory(dff);

        SimpleDateFormat DateFor = new SimpleDateFormat("dd-MM-yyyy");
        txtDate.setText(DateFor.format(new Date()));
        txtAmount.setText("0.00");
        txtSecondary.setText("0.00");

        cmbTypes.addItem(formatTransactionType(0));
        cmbTypes.addItem(formatTransactionType(1));
        cmbTypes.addItem(formatTransactionType(2));

        cmbCredit.setModel(ccm);
        cmbRent.setModel(rcm);

        this.dialog = new JDialog();
        dialog.setTitle("Add Transaction");
        dialog.add(mainPanel);
        dialog.pack();
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);

    }

    private void setupListeners() {
        btnSave.addActionListener(
                e -> {
                    String reference = "";
                    if (txtDate.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(new JFrame(), "Date is not valid!", "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    Float fAmount = validatePrice(txtAmount.getText(), "Amount");
                    if (fAmount == null) {
                        return;
                    }

                    Float fSecondaryAmount = validatePrice(txtSecondary.getText(), "Secondary amount");
                    if (fSecondaryAmount == null) {
                        return;
                    }

                    String selectedType = (String) cmbTypes.getSelectedItem();
                    assert selectedType != null;
                    int type = getTypeIdByTransactionType(selectedType);

                    if (type == Transaction.RENT_PAYMENT) {
                        Rent rent = (Rent) cmbRent.getSelectedItem();
                        if (rent == null) {
                            JOptionPane.showMessageDialog(new JFrame(), "Select a valid reference rent!", "Error",
                                    JOptionPane.ERROR_MESSAGE);
                            return;
                        } else {
                            reference = rent.getId();
                        }
                    }

                    if (type == Transaction.CREDIT_PAYMENT) {
                        Credit credit = (Credit) cmbCredit.getSelectedItem();
                        if (credit == null) {
                            JOptionPane.showMessageDialog(new JFrame(), "Select a valid reference credit!", "Error",
                                    JOptionPane.ERROR_MESSAGE);
                            return;
                        } else {
                            reference = credit.getId();
                        }
                    }

                    uw.eventAddNewTransaction(new Transaction(txtDescription.getText(), reference, txtDate.getText(),type, this.unit.getId(), fAmount, fSecondaryAmount));
                    dialog.dispose();
                });

        cmbTypes.addItemListener(event -> {
            if (event.getStateChange() == ItemEvent.SELECTED) {
                String item = (String) cmbTypes.getSelectedItem();
                int type = getTypeIdByTransactionType(item);
                setVisibilities(type);
            }
        });
    }

    private void setVisibilities(int type) {
        lblSecondary.setVisible(false);
        txtSecondary.setVisible(false);
        cmbCredit.setVisible(false);
        cmbRent.setVisible(false);
        lblCreditReference.setVisible(false);
        lblRentReference.setVisible(false);

        if (type == Transaction.RENT_PAYMENT) {
            lblSecondary.setVisible(true);
            txtSecondary.setVisible(true);
            cmbRent.setVisible(true);
            lblRentReference.setVisible(true);
            lblSecondary.setText("Extra costs:");
        } else if (type == Transaction.CREDIT_PAYMENT) {
            lblSecondary.setVisible(true);
            txtSecondary.setVisible(true);
            cmbCredit.setVisible(true);
            lblCreditReference.setVisible(true);
            lblSecondary.setText("Interest:");
        }
        this.dialog.pack();
    }

    private void loadCredits(Unit u) {
        List<Credit> credits = CreditService.getCredit(u);
        ccm.setCredits(credits);
    }

    private void loadRents(Unit u) {
        List<Rent> rents = RentService.getRents(u);
        rcm.setRents(rents);
    }
}
