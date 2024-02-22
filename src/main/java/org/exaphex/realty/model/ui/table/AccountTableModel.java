package org.exaphex.realty.model.ui.table;

import org.exaphex.realty.model.Account;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AccountTableModel extends AbstractTableModel {
    private final ResourceBundle res = ResourceBundle.getBundle("i18n");
    private List<Account> accounts;

    public AccountTableModel(List<Account> accounts) {
        this.accounts = new ArrayList<>(accounts);
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = new ArrayList<>(accounts);
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return accounts.size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public String getColumnName(int column) {
        return switch (column) {
            case 0:
                yield res.getString("colName");
            case 1:
                yield res.getString("colIBAN");
            case 2:
                yield res.getString("colBIC");
            default:
                yield "";
        };
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Account account = accounts.get(rowIndex);
        return switch (columnIndex) {
            case 0:
                yield account.getName();
            case 1:
                yield account.getIban();
            case 2:
                yield account.getBic();
            default:
                yield "??";
        };
    }

    public Account getAccountAt(int row) {
        return accounts.get(row);
    }
}
