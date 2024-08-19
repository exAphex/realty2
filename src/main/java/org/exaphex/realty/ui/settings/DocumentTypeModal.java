package org.exaphex.realty.ui.settings;

import org.exaphex.realty.model.DocumentType;
import org.exaphex.realty.ui.MainWindow;

import javax.swing.*;
import java.util.ResourceBundle;

public class DocumentTypeModal {
    private final ResourceBundle res = ResourceBundle.getBundle("i18n");
    private final MainWindow mw;
    private DocumentType selectedDocumentType;
    private JTextField txtName;
    private JButton btnSave;
    private JPanel mainPanel;
    private JDialog dialog;

    public DocumentTypeModal(MainWindow mw) {
        this.mw = mw;
        setupUI();
        setupListeners();
    }

    public DocumentTypeModal(MainWindow mw, DocumentType documentType) {
        this.mw = mw;
        this.selectedDocumentType = documentType;
        setupEditUI();
        setupListeners();
    }

    private void setupEditUI() {
        setupUI();
        this.txtName.setText(this.selectedDocumentType.getName());
    }
    private void setupUI() {
        this.dialog = new JDialog();
        dialog.setTitle(res.getString("titleManageDocumentType"));
        dialog.add(mainPanel);
        dialog.pack();
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    private void setupListeners() {
        btnSave.addActionListener(
                e -> {
                    String strName = txtName.getText();
                    if (strName.isEmpty()) {
                        JOptionPane.showMessageDialog(new JFrame(), res.getString("msgNameInvalid"), res.getString("msgError"),
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (this.selectedDocumentType != null) {
                        mw.eventEditDocumentType(new DocumentType(this.selectedDocumentType.getId(), strName));
                    } else {
                        mw.eventAddNewDocumentType(new DocumentType(strName));
                    }
                    dialog.dispose();
                });
    }

}
