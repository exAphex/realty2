package org.exaphex.realty.model.ui.table;
import org.exaphex.realty.model.Building;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;


public class BuildingTableModel extends AbstractTableModel {

    private List<Building> buildings;

    public BuildingTableModel(List<Building> buildings) {
        this.buildings = new ArrayList<>(buildings);
    }

    public void setBuildings(List<Building> buildings) {
        this.buildings = new ArrayList<>(buildings);
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
        return switch (column) {
            case 0:
                yield "Name";
            case 1:
                yield "Street";
            case 2:
                yield "City";
            default:
                yield "";
        };
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object value = "??";
        Building building = buildings.get(rowIndex);
        return switch (columnIndex) {
            case 0:
                yield building.getName();
            case 1:
                yield building.getAddress() + " " + building.getNumber();
            case 2:
                yield building.getPostalCode() + " " + building.getCity();
            default:
                yield "??";
        };
    }

    public Building getBuldingAt(int row) {
        return buildings.get(row);
    }
}