package org.exaphex.realty.model.ui.table;

import org.exaphex.realty.model.DocumentType;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DocumentTypeTableModel extends AbstractTableModel {
    private final ResourceBundle res = ResourceBundle.getBundle("i18n");
    private List<DocumentType> documentTypes;

    public DocumentTypeTableModel(List<DocumentType> documentTypes) {
        this.documentTypes = new ArrayList<>(documentTypes);
    }

    public void setDocumentTypes(List<DocumentType> documentTypes) {
        this.documentTypes = new ArrayList<>(documentTypes);
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return documentTypes.size();
    }

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public String getColumnName(int column) {
        return column == 0 ? res.getString("colName") : "";
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        DocumentType category = documentTypes.get(rowIndex);
        return columnIndex == 0 ? category.getName() : "??";
    }

    public DocumentType getDocumentTypeById(int row) {
        return documentTypes.get(row);
    }

}
