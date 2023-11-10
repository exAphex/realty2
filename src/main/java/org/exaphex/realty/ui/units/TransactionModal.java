package org.exaphex.realty.ui.units;

import org.exaphex.realty.model.Transaction;
import org.exaphex.realty.model.Unit;

import javax.swing.*;
import javax.swing.text.DateFormatter;
import javax.swing.text.DefaultFormatterFactory;
import java.awt.event.ItemEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.exaphex.realty.model.Transaction.formatTransactionType;
import static org.exaphex.realty.model.Transaction.getTypeIdByTransactionType;
import static org.exaphex.realty.util.PriceUtils.validatePrice;

public class TransactionModal {
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

    public TransactionModal(UnitWindow uw, Unit u) {
        this.uw = uw;
        this.unit = u;
        setupUI();
        setupListeners();
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

                    uw.eventAddNewTransaction(new Transaction(txtDate.getText(),type, this.unit.getId(), fAmount, fSecondaryAmount));
                    dialog.dispose();
                });

        cmbTypes.addItemListener(event -> {
            if (event.getStateChange() == ItemEvent.SELECTED) {
                String item = (String) cmbTypes.getSelectedItem();
                int type = getTypeIdByTransactionType(item);
                if (type == Transaction.RENT_PAYMENT || type == Transaction.CREDIT_PAYMENT) {
                    lblSecondary.setVisible(true);
                    txtSecondary.setVisible(true);
                    lblSecondary.setText(type == Transaction.RENT_PAYMENT ? "Extra costs:" : "Interest:");
                    this.dialog.pack();
                } else {
                    lblSecondary.setVisible(false);
                    txtSecondary.setVisible(false);
                    this.dialog.pack();
                }
            }
        });
    }
}
