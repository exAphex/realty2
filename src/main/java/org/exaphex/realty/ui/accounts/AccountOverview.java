package org.exaphex.realty.ui.accounts;

import org.exaphex.realty.db.service.BuildingService;
import org.exaphex.realty.db.service.TransactionService;
import org.exaphex.realty.db.service.UnitService;
import org.exaphex.realty.model.Account;
import org.exaphex.realty.model.Building;
import org.exaphex.realty.model.Transaction;
import org.exaphex.realty.model.Unit;
import org.exaphex.realty.model.ui.table.AccountTransactionTableModel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AccountOverview extends JFrame {
    private final ResourceBundle res = ResourceBundle.getBundle("i18n");
    private JButton btnAdd;
    private JButton btnDelete;
    private JPanel mainPanel;
    private JTable tblTransactions;
    private JTabbedPane tabbedPane1;
    private JTextField txtName;
    private JTextField txtIBAN;
    private JTextField txtBIC;
    private JButton btnSave;
    private AccountPane ap;
    private final Account account;
    private final AccountTransactionTableModel atm = new AccountTransactionTableModel(new ArrayList<>());

    public AccountOverview(AccountPane ap, Account account) {
        this.ap = ap;
        this.account = account;
        setupUI();
        setupEditUI();
        setupListeners();
        loadTransactions();
    }

    private void setupUI() {
        tblTransactions.setModel(atm);
        this.setTitle(this.account.getName());
        setContentPane(mainPanel);
    }

    private void setupEditUI() {
        txtName.setText(this.account.getName());
        txtIBAN.setText(this.account.getIban());
        txtBIC.setText(this.account.getBic());
    }

    private void setupListeners() {
        btnSave.addActionListener(
                e -> {
                    if (txtName.getText().isEmpty() || txtIBAN.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(new JFrame(), res.getString("msgNameOrIBANMissing"), res.getString("msgError"),
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (this.account != null) {
                        ap.eventUpdateAccount(new Account(this.account.getId(), txtName.getText(), txtIBAN.getText(), txtBIC.getText()));
                    } else {
                        ap.eventAddNewAccount(new Account(txtName.getText(), txtIBAN.getText(), txtBIC.getText()));
                    }
                });
    }

    public void loadTransactions() {
        List<Transaction> transactions = TransactionService.getTransactionsByAccount(account);
        List<Building> buildings = BuildingService.getBuilding();
        List<Unit> units = UnitService.getUnits(null);
        atm.setTransactions(transactions, buildings, units);
    }

    public Account getAccount() {
        return this.account;
    }

}
