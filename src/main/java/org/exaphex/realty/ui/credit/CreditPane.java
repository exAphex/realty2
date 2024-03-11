package org.exaphex.realty.ui.credit;

import org.exaphex.realty.db.service.CreditService;
import org.exaphex.realty.model.Building;
import org.exaphex.realty.model.Credit;
import org.exaphex.realty.model.Unit;
import org.exaphex.realty.model.ui.table.CreditTableModel;
import org.exaphex.realty.ui.units.CreditModal;
import org.exaphex.realty.ui.units.PaymentCheckWindow;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class CreditPane {
    private final CreditTableModel ctm = new CreditTableModel(new ArrayList<>());
    private JPanel mainPanel;
    private JButton btnAddCredit;
    private JButton btnDeleteCredit;
    private JButton btnCheckCredit;
    private JTable tblCredit;
    private Building selectedBuilding;
    private Unit selectedUnit;

    public void setUI(Building building) {
        this.selectedBuilding = building;
        _setUI();
    }

    public void setUI(Unit unit) {
        this.selectedUnit = unit;
        _setUI();
    }

    private void _setUI() {
        tblCredit.setModel(ctm);
        setupListeners();
        loadData();
    }

    private void setupListeners() {
        btnAddCredit.addActionListener(e -> this.onAddNewCredit());
        btnDeleteCredit.addActionListener(e -> this.onDeleteCredit());
    }

    private void loadData() {
        List<Credit> credits;
        if (this.selectedBuilding != null) {
            credits = CreditService.getCredit(this.selectedBuilding);
        } else {
            credits = CreditService.getCredit(this.selectedUnit);
        }
        ctm.setCredits(credits);
    }

    private void onAddNewCredit() {
        if (this.selectedBuilding != null) {
            new CreditModal(this, this.selectedBuilding);
        } else {
            new CreditModal(this, this.selectedUnit);
        }
    }

    private void onCheckCredit() {
        if (tblCredit.getSelectedRow() == -1)
            return;

        Credit credit = ctm.getCreditAt(tblCredit.convertRowIndexToModel(tblCredit.getSelectedRow()));
        new PaymentCheckWindow(this, this.selectedUnit, credit);
    }

    private void onDeleteCredit() {
        int[] selectedRows = tblCredit.getSelectedRows();
        for (int i : selectedRows) {
            Credit credit = ctm.getCreditAt(tblCredit.convertRowIndexToModel(i));
            CreditService.deleteCredit(credit);
        }
        loadData();
    }

    public void eventAddNewCredit(Credit c) {
        CreditService.addCredit(c);
        loadData();
    }
}
