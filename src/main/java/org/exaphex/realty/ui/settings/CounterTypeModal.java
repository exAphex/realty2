package org.exaphex.realty.ui.settings;

import org.exaphex.realty.model.CounterType;
import org.exaphex.realty.model.DocumentType;
import org.exaphex.realty.ui.MainWindow;

import javax.swing.*;
import java.util.ResourceBundle;

public class CounterTypeModal {
    private final ResourceBundle res = ResourceBundle.getBundle("i18n");
    private final MainWindow mw;
    private CounterType selectedCounterType;
    private JTextField txtName;
    private JButton btnSave;
    private JPanel mainPanel;
    private JDialog dialog;

    public CounterTypeModal(MainWindow mw) {
        this.mw = mw;
        setupUI();
        setupListeners();
    }

    public CounterTypeModal(MainWindow mw, CounterType selectedCounterType) {
        this.mw = mw;
        this.selectedCounterType = selectedCounterType;
        setupEditUI();
        setupListeners();
    }

    private void setupEditUI() {
        setupUI();
        this.txtName.setText(this.selectedCounterType.getName());
    }
    private void setupUI() {
        this.dialog = new JDialog();
        dialog.setTitle(res.getString("titleCounterType"));
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
                    if (this.selectedCounterType != null) {
                        mw.eventEditCounterType(new CounterType(this.selectedCounterType.getId(), strName));
                    } else {
                        mw.eventAddNewCounterType(new CounterType(strName));
                    }
                    dialog.dispose();
                });
    }
}
