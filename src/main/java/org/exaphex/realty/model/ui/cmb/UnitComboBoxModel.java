package org.exaphex.realty.model.ui.cmb;

import org.exaphex.realty.model.Unit;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.util.ArrayList;
import java.util.List;

public class UnitComboBoxModel implements ComboBoxModel {

    private List<Unit> units;

    int index = -1;

    public UnitComboBoxModel(List<Unit> units) {
        this.units = new ArrayList<>(units);
    }

    public void setUnits(List<Unit> units) {
        this.units = new ArrayList<>(units);
    }

    @Override
    public void setSelectedItem(Object anItem) {
        for(int i = 0; i < units.size(); i++) {
            if(units.get(i).equals(anItem)) {
                index = i;
                break;
            }
        }
    }

    @Override
    public Object getSelectedItem() {
        if(index >= 0){
            return units.get(index);
        } else {
            return "";
        }
    }

    @Override
    public int getSize() {
        return units.size();
    }

    @Override
    public Object getElementAt(int index) {
        return units.get(index);
    }

    @Override
    public void addListDataListener(ListDataListener l) {

    }

    @Override
    public void removeListDataListener(ListDataListener l) {

    }
}
