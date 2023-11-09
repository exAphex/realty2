package org.exaphex.realty.model.ui.table;

import org.exaphex.realty.model.Credit;

import javax.swing.table.AbstractTableModel;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CreditTableModel extends AbstractTableModel {
    private List<Credit> credits;

    public CreditTableModel(List<Credit> credits) {
        this.credits = new ArrayList<>(credits);
    }

    public void setCredits(List<Credit> credits) {
        this.credits = new ArrayList<>(credits);
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return credits.size();
    }

    @Override
    public int getColumnCount() {
        return 5;
    }

    @Override
    public String getColumnName(int column) {
        return switch (column) {
            case 0:
                yield "Name";
            case 1:
                yield "Description";
            case 2:
                yield "Start date";
            case 3:
                yield "Interest rate";
            case 4:
                yield "Redemption rate";
            default:
                yield "";
        };
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        DecimalFormat decimalFormatter = new DecimalFormat("##.##%");
        Credit credit = credits.get(rowIndex);
        return switch (columnIndex) {
            case 0:
                yield credit.getName();
            case 1:
                yield credit.getDescription();
            case 2:
                yield credit.getStartDate();
            case 3:
                yield decimalFormatter.format(credit.getInterestRate());
            case 4:
                yield decimalFormatter.format(credit.getRedemptionRate());
            default:
                yield "??";
        };
    }

    public Credit getCreditAt(int row) {
        return credits.get(row);
    }
}
