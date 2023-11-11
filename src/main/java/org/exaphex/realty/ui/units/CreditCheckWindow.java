package org.exaphex.realty.ui.units;

import org.exaphex.realty.db.service.CreditService;
import org.exaphex.realty.db.service.ReceivableService;
import org.exaphex.realty.db.service.RentService;
import org.exaphex.realty.db.service.TransactionService;
import org.exaphex.realty.model.*;
import org.exaphex.realty.model.ui.table.PaymentCheckTableModel;
import org.exaphex.realty.processor.CreditPaymentCheckProcessor;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class CreditCheckWindow {
    private final PaymentCheckTableModel pct = new PaymentCheckTableModel(new ArrayList<>());
    private final UnitWindow uw;
    private final Unit unit;
    private JButton btnCreateTransactions;
    private JTable tblCreditCheck;
    private JScrollPane tblTransactions;
    private JPanel mainPanel;
    private JDialog dialog;

    public CreditCheckWindow(UnitWindow uw, Unit u) {
        this.unit = u;
        this.uw = uw;
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
                dialog.dispose();
            });
    }

    private void loadData() {
        List<Credit> credits = CreditService.getCredit(this.unit);
        List<Transaction> transactions = TransactionService.getTransactions(this.unit);
        List<PaymentCheck> paymentChecks = CreditPaymentCheckProcessor.getCreditPaymentCheck(this.unit, credits, transactions);
        pct.setPaymentChecks(paymentChecks);
    }
}
