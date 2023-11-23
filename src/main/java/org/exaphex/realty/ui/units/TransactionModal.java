package org.exaphex.realty.ui.units;

import org.exaphex.realty.db.service.CreditService;
import org.exaphex.realty.db.service.RentService;
import org.exaphex.realty.db.service.UnitService;
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
import java.util.ResourceBundle;

import static org.exaphex.realty.model.Transaction.formatTransactionType;
import static org.exaphex.realty.model.Transaction.getTypeIdByTransactionType;
import static org.exaphex.realty.util.PriceUtils.validatePrice;

public class TransactionModal {
    private final ResourceBundle res = ResourceBundle.getBundle("i18n");
    private final CreditComboBoxModel ccm = new CreditComboBoxModel(new ArrayList<>());
    private final RentComboBoxModel rcm = new RentComboBoxModel(new ArrayList<>());
    private final UnitWindow uw;
    private Unit unit;
    private Transaction transaction;
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
    private JComboBox<Credit> cmbCredit;
    private JComboBox<Rent> cmbRent;

    public TransactionModal(UnitWindow uw, Unit u) {
        this.uw = uw;
        this.unit = u;
        setupUI();
        setupListeners();
        loadData();
    }

    public TransactionModal(UnitWindow uw, Transaction transaction) {
        this.uw = uw;
        this.transaction = transaction;
        setupUI();
        setupListeners();
        loadData();
        setupEditUI();
    }

    private void setupEditUI() {
        String reference = this.transaction.getReference();
        txtDate.setText(this.transaction.getDate());
        txtDescription.setText(this.transaction.getDescription());
        txtSecondary.setText(this.transaction.getSecondaryAmount()+"");
        txtAmount.setText(this.transaction.getAmount()+"");
        cmbTypes.setSelectedItem(formatTransactionType(this.transaction.getType()));
        CreditComboBoxModel creditModel = (CreditComboBoxModel) cmbCredit.getModel();
        RentComboBoxModel rentModel = (RentComboBoxModel) cmbRent.getModel();
        switch (this.transaction.getType()) {
            case Transaction.CREDIT_PAYMENT -> {
                int pos = creditModel.getCreditIndexById(reference);
                if (pos >= 0) {
                    cmbCredit.setSelectedIndex(pos);
                }
            }
            case Transaction.RENT_PAYMENT -> {
                int pos = rentModel.getRentIndexById(reference);
                if (pos >= 0) {
                    cmbRent.setSelectedIndex(pos);
                }
            }
        }
    }

    private void loadData() {
        if (this.unit != null) {
            loadCredits(this.unit);
            loadRents(this.unit);
        } else {
            Unit u = UnitService.getUnitById(this.transaction.getUnitId());
            loadCredits(u);
            loadRents(u);
        }

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
        dialog.setTitle(res.getString("titleAddTransaction"));
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
                        JOptionPane.showMessageDialog(new JFrame(), res.getString("msgDateInvalid"), res.getString("msgError"),
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    Float fAmount = validatePrice(txtAmount.getText(), res.getString("msgAmount"));
                    if (fAmount == null) {
                        return;
                    }

                    Float fSecondaryAmount = validatePrice(txtSecondary.getText(), res.getString("msgSecondaryAmount"));
                    if (fSecondaryAmount == null) {
                        return;
                    }

                    String selectedType = (String) cmbTypes.getSelectedItem();
                    assert selectedType != null;
                    int type = getTypeIdByTransactionType(selectedType);

                    if (type == Transaction.RENT_PAYMENT) {
                        Rent rent = (Rent) cmbRent.getSelectedItem();
                        if (rent == null) {
                            JOptionPane.showMessageDialog(new JFrame(), res.getString("msgInvalidRentReference"), res.getString("msgError"),
                                    JOptionPane.ERROR_MESSAGE);
                            return;
                        } else {
                            reference = rent.getId();
                        }
                    }

                    if (type == Transaction.CREDIT_PAYMENT) {
                        Credit credit = (Credit) cmbCredit.getSelectedItem();
                        if (credit == null) {
                            JOptionPane.showMessageDialog(new JFrame(), res.getString("msgInvalidCreditReference"), res.getString("msgError"),
                                    JOptionPane.ERROR_MESSAGE);
                            return;
                        } else {
                            reference = credit.getId();
                        }
                    }

                    if (this.transaction != null) {
                        uw.eventEditTransaction(new Transaction(this.transaction.getId(), txtDescription.getText(), reference, txtDate.getText(),type, this.transaction.getUnitId(), fAmount, fSecondaryAmount));
                    } else {
                        uw.eventAddNewTransaction(new Transaction(txtDescription.getText(), reference, txtDate.getText(),type, this.unit.getId(), fAmount, fSecondaryAmount));
                    }
                    dialog.dispose();
                });

        cmbTypes.addItemListener(event -> {
            if (event.getStateChange() == ItemEvent.SELECTED) {
                String item = (String) cmbTypes.getSelectedItem();
                int type = 0;
                if (item != null) {
                    type = getTypeIdByTransactionType(item);
                }
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
            lblSecondary.setText(res.getString("lblExtraCosts"));
        } else if (type == Transaction.CREDIT_PAYMENT) {
            lblSecondary.setVisible(true);
            txtSecondary.setVisible(true);
            cmbCredit.setVisible(true);
            lblCreditReference.setVisible(true);
            lblSecondary.setText(res.getString("lblInterest"));
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
