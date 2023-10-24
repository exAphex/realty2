package org.exaphex.realty.model.ui.table;
import org.exaphex.realty.model.Building;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;


public class BuildingTableModel extends AbstractTableModel {

    private List<Building> buildings;

    public BuildingTableModel(List<Building> buildings) {
        this.buildings = new ArrayList<Building>(buildings);
    }

    public void setBuildings(List<Building> buildings) {
        this.buildings = new ArrayList<Building>(buildings);
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return buildings.size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "Name";
            case 1:
                return "Street";
            case 2:
                return "City";
        }
        return "";
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        Object value = "??";
        Building building = buildings.get(rowIndex);
        switch (columnIndex) {
            case 0:
                value = building.getName();
                break;
            case 1:
                value = building.getAddress() + " " + building.getNumber();
                break;
            case 2:
                value = building.getPostalCode() + " " + building.getCity();
                break;
        }
        return value;
    }

    public Building getBuldingAt(int row) {
        return buildings.get(row);
    }

    public void addBuilding(Building b) {
        this.buildings.add(b);
        fireTableDataChanged();
    }

    public void deleteBuilding(Building b) {
        this.buildings.remove(b);
    }

}