package org.exaphex.realty.ui;

import org.exaphex.realty.db.service.BuildingService;
import org.exaphex.realty.model.Building;
import org.exaphex.realty.model.ui.table.BuildingTableModel;
import org.exaphex.realty.ui.buildings.BuildingModal;
import org.exaphex.realty.ui.units.UnitWindow;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class MainWindow extends JFrame {

    BuildingTableModel btm = new BuildingTableModel(new ArrayList<>());

    List<UnitWindow> uw = new ArrayList<>();
    private JTabbedPane tabbedPane1;
    private JPanel mainPanel;
    private JButton addButton;
    private JButton deleteButton;
    private JTable buildingsTable ;

    public MainWindow() {
        setContentPane(this.mainPanel);
        buildingsTable.setModel(btm);
        setListeners();
        loadBuildings();
    }

    public void setListeners() {
        addButton.addActionListener(e -> this.onAddNewBuilding());
        deleteButton.addActionListener(e -> this.onDeleteBuilding());
        buildingsTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouseEvent) {
                JTable table = (JTable) mouseEvent.getSource();
                int selectedRow = table.getSelectedRow();
                if (mouseEvent.getClickCount() == 2 && selectedRow != -1) {
                    int selectedModelRow = buildingsTable.convertColumnIndexToModel(selectedRow);
                    Building selectedBuilding = ((BuildingTableModel) buildingsTable.getModel()).getBuldingAt(selectedModelRow);
                    createBuildingDetailView(selectedBuilding);
                }
            }
        });
    }

    public void onAddNewBuilding() {
        new BuildingModal(this);
    }

    public void onDeleteBuilding() {
        if (buildingsTable.getSelectedRow() == -1)
            return;

        Building building = btm.getBuldingAt(buildingsTable.getSelectedRow());
        int dialogResult = JOptionPane.showConfirmDialog(null,
                "Are you sure you want to delete " + building.getName() + "?",
                "Warning", JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.YES_OPTION) {
            BuildingService.deleteBuilding(building);
            loadBuildings();
        }
    }

    public void eventAddNewBuilding(Building b) {
        BuildingService.addBuilding(b);
        loadBuildings();
    }

    public void loadBuildings() {
        List<Building> buildings = BuildingService.getAllBuildings();
        btm.setBuildings(buildings);
    }

    private void createBuildingDetailView(Building b) {
        for (UnitWindow db : uw) {
            if (db.getBuilding().equals(b)) {
                db.setVisible(true);
                return;
            }
        }

        UnitWindow u = new UnitWindow(b);
        u.pack();
        u.setLocationRelativeTo(null);
        u.setVisible(true);
        this.uw.add(u);
    }
}
