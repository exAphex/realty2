package org.exaphex.realty.model.ui.cmb;

import org.exaphex.realty.model.Account;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.util.ArrayList;
import java.util.List;

public class AccountComboBoxModel implements ComboBoxModel<Account> {
    private List<Account> accounts;
    int index = -1;
    public AccountComboBoxModel(List<Account> accounts) {
        this.accounts = new ArrayList<>(accounts);
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = new ArrayList<>(accounts);
    }

    public int getAccountById(String id) {
        if (id == null) {
            return -1;
        }
        for (int i = 0; i < accounts.size(); i++) {
            if (id.equals(accounts.get(i).getId())) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void setSelectedItem(Object anItem) {
        for(int i = 0; i < accounts.size(); i++) {
            if(accounts.get(i).equals(anItem)) {
                index = i;
                break;
            }
        }
    }

    @Override
    public Object getSelectedItem() {
        if(index >= 0){
            return accounts.get(index);
        } else {
            return null;
        }
    }

    @Override
    public int getSize() {
        return accounts.size();
    }

    @Override
    public Account getElementAt(int index) {
        return accounts.get(index);
    }

    @Override
    public void addListDataListener(ListDataListener l) {

    }

    @Override
    public void removeListDataListener(ListDataListener l) {

    }
}
