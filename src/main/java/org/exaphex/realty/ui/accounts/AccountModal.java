package org.exaphex.realty.ui.accounts;

import org.exaphex.realty.model.Account;

import javax.swing.*;
import java.util.ResourceBundle;

public class AccountModal {
    private final ResourceBundle res = ResourceBundle.getBundle("i18n");
    private JTextField txtName;
    private JTextField txtIBAN;
    private JTextField txtBIC;
    private JButton btnSave;
    private JPanel mainPanel;
    private final AccountPane ap;
    private Account selectedAccount;
    private JDialog dialog;

    public AccountModal(AccountPane ap) {
        this.ap = ap;
        setupUI();
        setupListeners();
    }

    public AccountModal(AccountPane ap, Account account) {
        this.ap = ap;
        this.selectedAccount = account;
        setupUI();
        setupEditUI();
        setupListeners();
    }

    private void setupUI() {
        this.dialog = new JDialog();
        dialog.setTitle(res.getString("titleManageAccount"));
        dialog.add(mainPanel);
        dialog.pack();
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    private void setupEditUI() {
        txtName.setText(this.selectedAccount.getName());
        txtIBAN.setText(this.selectedAccount.getIban());
        txtBIC.setText(this.selectedAccount.getBic());
    }

    private void setupListeners() {
        btnSave.addActionListener(
                e -> {
                    if (txtName.getText().isEmpty() || txtIBAN.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(new JFrame(), res.getString("msgNameOrIBANMissing"), res.getString("msgError"),
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (this.selectedAccount != null) {
                        ap.eventUpdateAccount(new Account(this.selectedAccount.getId(), txtName.getText(), txtIBAN.getText(), txtBIC.getText()));
                    } else {
                        ap.eventAddNewAccount(new Account(txtName.getText(), txtIBAN.getText(), txtBIC.getText()));
                    }
                    dialog.dispose();
                });
    }
}