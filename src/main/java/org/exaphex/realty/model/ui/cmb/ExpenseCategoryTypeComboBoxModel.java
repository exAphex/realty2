package org.exaphex.realty.model.ui.cmb;

import org.exaphex.realty.model.ExpenseCategory;
import org.exaphex.realty.model.ExpenseCategoryCalculationType;

import javax.swing.*;
import javax.swing.event.ListDataListener;

public class ExpenseCategoryTypeComboBoxModel implements ComboBoxModel<ExpenseCategoryCalculationType>  {

    private ExpenseCategoryCalculationType[] options;
    int index = -1;
    public ExpenseCategoryTypeComboBoxModel() {
        this.options = ExpenseCategory.calculationTypes;
    }

    public int getCategoryById(int id) {
        for (int i = 0; i < options.length; i++) {
            if (id == options[i].getId()) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void setSelectedItem(Object anItem) {
        for(int i = 0; i < options.length; i++) {
            if(options[i].equals(anItem)) {
                index = i;
                break;
            }
        }
    }

    @Override
    public Object getSelectedItem() {
        if(index >= 0){
            return options[index];
        } else {
            return "";
        }
    }

    @Override
    public int getSize() {
        return options.length;
    }

    @Override
    public ExpenseCategoryCalculationType getElementAt(int index) {
        return options[index];
    }

    @Override
    public void addListDataListener(ListDataListener l) {

    }

    @Override
    public void removeListDataListener(ListDataListener l) {

    }
}
