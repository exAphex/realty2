package org.exaphex.realty.ui.buildings;

import org.exaphex.realty.db.service.BuildingService;
import org.exaphex.realty.db.service.TransactionService;
import org.exaphex.realty.db.service.UnitService;
import org.exaphex.realty.model.*;
import org.exaphex.realty.processor.RentPaymentCheckProcessor;
import org.exaphex.realty.processor.chart.IncomeExpenseChartProcessor;
import org.exaphex.realty.ui.units.UnitWindow;
import org.jfree.chart.ChartPanel;

import javax.swing.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static org.exaphex.realty.util.PriceUtils.validatePrice;

public class BuildingWindow extends JFrame {
    private final ResourceBundle res = ResourceBundle.getBundle("i18n");
    private Building selectedBuilding;
    private JTabbedPane tabBuilding;
    private JPanel mainPanel;
    private JLabel lblPaidRent;
    private JLabel lblPaidRentNumber;
    private JLabel lblUnpaidRent;
    private JLabel lblUnpaidRentNumber;
    private ChartPanel chartPanel1;
    private JTextField txtName;
    private JTextField txtStreet;
    private JTextField txtStreetNumber;
    private JTextField txtPostalCode;
    private JTextField txtCity;
    private JTextField txtTotalArea;
    private JButton btnBuildingGeneralSave;
    private JPanel unitPanel;
    private UnitWindow unitDetail;

    public BuildingWindow(Building b) {
        super();
        this.selectedBuilding = b;
        buildUI();
        setListeners();
        this.unitDetail.setUI(b);
    }

    private void buildUI() {
        setTitle(this.selectedBuilding.getName());
        setBuildingOverviewData(this.selectedBuilding);
        setContentPane(mainPanel);
    }

    private void setBuildingOverviewData(Building b) {
        List<Unit> units = UnitService.getUnits(b);
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        List<PaymentCheck> payments = RentPaymentCheckProcessor.getRentPaymentCheck(this.selectedBuilding);
        List<PaymentCheck> paidRents = payments.stream().filter(p -> p.getPaidAmount() > 0).toList();
        List<PaymentCheck> unpaidRents = payments.stream().filter(p -> p.getPaidAmount() == 0).toList();
        float paidRentNumber = paidRents.stream().map(PaymentCheck::getPaidAmount).reduce(0f, Float::sum);
        float unpaidRentNumber = unpaidRents.stream().map(PaymentCheck::getAmount).reduce(0f, Float::sum);

        lblPaidRent.setText(paidRents.size()+"");
        lblPaidRentNumber.setText(formatter.format(paidRentNumber));

        lblUnpaidRent.setText(unpaidRents.size()+"");
        lblUnpaidRentNumber.setText(formatter.format(unpaidRentNumber));

        List<Transaction> transactions = new ArrayList<>();
        for (Unit u : units) {
            transactions.addAll(TransactionService.getTransactions(u));
        }
        chartPanel1.setChart(IncomeExpenseChartProcessor.createBuildingChart(transactions));
    }

    private void setBuildingGeneralData(Building b) {
        txtName.setText(b.getName());
        txtStreet.setText(b.getAddress());
        txtStreetNumber.setText(b.getNumber());
        txtPostalCode.setText(b.getPostalCode());
        txtCity.setText(b.getCity());
        txtTotalArea.setText(b.getTotalArea()+"");
    }

    private void setListeners() {
        tabBuilding.addChangeListener(e -> {
            if (tabBuilding.getSelectedIndex() == 0) {
                setBuildingOverviewData(this.selectedBuilding);
            } else if (tabBuilding.getSelectedIndex() == 1){
                setBuildingGeneralData(this.selectedBuilding);
            }
        });

        btnBuildingGeneralSave.addActionListener(e -> {
            onSaveGeneral();
        });
    }

    private void onSaveGeneral() {
        Float fArea = validatePrice(txtTotalArea.getText(), res.getString("msgArea"));
        if (fArea == null || fArea <= 0) {
            return;
        }

        BuildingService.updateBuilding(new Building(this.selectedBuilding.getId(), txtName.getText(), txtStreet.getText(), txtStreetNumber.getText(), txtPostalCode.getText(), txtCity.getText(), fArea));
        this.selectedBuilding = BuildingService.getBuilding(this.selectedBuilding).get(0);
    }

    private void createUIComponents() {
        chartPanel1 = new ChartPanel(null);
    }

    public Building getBuilding() {
        return this.selectedBuilding;
    }
}
