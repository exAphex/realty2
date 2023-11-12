package org.exaphex.realty.model.ui.table;

import org.exaphex.realty.model.Transaction;

import javax.swing.table.AbstractTableModel;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import static org.exaphex.realty.model.Transaction.formatTransactionType;

public class TransactionTableModel extends AbstractTableModel {
    private List<Transaction> transactions;

    public TransactionTableModel(List<Transaction> transactions) {
        this.transactions = new ArrayList<>(transactions);
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = new ArrayList<>(transactions);
        fireTableDataChanged();
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    @Override
    public int getRowCount() {
        return transactions.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public String getColumnName(int column) {
        return switch (column) {
            case 0:
                yield "Date";
            case 1:
                yield "Description";
            case 2:
                yield "Type";
            case 3:
                yield "Amount";
            default:
                yield "";
        };
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        Transaction transaction = transactions.get(rowIndex);
        return switch (columnIndex) {
            case 0:
                yield transaction.getDate();
            case 1:
                yield transaction.getDescription();
            case 2:
                yield formatTransactionType(transaction.getType());
            case 3:
                yield transaction.getType() == Transaction.RENT_PAYMENT || transaction.getType() == Transaction.CREDIT_PAYMENT ? formatter.format(transaction.getAmount() + transaction.getSecondaryAmount()) : formatter.format(transaction.getAmount());
            default:
                yield "??";
        };
    }

    public Transaction getTransactionAt(int row) {
        return transactions.get(row);
    }
}
