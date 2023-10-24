package org.exaphex.realty.model.ui.table;

import org.exaphex.realty.model.Valuation;

import javax.swing.table.AbstractTableModel;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class ValuationTableModel extends AbstractTableModel {

    private List<Valuation> valuations;

    public ValuationTableModel(List<Valuation> valuations) {
        this.valuations = new ArrayList<>(valuations);
    }

    public void setValuations(List<Valuation> valuations) {
        this.valuations = new ArrayList<>(valuations);
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
        return switch (column) {
            case 0:
                yield "Date";
            case 1:
                yield "Value";
            default:
                yield "";
        };
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Valuation val = valuations.get(rowIndex);
        return switch (columnIndex) {
            case 0:
                yield val.getDate();
            case 1:
                NumberFormat formatter = NumberFormat.getCurrencyInstance();
                yield formatter.format(val.getValue());
            default:
                yield "??";
        };
    }

    public Valuation getValuationAt(int row) {
        return valuations.get(row);
    }
}
