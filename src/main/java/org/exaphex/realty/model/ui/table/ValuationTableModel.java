package org.exaphex.realty.model.ui.table;

import org.exaphex.realty.model.Building;
import org.exaphex.realty.model.Valuation;

import javax.swing.table.AbstractTableModel;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class ValuationTableModel extends AbstractTableModel {

    private List<Valuation> valuations;

    public ValuationTableModel(List<Valuation> valuations) {
        this.valuations = new ArrayList<Valuation>(valuations);
    }

    public void setValuations(List<Valuation> valuations) {
        this.valuations = new ArrayList<Valuation>(valuations);
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return valuations.size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "Date";
            case 1:
                return "Value";
        }
        return "";
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        Object value = "??";
        Valuation val = valuations.get(rowIndex);
        switch (columnIndex) {
            case 0:
                value = val.getDate();
                break;
            case 1:
                NumberFormat formatter = NumberFormat.getCurrencyInstance();
                value = formatter.format(val.getValue());
                break;
        }
        return value;
    }

    public Valuation getValuationAt(int row) {
        return valuations.get(row);
    }

    public void addValuation(Valuation v) {
        this.valuations.add(v);
        fireTableDataChanged();
    }

    public void deleteBuilding(Building b) {
        this.valuations.remove(b);
    }
}
