package org.exaphex.realty.model.ui.cmb;

import org.exaphex.realty.model.Credit;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.util.ArrayList;
import java.util.List;

public class CreditComboBoxModel implements ComboBoxModel<Credit> {
    private List<Credit> credits;
    int index = -1;
    public CreditComboBoxModel(List<Credit> credits) {
        this.credits = new ArrayList<>(credits);
    }

    public void setCredits(List<Credit> credits) {
        this.credits = new ArrayList<>(credits);
    }

    public int getCreditIndexById(String id) {
        if (id == null) {
            return -1;
        }
        for (int i = 0; i < credits.size(); i++) {
            if (id.equals(credits.get(i).getId())) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void setSelectedItem(Object anItem) {
        for(int i = 0; i < credits.size(); i++) {
            if(credits.get(i).equals(anItem)) {
                index = i;
                break;
            }
        }
    }

    @Override
    public Object getSelectedItem() {
        if(index >= 0){
            return credits.get(index);
        } else {
            return "";
        }
    }

    @Override
    public int getSize() {
        return credits.size();
    }

    @Override
    public Credit getElementAt(int index) {
        return credits.get(index);
    }

    @Override
    public void addListDataListener(ListDataListener l) {

    }

    @Override
    public void removeListDataListener(ListDataListener l) {

    }
}
