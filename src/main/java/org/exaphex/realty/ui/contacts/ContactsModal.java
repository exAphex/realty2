package org.exaphex.realty.ui.contacts;

import org.exaphex.realty.model.Contact;

import javax.swing.*;

import java.util.ResourceBundle;

public class ContactsModal {
    private final ResourceBundle res = ResourceBundle.getBundle("i18n");
    private JTextField txtFirstName;
    private JTextField txtLastName;
    private JTextField txtMail;
    private JTextField txtTelNr;
    private JButton btnSave;
    private JPanel mainPanel;
    private ContactsPane cp;
    private JDialog dialog;
    private Contact selectedContact;

    public ContactsModal(ContactsPane cp) {
        this.cp = cp;
        setupUI();
        setupListeners();
    }

    public ContactsModal(ContactsPane cp, Contact contact) {
        this.cp = cp;
        this.selectedContact = contact;
        setupUI();
        setupEditUI();
        setupListeners();
    }

    private void setupUI() {
        this.dialog = new JDialog();
        dialog.setTitle(res.getString("titleAddContact"));
        dialog.add(mainPanel);
        dialog.pack();
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    private void setupEditUI() {
        txtFirstName.setText(this.selectedContact.getFirstName());
        txtLastName.setText(this.selectedContact.getLastName());
        txtMail.setText(this.selectedContact.getMail());
        txtTelNr.setText(this.selectedContact.getTelNumber());
    }

    private void setupListeners() {
        btnSave.addActionListener(
                e -> {
                    if (txtFirstName.getText().isEmpty() || txtLastName.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(new JFrame(), res.getString("msgFirstOrLastNameMissing"), res.getString("msgError"),
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (this.selectedContact != null) {
                        cp.eventUpdateContact(new Contact(this.selectedContact.getId(), txtFirstName.getText(), txtLastName.getText(), txtMail.getText(),
                                txtTelNr.getText()));
                    } else {
                        cp.eventAddNewContact(new Contact(txtFirstName.getText(), txtLastName.getText(), txtMail.getText(),
                                txtTelNr.getText()));
                    }
                    dialog.dispose();
                });
    }
}
