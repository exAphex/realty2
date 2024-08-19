package org.exaphex.realty.ui.counters;

import org.exaphex.realty.db.service.CounterTypeService;
import org.exaphex.realty.model.*;
import org.exaphex.realty.model.ui.cmb.*;

import javax.swing.*;
import javax.swing.text.DateFormatter;
import javax.swing.text.DefaultFormatterFactory;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import static org.exaphex.realty.model.Transaction.formatTransactionType;


public class CounterModal {
    private final ResourceBundle res = ResourceBundle.getBundle("i18n");
    private final CounterTypeComboBoxModel ctcm = new CounterTypeComboBoxModel(new ArrayList<>());
    private JComboBox<CounterType> cmbCounterType;
    private JTextField txtValue;
    private JTextField txtDescription;
    private JFormattedTextField txtDate;
    private JButton btnSave;
    private JPanel mainPanel;
    private Unit selectedUnit;
    private Building selectedBuilding;
    private final CounterPane cp;
    private JDialog dialog;
    private CounterRecord counterRecord;

    public CounterModal(CounterPane cp, Unit u) {
        this.cp = cp;
        this.selectedUnit = u;
        setupUI();
        setupListeners();
        loadData();
    }

    public CounterModal(CounterPane cp, Building building) {
        this.cp = cp;
        this.selectedBuilding = building;
        setupUI();
        setupListeners();
        loadData();
    }

    public CounterModal(CounterPane cp, CounterRecord counterRecord) {
        this.cp = cp;
        this.counterRecord = counterRecord;
        setupUI();
        setupListeners();
        loadData();
        setupEditUI();
    }

    private void setupListeners() {
        btnSave.addActionListener(
                e -> {
                    String date = txtDate.getText();
                    String value = txtValue.getText();
                    String description = txtDescription.getText();
                    CounterType selectedType = (CounterType) cmbCounterType.getSelectedItem();
                    if (date.isEmpty()) {
                        JOptionPane.showMessageDialog(new JFrame(), res.getString("msgDateInvalid"), res.getString("msgError"),
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (value.isEmpty()) {
                        JOptionPane.showMessageDialog(new JFrame(), res.getString("msgValueInvalid"), res.getString("msgError"),
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (selectedType == null) {
                        JOptionPane.showMessageDialog(new JFrame(), res.getString("msgCounterTypeInvalid"), res.getString("msgError"),
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (this.counterRecord != null) {
                        cp.eventEditCounterRecord(new CounterRecord(counterRecord.getId(), selectedType.getId(), counterRecord.getObjectId(), date, value, description));
                    } else if (this.selectedBuilding != null) {
                        cp.eventAddNewCounterRecord(new CounterRecord(selectedType.getId(), this.selectedBuilding.getId(), date, value, description));
                    } else if (this.selectedUnit != null) {
                        cp.eventAddNewCounterRecord(new CounterRecord(selectedType.getId(), this.selectedUnit.getId(), date, value, description));
                    }
                    dialog.dispose();
                });
    }

    private void setupEditUI() {
        txtValue.setText(counterRecord.getValue());
        txtDate.setText(counterRecord.getDate());
        txtDescription.setText(counterRecord.getDescription());

        CounterTypeComboBoxModel creditModel = (CounterTypeComboBoxModel) cmbCounterType.getModel();
        int pos = creditModel.getCounterTypeByIndex(counterRecord.getCounterTypeId());
        if (pos >= 0) {
            cmbCounterType.setSelectedIndex(pos);
        }
    }

    private void setupUI() {
        DateFormatter df = new DateFormatter(new SimpleDateFormat("dd-MM-yyyy"));
        DefaultFormatterFactory dff = new DefaultFormatterFactory(df, df, df, df);
        txtDate.setFormatterFactory(dff);

        SimpleDateFormat DateFor = new SimpleDateFormat("dd-MM-yyyy");
        txtDate.setText(DateFor.format(new Date()));
        txtValue.setText("0.00");

        cmbCounterType.setModel(ctcm);

        this.dialog = new JDialog();
        dialog.setTitle(res.getString("titleManageCounterRecord"));
        dialog.add(mainPanel);
        dialog.pack();
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    private void loadData() {
        List<CounterType> counterTypes = CounterTypeService.getCounterTypes();
        ctcm.setCounterTypes(counterTypes);
    }

}
