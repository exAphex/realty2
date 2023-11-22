package org.exaphex.realty.ui.units;

import org.exaphex.realty.model.Unit;
import org.exaphex.realty.model.Valuation;

import javax.swing.*;
import javax.swing.text.DateFormatter;
import javax.swing.text.DefaultFormatterFactory;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class ValuationModal {
    private final ResourceBundle res = ResourceBundle.getBundle("i18n");
    private final UnitWindow uw;
    private Valuation valuation;
    private Unit unit;
    private JDialog dialog;
    private JFormattedTextField txtDate;
    private JPanel mainPanel;
    private JButton btnSave;
    private JTextField txtValue;

    public ValuationModal(UnitWindow uw, Unit u) {
        this.unit = u;
        this.uw = uw;
        setupUI();
        setupListeners();
    }

    public ValuationModal(UnitWindow uw, Valuation valuation) {
        this.uw = uw;
        this.valuation = valuation;
        setupEditUI();
        setupListeners();
    }

    private void setupDialog() {
        DateFormatter df  = new DateFormatter(new SimpleDateFormat("dd-MM-yyyy"));
        DefaultFormatterFactory dff  = new DefaultFormatterFactory(df, df, df, df);
        txtDate.setFormatterFactory(dff);

        this.dialog = new JDialog();
        dialog.setTitle(res.getString("titleValuation"));
        dialog.add(mainPanel);
        dialog.pack();
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    private void setupUI() {
        setupDialog();
        SimpleDateFormat DateFor = new SimpleDateFormat("dd-MM-yyyy");
        txtDate.setText(DateFor.format(new Date()));
        txtValue.setText("0.00");
    }

    private void setupEditUI() {
        setupDialog();
        txtDate.setText(this.valuation.getDate());
        txtDate.setEnabled(false);
        txtValue.setText(this.valuation.getValue()+"");
    }

    private void setupListeners() {
        btnSave.addActionListener(
                e -> {
                    String date = txtDate.getText();
                    String value = txtValue.getText();
                    if (date.isEmpty()) {
                        JOptionPane.showMessageDialog(new JFrame(), res.getString("msgDateInvalid"), res.getString("msgError"),
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    float val;
                    try {
                        val = Float.parseFloat(value);
                    } catch(NumberFormatException n) {
                        JOptionPane.showMessageDialog(new JFrame(), res.getString("msgValueInvalid"), res.getString("msgError"),
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (this.valuation != null) {
                        uw.eventEditValuation(new Valuation(this.valuation.getId(), txtDate.getText(), val));
                    } else {
                        uw.eventAddNewValuation(new Valuation(this.unit.getId(), txtDate.getText(), val));
                    }
                    dialog.dispose();
                });
    }
}
