package org.exaphex.realty.model.ui.cmb;

import org.exaphex.realty.model.Rent;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.util.ArrayList;
import java.util.List;

public class RentComboBoxModel implements ComboBoxModel<Rent> {
    private List<Rent> rents;
    int index = -1;
    public RentComboBoxModel(List<Rent> rents) {
        this.rents = new ArrayList<>(rents);
    }

    public void setRents(List<Rent> rents) {
        this.rents = new ArrayList<>(rents);
    }

    @Override
    public void setSelectedItem(Object anItem) {
        for(int i = 0; i < rents.size(); i++) {
            if(rents.get(i).equals(anItem)) {
                index = i;
                break;
            }
        }
    }

    @Override
    public Object getSelectedItem() {
        if(index >= 0){
            return rents.get(index);
        } else {
            return "";
        }
    }

    public int getRentIndexById(String id) {
        if (id == null) {
            return -1;
        }
        for (int i = 0; i < rents.size(); i++) {
            if (id.equals(rents.get(i).getId())) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int getSize() {
        return rents.size();
    }

    @Override
    public Rent getElementAt(int index) {
        return rents.get(index);
    }

    @Override
    public void addListDataListener(ListDataListener l) {

    }

    @Override
    public void removeListDataListener(ListDataListener l) {

    }
}
