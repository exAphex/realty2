package org.exaphex.realty.model.ui.table;

import org.exaphex.realty.model.Receivable;

import javax.swing.table.AbstractTableModel;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import static org.exaphex.realty.model.Transaction.formatTransactionType;

public class ReceiveableTableModel extends AbstractTableModel {
    private List<Receivable> receivables;

    public ReceiveableTableModel(List<Receivable> receivables) {
        this.receivables = new ArrayList<>(receivables);
    }

    public void setReceivables(List<Receivable> receivables) {
        this.receivables = new ArrayList<>(receivables);
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return receivables.size();
    }

    @Override
    public int getColumnCount() {
        return 6;
    }

    @Override
    public String getColumnName(int column) {
        return switch (column) {
            case 0:
                yield "Name";
            case 1:
                yield "Due date";
            case 2:
                yield "Type";
            case 3:
                yield "Amount";
            case 4:
                yield "Received on";
            case 5:
                yield "Received amount";
            default:
                yield "";
        };
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        Receivable receivable = receivables.get(rowIndex);
        return switch (columnIndex) {
            case 0:
                yield receivable.getName();
            case 1:
                yield receivable.getDue();
            case 2:
                yield formatTransactionType(receivable.getType());
            case 3:
                yield formatter.format(receivable.getAmount());
            case 4:
                yield (receivable.getTransaction() != null ? receivable.getTransaction().getDate() : "-");
            case 5:
                yield (receivable.getTransaction() != null ? formatter.format(receivable.getTransaction().getAmount()) : "-");
            default:
                yield "??";
        };
    }

}