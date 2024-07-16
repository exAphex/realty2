package org.exaphex.realty.ui.documents;

import org.exaphex.realty.db.service.DocumentService;
import org.exaphex.realty.model.Building;
import org.exaphex.realty.model.Document;
import org.exaphex.realty.model.Unit;
import org.exaphex.realty.model.ui.table.DocumentTableModel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class DocumentPane {
    private final DocumentTableModel dtm = new DocumentTableModel(new ArrayList<>());
    private JButton btnAddDocument;
    private JButton btnDeleteDocument;
    private JTable tblDocument;
    private JPanel mainPanel;
    private Building selectedBuilding;
    private Unit selectedUnit;

    public void init() {
        tblDocument.setModel(dtm);
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
        btnAddDocument.addActionListener(e -> this.onAddNewDocument());
        btnDeleteDocument.addActionListener(e -> this.onDeleteDocument());
    }

    private void loadData() {
        List<Document> documents;
        if (this.selectedBuilding != null) {
            documents = DocumentService.getDocument(this.selectedBuilding);
        } else {
            documents = DocumentService.getDocument(this.selectedUnit);
        }
        dtm.setDocuments(documents);
    }

    private void onAddNewDocument() {

    }

    private void onDeleteDocument() {
        int[] selectedRows = tblDocument.getSelectedRows();
        for (int i : selectedRows) {
            Document document = dtm.getDocumentAt(tblDocument.convertRowIndexToModel(i));
            DocumentService.deleteDocument(document);
        }
        loadData();
    }

    public void eventAddNewDocument(Document d) {
        DocumentService.addDocument(d);
        loadData();
    }
}
