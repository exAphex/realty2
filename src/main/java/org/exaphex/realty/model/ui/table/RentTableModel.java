package org.exaphex.realty.model.ui.table;

import org.exaphex.realty.model.Rent;

import javax.swing.table.AbstractTableModel;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class RentTableModel extends AbstractTableModel  {
    private final ResourceBundle res = ResourceBundle.getBundle("i18n");
    private List<Rent> rents;

    public RentTableModel(List<Rent> rents) {
        this.rents = new ArrayList<>(rents);
    }

    public void setRents(List<Rent> rents) {
        this.rents = new ArrayList<>(rents);
        fireTableDataChanged();
    }

    public List<Rent> getRents() {
        return rents;
    }

    @Override
    public int getRowCount() {
        return rents.size();
    }

    @Override
    public int getColumnCount() {
        return 7;
    }

    @Override
    public String getColumnName(int column) {
        return switch (column) {
            case 0:
                yield res.getString("colRenter");
            case 1:
                yield res.getString("colStartDate");
            case 2:
                yield res.getString("colEndDate");
            case 3:
                yield res.getString("colRentalPrice");
            case 4:
                yield res.getString("colExtraCosts");
            case 5:
                yield res.getString("colDeposit");
            case 6:
                yield res.getString("colNumOfTentants");
            default:
                yield "";
        };
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        Rent rent = rents.get(rowIndex);
        return switch (columnIndex) {
            case 0:
                yield rent.getFirstName() + " " + rent.getLastName();
            case 1:
                yield rent.getStartDate();
            case 2:
                yield rent.getEndDate();
            case 3:
                yield formatter.format(rent.getRentalPrice());
            case 4:
                yield formatter.format(rent.getExtraCosts());
            case 5:
                yield formatter.format(rent.getDeposit());
            case 6:
                yield rent.getNumOfTentants();
            default:
                yield "??";
        };
    }

    public Rent getRentAt(int row) {
        return rents.get(row);
    }
}
