package org.exaphex.realty.ui.units;

import org.exaphex.realty.db.service.UnitService;
import org.exaphex.realty.model.Building;
import org.exaphex.realty.model.Unit;
import org.exaphex.realty.model.ui.cmb.UnitComboBoxModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

public class UnitWindow extends JFrame {

    UnitComboBoxModel utm = new UnitComboBoxModel(new ArrayList<>());
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
    private JButton button1;
    private JButton button2;
    private JTable table1;

    public UnitWindow(Building b) {
        super();
        this.building = b;
        buildUI();
        setListeners();
        loadUnits();
    }

    private void buildUI() {
        setTitle(this.building.getName());
        cmbUnits.setModel(utm);
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
        cmbUnits.addItemListener(event -> {
            if (event.getStateChange() == ItemEvent.SELECTED) {
                Unit item = (Unit) event.getItem();
                this.selectedUnit = item;
                setFields(item);
                setTabPanelStatus(true);
            }
        });
    }

    private void setFields(Unit u) {
        this.txtName.setText(u.getName());
    }

    private void loadUnits() {
        List<Unit> units = UnitService.getAllUnits();
        utm.setUnits(units);
    }

    public Building getBuilding() {
        return this.building;
    }

    private void onAddNewUnit() {
        new UnitModal(this);
    }

    private void onDeleteUnit() {

    }

    public void eventAddNewUnit(Unit u) {
        UnitService.addUnit(u);
        loadUnits();
    }

}
