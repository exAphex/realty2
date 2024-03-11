package org.exaphex.realty.ui.units;

import org.exaphex.realty.db.service.AccountService;
import org.exaphex.realty.db.service.TransactionService;
import org.exaphex.realty.model.*;
import org.exaphex.realty.model.ui.cmb.AccountComboBoxModel;
import org.exaphex.realty.model.ui.table.PaymentCheckTableModel;
import org.exaphex.realty.processor.CreditPaymentCheckProcessor;
import org.exaphex.realty.processor.RentPaymentCheckProcessor;
import org.exaphex.realty.ui.credit.CreditPane;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class PaymentCheckWindow {
    private final ResourceBundle res = ResourceBundle.getBundle("i18n");
    private final PaymentCheckTableModel pct = new PaymentCheckTableModel(new ArrayList<>());
    private final AccountComboBoxModel acm = new AccountComboBoxModel(new ArrayList<>());
    private final CreditPane cp;
    private final Unit unit;
    private Credit credit;
    private Rent rent;
    private JButton btnCreateTransactions;
    private JTable tblPaymentCheck;
    private JPanel mainPanel;
    private JCheckBox chkShowOnlyUnpaid;
    private JComboBox<Account> cmbAccount;
    private JDialog dialog;
    private final int type;

    public PaymentCheckWindow(CreditPane cp, Unit u, Credit c) {
        this.unit = u;
        this.cp = cp;
        this.credit = c;
        this.type = Transaction.CREDIT_PAYMENT;
        setupUI();
        setupListeners();
        loadData();
    }

    public PaymentCheckWindow(CreditPane cp, Unit u, Rent r) {
        this.unit = u;
        this.cp = cp;
        this.rent = r;
        this.type = Transaction.RENT_PAYMENT;
        setupUI();
        setupListeners();
        loadData();
    }

    private void setupUI() {
        tblPaymentCheck.setModel(pct);
        tblPaymentCheck.getSelectionModel().addListSelectionListener(event -> btnCreateTransactions.setEnabled(tblPaymentCheck.getSelectedRowCount() > 0));

        this.cmbAccount.setModel(acm);

        this.dialog = new JDialog();
        dialog.setTitle(res.getString("titlePaymentCheck"));
        dialog.add(mainPanel);
        dialog.pack();
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    private void setupListeners() {
        btnCreateTransactions.addActionListener(
            e -> {
                Account account = (Account) cmbAccount.getSelectedItem();
                if (account == null) {
                    JOptionPane.showMessageDialog(new JFrame(), res.getString("msgInvalidAccount"), res.getString("msgError"),
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int[] selectedRows = tblPaymentCheck.getSelectedRows();
                for (int i : selectedRows) {
                    Transaction transaction = pct.getPaymentCheck(i).getTransaction();
                    transaction.setAccountId(account.getId());
                    cp.eventAddNewTransaction(transaction);
                }

                if (type == Transaction.CREDIT_PAYMENT) {
                    cp.loadData();
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
        loadAccounts();
    }

    private void loadAccounts() {
        List<Account> accounts = AccountService.getAccounts();
        this.acm.setAccounts(accounts);
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
