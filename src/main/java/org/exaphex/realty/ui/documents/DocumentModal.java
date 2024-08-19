package org.exaphex.realty.ui.documents;

import org.exaphex.realty.db.service.DocumentTypeService;
import org.exaphex.realty.model.Building;
import org.exaphex.realty.model.Document;
import org.exaphex.realty.model.DocumentType;
import org.exaphex.realty.model.Unit;
import org.exaphex.realty.model.ui.cmb.DocumentTypeComboBoxModel;

import javax.swing.*;
import javax.swing.text.DateFormatter;
import javax.swing.text.DefaultFormatterFactory;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class DocumentModal {
    private final ResourceBundle res = ResourceBundle.getBundle("i18n");
    private final DocumentTypeComboBoxModel dcm = new DocumentTypeComboBoxModel(new ArrayList<>());
    private JPanel mainPanel;
    private JTextField txtDescription;
    private JTextField txtName;
    private JButton btnFile;
    private JButton btnSave;
    private JComboBox<DocumentType> cmbDocumentType;
    private JFormattedTextField txtDate;
    private JLabel lblFileName;
    private File selectedFile;
    private Building building;
    private Unit unit;

    private DocumentPane dp;
    private JDialog dialog;

    public DocumentModal(DocumentPane dp, Unit u) {
        this.unit = u;
        this.dp = dp;
        setupUI();
        setupListeners();
        loadDocumentTypes();
    }

    public DocumentModal(DocumentPane dp, Building b) {
        this.building = b;
        this.dp = dp;
        setupUI();
        setupListeners();
        loadDocumentTypes();
    }

    private void setupUI() {
        DateFormatter df = new DateFormatter(new SimpleDateFormat("dd-MM-yyyy"));
        DefaultFormatterFactory dff = new DefaultFormatterFactory(df, df, df, df);
        txtDate.setFormatterFactory(dff);


        SimpleDateFormat DateFor = new SimpleDateFormat("dd-MM-yyyy");
        txtDate.setText(DateFor.format(new Date()));

        cmbDocumentType.setModel(dcm);

        this.dialog = new JDialog();
        dialog.setTitle(res.getString("titleAddDocument"));
        dialog.add(mainPanel);
        dialog.pack();
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    private void loadDocumentTypes() {
        List<DocumentType> documentTypes = DocumentTypeService.getDocumentTypes();
        dcm.setDocumentTypes(documentTypes);
    }

    private void setupListeners() {
        btnSave.addActionListener(
            e -> {
                String name = txtName.getText();
                if (name.isEmpty()) {
                    JOptionPane.showMessageDialog(new JFrame(), res.getString("msgNameMandatory"), res.getString("msgError"),
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String date = txtDate.getText();
                if (date.isEmpty()) {
                    JOptionPane.showMessageDialog(new JFrame(), res.getString("msgDateInvalid"), res.getString("msgError"),
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                DocumentType selectedType = (DocumentType) cmbDocumentType.getSelectedItem();
                if (selectedType == null) {
                    JOptionPane.showMessageDialog(new JFrame(), res.getString("msgInvalidType"), res.getString("msgError"),
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (this.selectedFile == null) {
                    JOptionPane.showMessageDialog(new JFrame(), res.getString("msgInvalidFile"), res.getString("msgError"),
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                SimpleDateFormat DateFor = new SimpleDateFormat("dd-MM-yyyy");
                String now = DateFor.format(new Date());

                byte[] bytes;
                try {
                    bytes = Files.readAllBytes(Paths.get(this.selectedFile.getAbsolutePath()));
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(new JFrame(), res.getString("msgInvalidFile"), res.getString("msgError"),
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (this.building != null) {
                    dp.eventAddNewDocument(new Document(name, date, txtDescription.getText(), this.selectedFile.getName(), now, now, selectedFile.length(), false, this.building.getId(), selectedType.getId()), bytes);
                } else {
                    dp.eventAddNewDocument(new Document(name, date, txtDescription.getText(), this.selectedFile.getName(), now, now, selectedFile.length(), false, this.unit.getId(), selectedType.getId()), bytes);
                }
                dialog.dispose();
            });

        btnFile.addActionListener(e-> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
            int result = fileChooser.showOpenDialog(this.dialog);
            if (result == JFileChooser.APPROVE_OPTION) {
                this.selectedFile = fileChooser.getSelectedFile();
                lblFileName.setText(this.selectedFile.getName());
                //System.out.println("Selected file: " + selectedFile.getAbsolutePath());
            }
        });
    }
}
