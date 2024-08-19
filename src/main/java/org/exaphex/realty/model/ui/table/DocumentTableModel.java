package org.exaphex.realty.model.ui.table;

import org.exaphex.realty.model.Document;
import org.exaphex.realty.model.DocumentType;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class DocumentTableModel extends AbstractTableModel {
    private final ResourceBundle res = ResourceBundle.getBundle("i18n");
    private List<Document> documents;
    private HashMap<String, String> documentTypes = new HashMap<>();

    public DocumentTableModel(List<Document> documents) {
        this.documents = new ArrayList<>(documents);
    }

    public void setDocuments(List<Document> documents, List<DocumentType> documentTypes) {
        this.documents = new ArrayList<>(documents);
        populateHashMap(documentTypes);
        fireTableDataChanged();
    }

    private void populateHashMap(List<DocumentType> documentTypes) {
        this.documentTypes = new HashMap<>();
        for (DocumentType dt : documentTypes) {
            this.documentTypes.put(dt.getId(), dt.getName());
        }
    }



    @Override
    public int getRowCount() {
        return documents.size();
    }

    @Override
    public int getColumnCount() {
        return 5;
    }

    @Override
    public String getColumnName(int column) {
        return switch (column) {
            case 0:
                yield res.getString("colName");
            case 1:
                yield res.getString("colDescription");
            case 2:
                yield res.getString("colDate");
            case 3:
                yield res.getString("colLastModified");
            case 4:
                yield res.getString("colDocumentType");
            default:
                yield "";
        };
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Document document = documents.get(rowIndex);
        return switch (columnIndex) {
            case 0:
                yield document.getName();
            case 1:
                yield document.getDescription();
            case 2:
                yield document.getDate();
            case 3:
                yield document.getLastModified();
            case 4:
                yield documentTypes.get(document.getDocumentTypeId());
            default:
                yield "??";
        };
    }

    public Document getDocumentAt(int row) {
        return documents.get(row);
    }
}
