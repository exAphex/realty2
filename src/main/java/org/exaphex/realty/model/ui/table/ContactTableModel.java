package org.exaphex.realty.model.ui.table;

import org.exaphex.realty.model.Contact;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ContactTableModel extends AbstractTableModel {
    private final ResourceBundle res = ResourceBundle.getBundle("i18n");
    private List<Contact> contacts;

    public ContactTableModel(List<Contact> contacts) {
        this.contacts = new ArrayList<>(contacts);
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = new ArrayList<>(contacts);
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return contacts.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public String getColumnName(int column) {
        return switch (column) {
            case 0:
                yield res.getString("colFirstName");
            case 1:
                yield res.getString("colLastName");
            case 2:
                yield res.getString("colMail");
            case 3:
                yield res.getString("colTelNumber");
            default:
                yield "";
        };
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Contact contact = contacts.get(rowIndex);
        return switch (columnIndex) {
            case 0:
                yield contact.getFirstName();
            case 1:
                yield contact.getLastName();
            case 2:
                yield contact.getMail();
            case 3:
                yield contact.getTelNumber();
            default:
                yield "??";
        };
    }

    public Contact getContactAt(int row) {
        return contacts.get(row);
    }
}
