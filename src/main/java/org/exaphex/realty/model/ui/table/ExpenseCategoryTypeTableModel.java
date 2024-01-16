package org.exaphex.realty.model.ui.table;

import org.exaphex.realty.model.ExpenseCategory;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ExpenseCategoryTypeTableModel extends AbstractTableModel {
    private final ResourceBundle res = ResourceBundle.getBundle("i18n");
    private List<ExpenseCategory> categories;

    public ExpenseCategoryTypeTableModel(List<ExpenseCategory> categories) {
        this.categories = new ArrayList<>(categories);
    }

    public void setCategories(List<ExpenseCategory> categories) {
        this.categories = new ArrayList<>(categories);
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return categories.size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public String getColumnName(int column) {
        return switch (column) {
            case 0:
                yield res.getString("colName");
            case 1:
                yield res.getString("colIsWrapable");
            case 2:
                yield res.getString("colCalculationUnit");
            default:
                yield "";
        };
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ExpenseCategory category = categories.get(rowIndex);
        return switch (columnIndex) {
            case 0:
                yield category.getName();
            case 1:
                yield category.isWrapable();
            case 2:
                yield ExpenseCategory.getExpenseCategoryCalulationTypeById(category.getCalculationType());
            default:
                yield "??";
        };
    }

    public ExpenseCategory getCategoryById(int row) {
        return categories.get(row);
    }

}
