package org.exaphex.realty.ui.contacts;

import org.exaphex.realty.db.service.ContactService;
import org.exaphex.realty.model.Contact;
import org.exaphex.realty.model.ui.table.ContactTableModel;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ContactsPane {
    private final ResourceBundle res = ResourceBundle.getBundle("i18n");
    final ContactTableModel ctm = new ContactTableModel(new ArrayList<>());
    private JButton btnAddContact;
    private JButton btnDeleteContact;
    private JTable tblContacts;
    private JPanel mainPanel;

    public void setUI() {
        buildUI();
        setupListeners();
    }

    private void buildUI() {
        tblContacts.setModel(ctm);
    }

    private void setupListeners() {
        ContactsPane self = this;
        btnAddContact.addActionListener(e -> this.onAddNewContact());
        btnDeleteContact.addActionListener(e -> this.onDeleteContacts());
        tblContacts.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouseEvent) {
                JTable table = (JTable) mouseEvent.getSource();
                int selectedRow = table.getSelectedRow();
                if (mouseEvent.getClickCount() == 2 && selectedRow != -1) {
                    Contact contact = ctm.getContactAt(tblContacts.convertRowIndexToModel(selectedRow));
                    new ContactsModal(self, contact);
                }
            }
        });
    }

    public void loadContacts() {
        List<Contact> contacts = ContactService.getContacts();
        ctm.setContacts(contacts);
    }

    public void eventAddNewContact(Contact contact) {
        ContactService.addContact(contact);
        loadContacts();
    }

    private void onAddNewContact() {
        new ContactsModal(this);
    }

    public void eventUpdateContact(Contact contact) {
        ContactService.updateContact(contact);
        loadContacts();
    }

    private void onDeleteContacts() {
        int[] selectedRows = tblContacts.getSelectedRows();
        for (int i : selectedRows) {
            Contact contact = ctm.getContactAt(tblContacts.convertRowIndexToModel(i));
            try {
                ContactService.deleteContact(contact);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(new JFrame(), res.getString("msgRentExistsWithContact") + " " + contact.getFirstName() + " " + contact.getLastName(), res.getString("msgError"),
                        JOptionPane.ERROR_MESSAGE);
            }
        }
        loadContacts();
    }
}
