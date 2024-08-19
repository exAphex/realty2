package org.exaphex.realty.ui.counters;

import org.exaphex.realty.db.service.CounterRecordService;
import org.exaphex.realty.db.service.CounterTypeService;
import org.exaphex.realty.model.*;
import org.exaphex.realty.model.ui.table.CounterRecordTableModel;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CounterPane {
    private final ResourceBundle res = ResourceBundle.getBundle("i18n");
    private final CounterRecordTableModel crtm = new CounterRecordTableModel(new ArrayList<>());
    private JButton btnAdd;
    private JButton btnDelete;
    private JTable tblCounterRecords;
    private JPanel mainPanel;
    private Building selectedBuilding;
    private Unit selectedUnit;

    public void init() {
        tblCounterRecords.setModel(crtm);
        setupListeners();
    }

    public void setUI(Building building) {
        this.selectedBuilding = building;
        loadData();
    }

    public void setUI(Unit unit) {
        this.selectedUnit = unit;
        loadData();
    }

    private void setupListeners() {
        CounterPane cp = this;
        btnAdd.addActionListener(e -> this.onAddNewCounterRecord());
        btnDelete.addActionListener(e -> this.onDeleteCounterRecord());
        tblCounterRecords.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouseEvent) {
                JTable table = (JTable) mouseEvent.getSource();
                int selectedRow = table.getSelectedRow();
                if (mouseEvent.getClickCount() == 2 && selectedRow != -1) {
                    CounterRecord counterRecord = crtm.getCounterRecordAt(tblCounterRecords.convertRowIndexToModel(selectedRow));
                    new CounterModal(cp, counterRecord);
                }
            }
        });
    }

    private void loadData() {
        List<CounterRecord> counterRecords;
        List<CounterType> counterTypes = CounterTypeService.getCounterTypes();
        if (this.selectedBuilding != null) {
            counterRecords = CounterRecordService.getCounterRecords(this.selectedBuilding);
        } else {
            counterRecords = CounterRecordService.getCounterRecords(this.selectedUnit);
        }
        crtm.setCounters(counterRecords, counterTypes);
    }

    private void onAddNewCounterRecord() {
        if (this.selectedBuilding != null) {
            new CounterModal(this, this.selectedBuilding);
        } else {
            new CounterModal(this, this.selectedUnit);
        }
    }

    public void eventEditCounterRecord(CounterRecord counterRecord) {
        CounterRecordService.updateCounterRecord(counterRecord);
        loadData();
    }

    private void onDeleteCounterRecord() {
        int[] selectedRows = tblCounterRecords.getSelectedRows();
        for (int i : selectedRows) {
            CounterRecord counterRecord = crtm.getCounterRecordAt(tblCounterRecords.convertRowIndexToModel(i));
            CounterRecordService.deleteCounterRecord(counterRecord);
        }
        loadData();
    }

    public void eventAddNewCounterRecord(CounterRecord counterRecord) {
        CounterRecordService.addCounterRecord(counterRecord);
        loadData();
    }
}
