package org.exaphex.realty.ui.accounts;

import org.exaphex.realty.db.service.AccountService;
import org.exaphex.realty.model.Account;
import org.exaphex.realty.model.ui.table.AccountTableModel;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AccountPane {
    private final ResourceBundle res = ResourceBundle.getBundle("i18n");
    private JButton btnAdd;
    private JButton btnDelete;
    private final AccountTableModel atm = new AccountTableModel(new ArrayList<>());
    private JTable tblAccounts;
    private JPanel mainPanel;

    public void setUI() {
        buildUI();
        setupListeners();
    }

    private void buildUI() {
        tblAccounts.setModel(atm);
    }

    public void setupListeners() {
        AccountPane self = this;
        btnAdd.addActionListener(e -> this.onAddNewAccount());
        btnDelete.addActionListener(e -> this.onDeleteAccount());
        tblAccounts.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouseEvent) {
                JTable table = (JTable) mouseEvent.getSource();
                int selectedRow = table.getSelectedRow();
                if (mouseEvent.getClickCount() == 2 && selectedRow != -1) {
                    Account account = atm.getAccountAt(tblAccounts.convertRowIndexToModel(selectedRow));
                    new AccountModal(self, account);
                }
            }
        });
    }

    public void loadAccounts() {
        List<Account> accounts = AccountService.getAccounts();
        atm.setAccounts(accounts);
    }

    private void onAddNewAccount() {
        new AccountModal(this);
    }

    private void onDeleteAccount() {
        int[] selectedRows = tblAccounts.getSelectedRows();
        for (int i : selectedRows) {
            Account account = atm.getAccountAt(tblAccounts.convertRowIndexToModel(i));
            try {
                AccountService.deleteAccount(account);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(new JFrame(), res.getString("msgTransactionExistsWithAccount") + " " + account.getName(), res.getString("msgError"),
                        JOptionPane.ERROR_MESSAGE);
            }
        }
        loadAccounts();
    }

    public void eventAddNewAccount(Account account) {
        AccountService.addAccount(account);
        loadAccounts();
    }

    public void eventUpdateAccount(Account account) {
        AccountService.updateAccount(account);
        loadAccounts();
    }

}
