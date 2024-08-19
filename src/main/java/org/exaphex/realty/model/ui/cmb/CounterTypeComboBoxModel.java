package org.exaphex.realty.model.ui.cmb;

import org.exaphex.realty.model.CounterType;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.util.ArrayList;
import java.util.List;

public class CounterTypeComboBoxModel implements ComboBoxModel<CounterType> {
    private List<CounterType> counterTypes;
    int index = -1;
    public CounterTypeComboBoxModel(List<CounterType> counterTypes) {
        this.counterTypes = new ArrayList<>(counterTypes);
    }

    public void setCounterTypes(List<CounterType> counterTypes) {
        this.counterTypes = new ArrayList<>(counterTypes);
    }

    public int getCounterTypeByIndex(String id) {
        if (id == null) {
            return -1;
        }
        for (int i = 0; i < counterTypes.size(); i++) {
            if (id.equals(counterTypes.get(i).getId())) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void setSelectedItem(Object anItem) {
        for(int i = 0; i < counterTypes.size(); i++) {
            if(counterTypes.get(i).equals(anItem)) {
                index = i;
                break;
            }
        }
    }

    @Override
    public Object getSelectedItem() {
        if(index >= 0){
            return counterTypes.get(index);
        } else {
            return "";
        }
    }

    @Override
    public int getSize() {
        return counterTypes.size();
    }

    @Override
    public CounterType getElementAt(int index) {
        return counterTypes.get(index);
    }

    @Override
    public void addListDataListener(ListDataListener l) {

    }

    @Override
    public void removeListDataListener(ListDataListener l) {

    }
}
