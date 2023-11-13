package org.exaphex.realty.model.ui.table;

import org.exaphex.realty.model.Credit;

import javax.swing.table.AbstractTableModel;
import java.text.DecimalFormat;
import java.text.NumberFormat;
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
        return 8;
    }

    @Override
    public String getColumnName(int column) {
        return switch (column) {
            case 0:
                yield "Name";
            case 1:
                yield "Description";
            case 2:
                yield "Amount";
            case 3:
                yield "Remained";
            case 4:
                yield "Start date";
            case 5:
                yield "End date";
            case 6:
                yield "Interest rate";
            case 7:
                yield "Redemption rate";
            default:
                yield "";
        };
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        DecimalFormat decimalFormatter = new DecimalFormat("##.##%");
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        Credit credit = credits.get(rowIndex);
        return switch (columnIndex) {
            case 0:
                yield credit.getName();
            case 1:
                yield credit.getDescription();
            case 2:
                yield formatter.format(credit.getAmount());
            case 3:
                yield formatter.format(credit.getAmount() - credit.getRepaidAmount());
            case 4:
                yield credit.getStartDate();
            case 5:
                yield credit.getEndDate();
            case 6:
                yield decimalFormatter.format(credit.getInterestRate());
            case 7:
                yield decimalFormatter.format(credit.getRedemptionRate());
            default:
                yield "??";
        };
    }

    public Credit getCreditAt(int row) {
        return credits.get(row);
    }

    public List<Credit> getCredits() {
        return credits;
    }
}
