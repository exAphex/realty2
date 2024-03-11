package org.exaphex.realty.ui.transactions;

import org.exaphex.realty.db.service.TransactionService;
import org.exaphex.realty.model.Building;
import org.exaphex.realty.model.Transaction;
import org.exaphex.realty.model.Unit;
import org.exaphex.realty.model.ui.table.TransactionTableModel;
import org.exaphex.realty.ui.units.TransactionModal;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import static org.exaphex.realty.util.DateUtils.setDateSorter;
import static org.exaphex.realty.util.PriceUtils.setPriceSorter;

public class TransactionPane {
    private final TransactionTableModel ttm = new TransactionTableModel(new ArrayList<>());
    private JButton btnAddTransaction;
    private JButton btnDeleteTransaction;
    private JTable tblAccount;
    private JPanel mainPanel;
    private Unit selectedUnit;
    private Building selectedBuilding;

    public void init() {
        tblAccount.setModel(ttm);
        setupListeners();
        setSorter();
    }

    public void setUI(Unit unit) {
        this.selectedUnit = unit;
        loadData();
    }

    public void setUI(Building building) {
        this.selectedBuilding = building;
        loadData();
    }

    private void setSorter() {
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(tblAccount.getModel());
        List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(0, SortOrder.DESCENDING));
        setDateSorter(sorter, 0);
        setPriceSorter(sorter, 3);
        sorter.setSortKeys(sortKeys);
        sorter.setSortsOnUpdates(true);
        tblAccount.setRowSorter(sorter);
    }

    private void setupListeners() {
        TransactionPane self = this;
        btnAddTransaction.addActionListener(e -> this.onAddTransaction());
        btnDeleteTransaction.addActionListener(e -> this.onDeleteTransaction());
        tblAccount.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouseEvent) {
                JTable table = (JTable) mouseEvent.getSource();
                int selectedRow = table.getSelectedRow();
                if (mouseEvent.getClickCount() == 2 && selectedRow != -1) {
                    Transaction transaction = ttm.getTransactionAt(tblAccount.convertRowIndexToModel(selectedRow));
                    new TransactionModal(self, transaction);
                }
            }
        });
    }

    private void onAddTransaction() {
        if (this.selectedBuilding != null) {
            new TransactionModal(this, this.selectedBuilding);
        } else {
            new TransactionModal(this, this.selectedUnit);
        }
    }

    public void loadData() {
        List<Transaction> transactions;
        if (this.selectedBuilding != null) {
            transactions = TransactionService.getTransactions(this.selectedBuilding);
        } else {
            transactions = TransactionService.getTransactions(this.selectedUnit);
        }
        ttm.setTransactions(transactions);
    }

    private void onDeleteTransaction() {
        int[] selectedRows = tblAccount.getSelectedRows();
        for (int i : selectedRows) {
            Transaction transaction = ttm.getTransactionAt(tblAccount.convertRowIndexToModel(i));
            TransactionService.deleteTransaction(transaction);
        }
        loadData();
    }

    public void eventEditTransaction(Transaction t) {
        TransactionService.updateTransaction(t);
        loadData();
    }

    public void eventAddNewTransaction(Transaction t) {
        TransactionService.addTransaction(t);
        loadData();
    }
}
