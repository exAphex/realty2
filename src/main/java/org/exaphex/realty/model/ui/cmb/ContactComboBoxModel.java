package org.exaphex.realty.model.ui.cmb;

import org.exaphex.realty.model.Contact;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.util.ArrayList;
import java.util.List;

public class ContactComboBoxModel implements ComboBoxModel<Contact> {
    private List<Contact> contacts;
    int index = -1;
    public ContactComboBoxModel(List<Contact> contacts) {
        this.contacts = new ArrayList<>(contacts);
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = new ArrayList<>(contacts);
    }

    public int getContactById(String id) {
        if (id == null) {
            return -1;
        }
        for (int i = 0; i < contacts.size(); i++) {
            if (id.equals(contacts.get(i).getId())) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void setSelectedItem(Object anItem) {
        for(int i = 0; i < contacts.size(); i++) {
            if(contacts.get(i).equals(anItem)) {
                index = i;
                break;
            }
        }
    }

    @Override
    public Object getSelectedItem() {
        if(index >= 0){
            return contacts.get(index);
        } else {
            return "";
        }
    }

    @Override
    public int getSize() {
        return contacts.size();
    }

    @Override
    public Contact getElementAt(int index) {
        return contacts.get(index);
    }

    @Override
    public void addListDataListener(ListDataListener l) {

    }

    @Override
    public void removeListDataListener(ListDataListener l) {

    }
}
