package org.exaphex.realty.model.ui.table;

import org.exaphex.realty.model.CounterRecord;
import org.exaphex.realty.model.CounterType;
import org.exaphex.realty.model.DocumentType;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class CounterRecordTableModel extends AbstractTableModel {
    private final ResourceBundle res = ResourceBundle.getBundle("i18n");
    private HashMap<String, String> counterTypes = new HashMap<>();
    private List<CounterRecord> counterRecords;

    public CounterRecordTableModel(List<CounterRecord> counterRecords) {
        this.counterRecords = new ArrayList<>(counterRecords);
    }

    public void setCounters(List<CounterRecord> counterRecords, List<CounterType> counterTypes) {
        this.counterRecords = new ArrayList<>(counterRecords);
        populateHashMap(counterTypes);
        fireTableDataChanged();
    }

    private void populateHashMap(List<CounterType> counterTypes) {
        this.counterTypes = new HashMap<>();
        for (CounterType ct : counterTypes) {
            this.counterTypes.put(ct.getId(), ct.getName());
        }
    }

    @Override
    public int getRowCount() {
        return counterRecords.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public String getColumnName(int column) {
        return switch (column) {
            case 0:
                yield res.getString("colDate");
            case 1:
                yield res.getString("colType");
            case 2:
                yield res.getString("colValue");
            case 3:
                yield res.getString("colDescription");
            default:
                yield "";
        };
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        CounterRecord counterRecord = counterRecords.get(rowIndex);
        return switch (columnIndex) {
            case 0:
                yield counterRecord.getDate();
            case 1:
                yield counterTypes.get(counterRecord.getCounterTypeId());
            case 2:
                yield counterRecord.getValue();
            case 3:
                yield counterRecord.getDescription();
            default:
                yield "??";
        };
    }

    public CounterRecord getCounterRecordAt(int row) {
        return counterRecords.get(row);
    }
}
