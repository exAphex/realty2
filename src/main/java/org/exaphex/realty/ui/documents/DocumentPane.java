package org.exaphex.realty.ui.documents;

import org.exaphex.realty.db.service.DocumentService;
import org.exaphex.realty.db.service.DocumentTypeService;
import org.exaphex.realty.model.*;
import org.exaphex.realty.model.ui.table.DocumentTableModel;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DocumentPane {
    private final ResourceBundle res = ResourceBundle.getBundle("i18n");
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
        tblDocument.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouseEvent) {
                JTable table = (JTable) mouseEvent.getSource();
                int selectedRow = table.getSelectedRow();
                if (mouseEvent.getClickCount() == 2 && selectedRow != -1) {
                    Document document = dtm.getDocumentAt(tblDocument.convertRowIndexToModel(selectedRow));
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setSelectedFile(new File(document.getFileName()));
                    if (fileChooser.showSaveDialog(mainPanel) == JFileChooser.APPROVE_OPTION) {
                        File file = fileChooser.getSelectedFile();
                        try {
                            DocumentService.saveDocumentBinaryToFile(document.getId(), file);
                        } catch (SQLException | IOException e) {
                            JOptionPane.showMessageDialog(new JFrame(), res.getString("msgInvalidFile"), res.getString("msgError"),
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });
    }

    private void loadData() {
        List<Document> documents;
        List<DocumentType> documentTypes = DocumentTypeService.getDocumentTypes();
        if (this.selectedBuilding != null) {
            documents = DocumentService.getDocument(this.selectedBuilding);
        } else {
            documents = DocumentService.getDocument(this.selectedUnit);
        }
        dtm.setDocuments(documents, documentTypes);
    }

    private void onAddNewDocument() {
        if (this.selectedBuilding != null) {
            new DocumentModal(this, this.selectedBuilding);
        } else {
            new DocumentModal(this, this.selectedUnit);
        }
    }

    private void onDeleteDocument() {
        int[] selectedRows = tblDocument.getSelectedRows();
        for (int i : selectedRows) {
            Document document = dtm.getDocumentAt(tblDocument.convertRowIndexToModel(i));
            DocumentService.deleteDocument(document);
        }
        loadData();
    }

    public void eventAddNewDocument(Document d, byte[] bytes) {
        DocumentService.addDocument(d, bytes);
        loadData();
    }
}
