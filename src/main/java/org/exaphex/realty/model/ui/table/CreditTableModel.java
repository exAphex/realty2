package org.exaphex.realty.model.ui.table;

import org.exaphex.realty.model.Credit;

import javax.swing.table.AbstractTableModel;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CreditTableModel extends AbstractTableModel {
    private final ResourceBundle res = ResourceBundle.getBundle("i18n");
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
                yield res.getString("colName");
            case 1:
                yield res.getString("colDescription");
            case 2:
                yield res.getString("colAmount");
            case 3:
                yield res.getString("colRemained");
            case 4:
                yield res.getString("colStartDate");
            case 5:
                yield res.getString("colEndDate");
            case 6:
                yield res.getString("colInterestRate");
            case 7:
                yield res.getString("colRedemptionRate");
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
