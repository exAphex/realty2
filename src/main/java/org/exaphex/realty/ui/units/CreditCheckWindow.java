package org.exaphex.realty.ui.units;

import org.exaphex.realty.db.service.TransactionService;
import org.exaphex.realty.model.*;
import org.exaphex.realty.model.ui.table.PaymentCheckTableModel;
import org.exaphex.realty.processor.CreditPaymentCheckProcessor;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CreditCheckWindow {
    private final PaymentCheckTableModel pct = new PaymentCheckTableModel(new ArrayList<>());
    private final UnitWindow uw;
    private final Unit unit;
    private final Credit credit;
    private JButton btnCreateTransactions;
    private JTable tblCreditCheck;
    private JScrollPane tblTransactions;
    private JPanel mainPanel;
    private JCheckBox chkShowOnlyUnpaid;
    private JDialog dialog;

    public CreditCheckWindow(UnitWindow uw, Unit u, Credit c) {
        this.unit = u;
        this.uw = uw;
        this.credit = c;
        setupUI();
        setupListeners();
        loadData();
    }

    private void setupUI() {
        tblCreditCheck.setModel(pct);
        tblCreditCheck.getSelectionModel().addListSelectionListener(event -> btnCreateTransactions.setEnabled(tblCreditCheck.getSelectedRowCount() > 0));

        this.dialog = new JDialog();
        dialog.setTitle("Credit payment check");
        dialog.add(mainPanel);
        dialog.pack();
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    private void setupListeners() {
        btnCreateTransactions.addActionListener(
            e -> {
                int[] selectedRows = tblCreditCheck.getSelectedRows();
                for (int i : selectedRows) {
                    Transaction transaction = pct.getPaymentCheck(i).getTransaction();
                    uw.eventAddNewTransaction(transaction);
                }
                uw.loadTransactions(this.unit);
                uw.loadCredits(this.unit);
                dialog.dispose();
            });
        chkShowOnlyUnpaid.addActionListener(e -> {
            loadData();
        });
    }

    private void loadData() {
        List<Transaction> transactions = TransactionService.getTransactions(this.unit);
        List<PaymentCheck> paymentChecks = CreditPaymentCheckProcessor.getCreditPaymentCheck(this.unit, this.credit, transactions);
        if (chkShowOnlyUnpaid.isSelected()) {
            paymentChecks = paymentChecks.stream().filter(p -> p.getPaidAmount() == 0).collect(Collectors.toList());
        }
        pct.setPaymentChecks(paymentChecks);
    }
}
