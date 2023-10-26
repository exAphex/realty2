package org.exaphex.realty.ui.units;

import com.opencsv.bean.CsvToBeanBuilder;
import org.exaphex.realty.db.service.ReceivableService;
import org.exaphex.realty.db.service.RentService;
import org.exaphex.realty.db.service.UnitService;
import org.exaphex.realty.db.service.ValuationService;
import org.exaphex.realty.model.*;
import org.exaphex.realty.model.transport.ValuationTransportModel;
import org.exaphex.realty.model.ui.cmb.UnitComboBoxModel;
import org.exaphex.realty.model.ui.table.ReceiveableTableModel;
import org.exaphex.realty.model.ui.table.RentTableModel;
import org.exaphex.realty.model.ui.table.ValuationTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class UnitWindow extends JFrame {

    UnitComboBoxModel utm = new UnitComboBoxModel(new ArrayList<>());
    ValuationTableModel vtm = new ValuationTableModel(new ArrayList<>());
    RentTableModel rtm = new RentTableModel(new ArrayList<>());
    ReceiveableTableModel rvtm = new ReceiveableTableModel(new ArrayList<>());
    Unit selectedUnit;
    private final Building building;
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
    private JButton btnAddRent;
    private JButton btnDeleteRent;
    private JTable tblRents;
    private JTable tblAccount;
    private JPanel paneAccount;

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
        tblRents.setModel(rtm);
        tblAccount.setModel(rvtm);
        setContentPane(mainPanel);
        setTabPanelStatus(false);
    }

    void setTabPanelStatus(Boolean isEnabled) {
        tabPane.setEnabled(isEnabled);
        setPanelEnabled(paneGeneral, isEnabled);
        setPanelEnabled(paneRent, isEnabled);
        setPanelEnabled(paneValuation, isEnabled);
        setPanelEnabled(paneAccount, isEnabled);
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
        btnAddRent.addActionListener(e -> this.onAddNewRent());
        btnDeleteRent.addActionListener(e -> this.onDeleteRent());

        cmbUnits.addItemListener(event -> {
            if (event.getStateChange() == ItemEvent.SELECTED) {
                Unit item = (Unit) event.getItem();
                selectUnit(item);
            }
        });
    }

    private void selectUnit(Unit u) {
        this.selectedUnit = u;
        setFields(u);
        setTabPanelStatus(true);
        loadValuations(this.selectedUnit);
        loadRents(this.selectedUnit);
        loadReceivables(this.selectedUnit);
    }

    private void setFields(Unit u) {
        this.txtName.setText(u != null ? u.getName() : "");
    }

    private void loadUnits(Building b) {
        List<Unit> units = UnitService.getUnits(b);
        utm.setUnits(units);
    }

    private void loadValuations(Unit u) {
        List<Valuation> valuations = ValuationService.getValuations(u);
        vtm.setValuations(valuations);
    }

    private void loadRents(Unit u) {
        List<Rent> rents = RentService.getRents(u);
        rtm.setRents(rents);
    }

    private void loadReceivables(Unit u) {
        List<Receivable> receivables = ReceivableService.getReceivables(u);
        rvtm.setReceivables(receivables);
    }

    public Building getBuilding() {
        return this.building;
    }

    private void onAddNewUnit() {
        new UnitModal(this, this.building);
    }

    private void onAddNewRent() {
        new RentModal(this, this.selectedUnit);
    }

    private void onAddNewValuation() {
        new ValuationModal(this, this.selectedUnit);
    }

    private void onImportValuation() {
        JFileChooser chooser = new JFileChooser();
        int retOption = chooser.showOpenDialog(null);
        if (retOption == JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();
            try {
                List<ValuationTransportModel> valuations = new CsvToBeanBuilder(new FileReader(f))
                        .withSeparator(';')
                        .withType(ValuationTransportModel.class)
                        .build()
                        .parse();
                for (ValuationTransportModel v : valuations) {
                    try {
                        ValuationService.addValuation(v.getValuation(this.selectedUnit.getId()));
                    } catch (Exception e) {
                        // TODO: proper exception
                    }
                }
                loadValuations(this.selectedUnit);
            } catch (FileNotFoundException e) {
                // TODO: error handling
            }
        }
    }

    private void onDeleteValuation() {
        if (tblValuations.getSelectedRow() == -1)
            return;

        Valuation valuation = vtm.getValuationAt(tblValuations.getSelectedRow());
        ValuationService.deleteValuation(valuation);
        loadValuations(this.selectedUnit);
    }

    private void onDeleteRent() {
        if (tblRents.getSelectedRow() == -1)
            return;

        Rent rent = rtm.getRentAt(tblRents.getSelectedRow());
        RentService.deleteRent(rent);
        loadRents(this.selectedUnit);
        loadReceivables(this.selectedUnit);
    }

    private void onDeleteUnit() {
        if (this.selectedUnit == null) { return;}
        UnitService.deleteUnit(this.selectedUnit);

        this.selectedUnit = null;
        utm = new UnitComboBoxModel(new ArrayList<>());
        cmbUnits.setModel(utm);
        setTabPanelStatus(false);
        setFields(null);
        tabPane.setSelectedIndex(0);

        loadUnits(this.building);
    }

    public void eventAddNewUnit(Unit u) {
        UnitService.addUnit(u);
        loadUnits(this.building);

        utm.setSelectedItem(u);
        selectUnit(u);
    }

    public void eventAddNewValuation(Valuation v) {
        ValuationService.addValuation(v);
        loadValuations(this.selectedUnit);
    }

    public void eventAddNewRent(Rent r) {
        RentService.addRent(r);
        loadRents(this.selectedUnit);
        loadReceivables(this.selectedUnit);
    }


}
