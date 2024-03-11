package org.exaphex.realty.model.ui.cmb;

import org.exaphex.realty.model.ExpenseCategory;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.util.ArrayList;
import java.util.List;

public class ExpenseCategoryComboBoxModel implements ComboBoxModel<ExpenseCategory> {
    private List<ExpenseCategory> expenseCategories;
    int index = -1;
    public ExpenseCategoryComboBoxModel(List<ExpenseCategory> credits) {
        this.expenseCategories = new ArrayList<>(credits);
    }

    public void setExpenseCategories(List<ExpenseCategory> expenseCategories) {
        this.expenseCategories = new ArrayList<>(expenseCategories);
    }

    public int getExpenseCategoryIndexById(String id) {
        if (id == null) {
            return -1;
        }
        for (int i = 0; i < expenseCategories.size(); i++) {
            if (id.equals(expenseCategories.get(i).getId())) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void setSelectedItem(Object anItem) {
        for(int i = 0; i < expenseCategories.size(); i++) {
            if(expenseCategories.get(i).equals(anItem)) {
                index = i;
                break;
            }
        }
    }

    @Override
    public Object getSelectedItem() {
        if(index >= 0){
            return expenseCategories.get(index);
        } else {
            return "";
        }
    }

    @Override
    public int getSize() {
        return expenseCategories.size();
    }

    @Override
    public ExpenseCategory getElementAt(int index) {
        return expenseCategories.get(index);
    }

    @Override
    public void addListDataListener(ListDataListener l) {

    }

    @Override
    public void removeListDataListener(ListDataListener l) {

    }
}
