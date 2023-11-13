package org.exaphex.realty.model.ui.cmb;

import org.exaphex.realty.model.Credit;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.util.ArrayList;
import java.util.List;

public class CreditComboBoxModel implements ComboBoxModel {
    private List<Credit> credits;
    int index = -1;
    public CreditComboBoxModel(List<Credit> credits) {
        this.credits = new ArrayList<>(credits);
    }

    public void setCredits(List<Credit> credits) {
        this.credits = new ArrayList<>(credits);
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
    public Object getElementAt(int index) {
        return credits.get(index);
    }

    @Override
    public void addListDataListener(ListDataListener l) {

    }

    @Override
    public void removeListDataListener(ListDataListener l) {

    }
}
