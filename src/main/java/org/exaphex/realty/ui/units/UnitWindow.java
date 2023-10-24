package org.exaphex.realty.ui.units;

import org.exaphex.realty.db.service.UnitService;
import org.exaphex.realty.db.service.ValuationService;
import org.exaphex.realty.model.Building;
import org.exaphex.realty.model.Unit;
import org.exaphex.realty.model.Valuation;
import org.exaphex.realty.model.ui.cmb.UnitComboBoxModel;
import org.exaphex.realty.model.ui.table.ValuationTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.List;

public class UnitWindow extends JFrame {

    UnitComboBoxModel utm = new UnitComboBoxModel(new ArrayList<>());
    ValuationTableModel vtm = new ValuationTableModel(new ArrayList<>());
    Unit selectedUnit;
    private Building building;
    private JComboBox cmbUnits;
    private JButton btnAddUnit;
    private JButton btnDeleteUnit;
    private JTabbedPane tabPane;
    private JTextField txtName;
    private JTextField textField2;
    private JButton saveButton;
    private JPanel mainPanel;
    private JPanel paneGeneral;
    private JPanel paneRent;
    private JPanel paneValuation;
    private JButton btnAddValuation;
    private JButton btnDeleteValuation;
    private JTable tblValuations;
    private JButton btnImportValuation;

    public UnitWindow(Building b) {
        super();
        this.building = b;
        buildUI();
        setListeners();
        loadUnits(this.building);
    }

    private void buildUI() {
        setTitle(this.building.getName());
        cmbUnits.setModel(utm);
        tblValuations.setModel(vtm);
        setContentPane(mainPanel);
        setTabPanelStatus(false);
    }

    void setTabPanelStatus(Boolean isEnabled) {
        tabPane.setEnabled(isEnabled);
        setPanelEnabled(paneGeneral,isEnabled);
        setPanelEnabled(paneRent,isEnabled);
        setPanelEnabled(paneValuation,isEnabled);
    }

    void setPanelEnabled(JPanel panel, Boolean isEnabled) {
        panel.setEnabled(isEnabled);
        Component[] components = panel.getComponents();
        for (Component component : components) {
            if (component instanceof JPanel) {
                setPanelEnabled((JPanel) component, isEnabled);
            }
            component.setEnabled(isEnabled);
        }
    }

    private void setListeners() {
        btnAddUnit.addActionListener(e -> this.onAddNewUnit());
        btnDeleteUnit.addActionListener(e -> this.onDeleteUnit());
        btnAddValuation.addActionListener(e -> this.onAddNewValuation());
        btnDeleteValuation.addActionListener(e -> this.onDeleteValuation());
        btnImportValuation.addActionListener(e -> this.onImportValuation());

        cmbUnits.addItemListener(event -> {
            if (event.getStateChange() == ItemEvent.SELECTED) {
                Unit item = (Unit) event.getItem();
                this.selectedUnit = item;
                setFields(item);
                setTabPanelStatus(true);
                loadValuations(this.selectedUnit);
            }
        });
    }

    private void setFields(Unit u) {
        this.txtName.setText(u.getName());
    }

    private void loadUnits(Building b) {
        List<Unit> units = UnitService.getUnits(b);
        utm.setUnits(units);
    }

    private void loadValuations(Unit u) {
        List<Valuation> valuations = ValuationService.getValuations(u);
        vtm.setValuations(valuations);
    }

    public Building getBuilding() {
        return this.building;
    }

    private void onAddNewUnit() {
        new UnitModal(this, this.building);
    }

    private void onAddNewValuation() {
        new ValuationModal(this, this.selectedUnit);
    }

    private void onImportValuation() {
        JFileChooser chooser = new JFileChooser();
        chooser.showOpenDialog(null);
        // TODO: Import data
    }

    private void onDeleteValuation() {
        if (tblValuations.getSelectedRow() == -1)
            return;

        Valuation valuation = vtm.getValuationAt(tblValuations.getSelectedRow());
        ValuationService.deleteValuation(valuation);
        loadValuations(this.selectedUnit);
    }

    private void onDeleteUnit() {

    }

    public void eventAddNewUnit(Unit u) {
        UnitService.addUnit(u);
        loadUnits(this.building);
    }

    public void eventAddNewValuation(Valuation v) {
        ValuationService.addValuation(v);
        loadValuations(this.selectedUnit);
    }

}
