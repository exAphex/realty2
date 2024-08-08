package org.exaphex.realty.model.ui.table;

import org.exaphex.realty.model.CounterType;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CounterTypeTableModel extends AbstractTableModel {
    private final ResourceBundle res = ResourceBundle.getBundle("i18n");
    private List<CounterType> counterTypes;

    public CounterTypeTableModel(List<CounterType> counterTypes) {
        this.counterTypes = new ArrayList<>(counterTypes);
    }

    public void setCounterTypes(List<CounterType> counterTypes) {
        this.counterTypes = new ArrayList<>(counterTypes);
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return counterTypes.size();
    }

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public String getColumnName(int column) {
        return column == 0 ? res.getString("colName") : "";
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        CounterType counterType = counterTypes.get(rowIndex);
        return columnIndex == 0 ? counterType.getName() : "??";
    }

    public CounterType getDocumentTypeById(int row) {
        return counterTypes.get(row);
    }

}
