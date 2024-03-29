package org.exaphex.realty.model.ui.table;
import org.exaphex.realty.model.Building;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.table.AbstractTableModel;


public class BuildingTableModel extends AbstractTableModel {

    private final ResourceBundle res = ResourceBundle.getBundle("i18n");
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
        return 4;
    }

    @Override
    public String getColumnName(int column) {
        return switch (column) {
            case 0:
                yield res.getString("colName");
            case 1:
                yield res.getString("colAddress");
            case 2:
                yield res.getString("colCity");
            case 3:
                yield res.getString("colTotalArea");
            default:
                yield "";
        };
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Building building = buildings.get(rowIndex);
        return switch (columnIndex) {
            case 0:
                yield building.getName();
            case 1:
                yield building.getAddress() + " " + building.getNumber();
            case 2:
                yield building.getPostalCode() + " " + building.getCity();
            case 3:
                yield building.getTotalArea() + " m²";
            default:
                yield "??";
        };
    }

    public Building getBuildingAt(int row) {
        return buildings.get(row);
    }
}