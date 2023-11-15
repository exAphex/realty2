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
    private final Unit unit;
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

    private void setupUI() {
        DateFormatter df  = new DateFormatter(new SimpleDateFormat("dd-MM-yyyy"));
        DefaultFormatterFactory dff  = new DefaultFormatterFactory(df, df, df, df);
        txtDate.setFormatterFactory(dff);

        SimpleDateFormat DateFor = new SimpleDateFormat("dd-MM-yyyy");
        txtDate.setText(DateFor.format(new Date()));
        txtValue.setText("0.00");

        this.dialog = new JDialog();
        dialog.setTitle(res.getString("titleValuation"));
        dialog.add(mainPanel);
        dialog.pack();
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
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
                    uw.eventAddNewValuation(new Valuation(this.unit.getId(), txtDate.getText(), val));
                    dialog.dispose();
                });
    }
}
