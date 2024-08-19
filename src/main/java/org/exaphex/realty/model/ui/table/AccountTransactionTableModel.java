package org.exaphex.realty.model.ui.table;

import org.exaphex.realty.model.Building;
import org.exaphex.realty.model.Transaction;
import org.exaphex.realty.model.Unit;

import javax.swing.table.AbstractTableModel;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import static org.exaphex.realty.model.Transaction.formatTransactionType;

public class AccountTransactionTableModel extends AbstractTableModel {
    private final ResourceBundle res = ResourceBundle.getBundle("i18n");
    private List<Transaction> transactions;
    private HashMap<String, String> objectNames = new HashMap<>();

    public AccountTransactionTableModel(List<Transaction> transactions) {
        this.transactions = new ArrayList<>(transactions);
    }

    public void setTransactions(List<Transaction> transactions, List<Building> buildings, List<Unit> units) {
        this.transactions = new ArrayList<>(transactions);
        populateHashMap(buildings, units);
        fireTableDataChanged();
    }

    private void populateHashMap(List<Building> buildings, List<Unit> units) {
        this.objectNames = new HashMap<>();
        for (Unit u : units) {
            objectNames.put(u.getId(), u.getName());
        }
        for (Building b : buildings) {
            objectNames.put(b.getId(), b.getName());
        }
    }

    @Override
    public int getRowCount() {
        return transactions.size();
    }

    @Override
    public int getColumnCount() {
        return 5;
    }

    @Override
    public String getColumnName(int column) {
        return switch (column) {
            case 0:
                yield res.getString("colDate");
            case 1:
                yield res.getString("colDescription");
            case 2:
                yield res.getString("colReference");
            case 3:
                yield res.getString("colType");
            case 4:
                yield res.getString("colAmount");
            default:
                yield "";
        };
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        Transaction transaction = transactions.get(rowIndex);
        String referenceName = objectNames.get(transaction.getObjectId());
        return switch (columnIndex) {
            case 0:
                yield transaction.getDate();
            case 1:
                yield transaction.getDescription();
            case 2:
                yield referenceName != null ? referenceName : "";
            case 3:
                yield formatTransactionType(transaction.getType());
            case 4:
                yield transaction.getType() == Transaction.RENT_PAYMENT || transaction.getType() == Transaction.CREDIT_PAYMENT ? formatter.format(transaction.getAmount() + transaction.getSecondaryAmount()) : formatter.format(transaction.getAmount());
            default:
                yield "??";
        };
    }
}
