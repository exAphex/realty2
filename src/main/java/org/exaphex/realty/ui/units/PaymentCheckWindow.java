package org.exaphex.realty.ui.units;

import org.exaphex.realty.db.service.TransactionService;
import org.exaphex.realty.model.*;
import org.exaphex.realty.model.ui.table.PaymentCheckTableModel;
import org.exaphex.realty.processor.CreditPaymentCheckProcessor;
import org.exaphex.realty.processor.RentPaymentCheckProcessor;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PaymentCheckWindow {
    private final PaymentCheckTableModel pct = new PaymentCheckTableModel(new ArrayList<>());
    private final UnitWindow uw;
    private final Unit unit;
    private Credit credit;
    private Rent rent;
    private JButton btnCreateTransactions;
    private JTable tblPaymentCheck;
    private JPanel mainPanel;
    private JCheckBox chkShowOnlyUnpaid;
    private JDialog dialog;
    private final int type;

    public PaymentCheckWindow(UnitWindow uw, Unit u, Credit c) {
        this.unit = u;
        this.uw = uw;
        this.credit = c;
        this.type = Transaction.CREDIT_PAYMENT;
        setupUI();
        setupListeners();
        loadData();
    }

    public PaymentCheckWindow(UnitWindow uw, Unit u, Rent r) {
        this.unit = u;
        this.uw = uw;
        this.rent = r;
        this.type = Transaction.RENT_PAYMENT;
        setupUI();
        setupListeners();
        loadData();
    }

    private void setupUI() {
        tblPaymentCheck.setModel(pct);
        tblPaymentCheck.getSelectionModel().addListSelectionListener(event -> btnCreateTransactions.setEnabled(tblPaymentCheck.getSelectedRowCount() > 0));

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
                int[] selectedRows = tblPaymentCheck.getSelectedRows();
                for (int i : selectedRows) {
                    Transaction transaction = pct.getPaymentCheck(i).getTransaction();
                    uw.eventAddNewTransaction(transaction);
                }
                uw.loadTransactions(this.unit);
                if (type == Transaction.CREDIT_PAYMENT) {
                    uw.loadCredits(this.unit);
                }
                dialog.dispose();
            });
        chkShowOnlyUnpaid.addActionListener(e -> loadData());
    }

    private void loadData() {
        List<PaymentCheck> paymentChecks;
        if (type == Transaction.CREDIT_PAYMENT) {
            paymentChecks = loadCreditData();
        } else {
            paymentChecks = loadRentData();
        }
        pct.setPaymentChecks(paymentChecks);
    }

    private List<PaymentCheck> loadCreditData() {
        List<Transaction> transactions = TransactionService.getTransactions(this.unit);
        List<PaymentCheck> paymentChecks = CreditPaymentCheckProcessor.getCreditPaymentCheck(this.unit, this.credit, transactions);
        if (chkShowOnlyUnpaid.isSelected()) {
            paymentChecks = paymentChecks.stream().filter(p -> p.getPaidAmount() == 0).collect(Collectors.toList());
        }
        return paymentChecks;

    }

    private List<PaymentCheck> loadRentData() {
        List<Transaction> transactions = TransactionService.getTransactions(this.unit);
        List<PaymentCheck> paymentChecks = RentPaymentCheckProcessor.getRentPaymentCheck(this.unit, this.rent, transactions);
        if (chkShowOnlyUnpaid.isSelected()) {
            paymentChecks = paymentChecks.stream().filter(p -> p.getPaidAmount() == 0).collect(Collectors.toList());
        }
        return paymentChecks;

    }
}
