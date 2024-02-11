package org.exaphex.realty.ui.units;

import org.exaphex.realty.db.service.ContactService;
import org.exaphex.realty.model.Contact;
import org.exaphex.realty.model.Rent;
import org.exaphex.realty.model.Unit;
import org.exaphex.realty.model.ui.cmb.ContactComboBoxModel;

import javax.swing.*;
import javax.swing.text.DateFormatter;
import javax.swing.text.DefaultFormatterFactory;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import static org.exaphex.realty.util.PriceUtils.validateInteger;
import static org.exaphex.realty.util.PriceUtils.validatePrice;

public class RentModal {
    private final ResourceBundle res = ResourceBundle.getBundle("i18n");
    private final ContactComboBoxModel ccm = new ContactComboBoxModel(new ArrayList<>());
    private Unit unit;
    private Rent selectedRent;
    private final UnitWindow uw;
    private JDialog dialog;
    private JFormattedTextField txtStartDate;
    private JTextField txtRentalPrice;
    private JTextField txtExtraCost;
    private JTextField txtDeposit;
    private JButton btnSave;
    private JPanel mainPanel;
    private JFormattedTextField txtEndDate;
    private JTextField txtNumOfTentants;
    private JComboBox<Contact> cmbContact;
    private JRadioButton radioFirstDay;
    private JRadioButton radioCustomDay;
    private JRadioButton radioLastDay;
    private JTextField txtCustomDay;

    public RentModal(UnitWindow uw, Unit u) {
        this.unit = u;
        this.uw = uw;
        setupCreateUI();
        setupListeners();
    }

    public RentModal(UnitWindow uw, Rent rent) {
        this.uw = uw;
        this.selectedRent = rent;
        setupEditUI();
        setupListeners();
    }

    private void setupCreateUI() {
        setupDialog();

        SimpleDateFormat DateFor = new SimpleDateFormat("dd-MM-yyyy");
        txtStartDate.setText(DateFor.format(new Date()));
        txtEndDate.setText("30-12-9999");

        txtRentalPrice.setText("0.00");
        txtExtraCost.setText("0.00");
        txtDeposit.setText("0.00");
        txtNumOfTentants.setText("1");
    }

    private void setupEditUI() {
        setupDialog();

        int pos = ccm.getContactById(this.selectedRent.getContactId());
        if (pos >= 0) {
            cmbContact.setSelectedIndex(pos);
        }

        switch (this.selectedRent.getPayDay()) {
            case 0:
                radioLastDay.setSelected(true);
                break;
            case 1:
                radioFirstDay.setSelected(true);
                break;
            default:
                radioCustomDay.setSelected(true);
                txtCustomDay.setText(this.selectedRent.getPayDay()+"");
        }

        txtNumOfTentants.setText(this.selectedRent.getNumOfTentants()+"");

        txtStartDate.setText(this.selectedRent.getStartDate());
        txtEndDate.setText(this.selectedRent.getEndDate());

        txtRentalPrice.setText(this.selectedRent.getRentalPrice()+"");
        txtExtraCost.setText(this.selectedRent.getExtraCosts()+"");
        txtDeposit.setText(this.selectedRent.getDeposit()+"");
    }

    private void setupDialog() {
        DateFormatter df = new DateFormatter(new SimpleDateFormat("dd-MM-yyyy"));
        DefaultFormatterFactory dff = new DefaultFormatterFactory(df, df, df, df);
        txtStartDate.setFormatterFactory(dff);
        txtEndDate.setFormatterFactory(dff);

        cmbContact.setModel(ccm);

        loadContacts();

        this.dialog = new JDialog();
        dialog.setTitle(res.getString("titleAddRent"));
        dialog.add(mainPanel);
        dialog.pack();
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    private void loadContacts() {
        List<Contact> contacts = ContactService.getContacts();
        ccm.setContacts(contacts);
    }

    private void setupListeners() {
        btnSave.addActionListener(
                e -> {

                    Contact contact = (Contact) cmbContact.getSelectedItem();
                    if (contact == null) {
                        JOptionPane.showMessageDialog(new JFrame(), res.getString("msgInvalidContact"), res.getString("msgError"),
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (txtStartDate.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(new JFrame(), res.getString("msgStartDateInvalid"), res.getString("msgError"),
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (txtEndDate.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(new JFrame(), res.getString("msgEndDateInvalid"), res.getString("msgError"),
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    int payDay = 1;
                    if (radioCustomDay.isSelected()) {
                        Integer intCustomDay = validateInteger(txtCustomDay.getText(), res.getString("msgNumberOfTentants"));
                        if (intCustomDay == null) {
                            return;
                        }
                        payDay = intCustomDay;
                    } else if (radioLastDay.isSelected()) {
                        payDay = 0;
                    }

                    Float fRentalPrice = validatePrice(txtRentalPrice.getText(), res.getString("msgRentalPrice"));
                    Float fExtraCosts = validatePrice(txtExtraCost.getText(), res.getString("msgExtraCosts"));
                    Float fDeposit = validatePrice(txtDeposit.getText(), res.getString("msgDeposit"));
                    Integer iNumberOfTentant = validateInteger(txtNumOfTentants.getText(), res.getString("msgNumberOfTentants"));
                    if (fRentalPrice == null || fExtraCosts == null || fDeposit == null || iNumberOfTentant == null) {
                        return;
                    }
                    if (this.selectedRent != null) {
                        uw.eventEditRent(new Rent(this.selectedRent.getId(), contact.getId(), this.selectedRent.getUnitId(), txtStartDate.getText(), txtEndDate.getText(), fRentalPrice, fExtraCosts, fDeposit, iNumberOfTentant, payDay, false));
                    } else {
                        uw.eventAddNewRent(new Rent(contact.getId(), this.unit.getId(), txtStartDate.getText(), txtEndDate.getText(), fRentalPrice, fExtraCosts, fDeposit, iNumberOfTentant, payDay, false));
                    }

                    dialog.dispose();
                });
    }

}
