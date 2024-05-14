package org.exaphex.realty.ui.buildings;

import org.exaphex.realty.db.service.*;
import org.exaphex.realty.model.*;
import org.exaphex.realty.processor.RentPaymentCheckProcessor;
import org.exaphex.realty.processor.chart.IncomeExpenseChartProcessor;
import org.exaphex.realty.ui.credit.CreditPane;
import org.exaphex.realty.ui.overview.OverviewPane;
import org.exaphex.realty.ui.transactions.TransactionPane;
import org.exaphex.realty.ui.units.UnitWindow;
import org.jfree.chart.ChartPanel;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import static org.exaphex.realty.util.DateUtils.safeFormatDate;
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
    private JTextField txtTotalShares;
    private JPanel tabCredit;
    private JPanel tabChart;
    private JPanel tabGeneral;
    private CreditPane creditPane;
    private JPanel tabTransaction;
    private TransactionPane transactionPane;
    private OverviewPane overviewPane;
    private JPanel tabOverview;

    public BuildingWindow(Building b) {
        super();
        this.selectedBuilding = b;
        buildUI();
        setListeners();
        this.unitDetail.setUI(b);
        this.transactionPane.init();
        this.creditPane.init();
    }

    private void buildUI() {
        setTitle(this.selectedBuilding.getName());
        setBuildingOverviewData(this.selectedBuilding);
        setContentPane(mainPanel);
    }

    private void setBuildingOverviewData(Building b) {
        List<Rent> rents = new ArrayList<>();
        List<Transaction> transactions = TransactionService.getTransactions(b);
        List<Credit> credits = CreditService.getCredit(b);
        List<Valuation> valuations = new ArrayList<>();
        List<Unit> units = UnitService.getUnits(b);


        for (Unit u : units) {
            List<Rent> tmpRents = RentService.getRents(u);
            tmpRents = tmpRents.stream().filter(e -> {
                Date startDate = safeFormatDate(e.getStartDate());
                Date endDate = safeFormatDate(e.getEndDate());
                Date now = new Date();
                return (now.before(endDate) || now.equals(endDate)) && (now.after(startDate) || now.equals(startDate));
            }).toList();
            if (!tmpRents.isEmpty()) {
                rents.add(tmpRents.get(0));
            }

            List<Valuation> tmpValuations = ValuationService.getValuations(u);
            tmpValuations.sort((lhs, rhs) -> {
                Date sfLhs = safeFormatDate(lhs.getDate());
                Date sfRhs = safeFormatDate(rhs.getDate());
                return sfLhs.after(sfRhs) ? -1 : sfLhs.before(sfRhs) ? 1 : 0;
            });

            if (!tmpValuations.isEmpty()) {
                valuations.add(tmpValuations.get(0));
            }
            transactions.addAll(TransactionService.getTransactions(u));
            credits.addAll(CreditService.getCredit(u));
        }

        this.overviewPane.loadData(units, rents, transactions, valuations, credits);
    }

    private void setChartBuildingData(Building b) {
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
        transactions.addAll(TransactionService.getTransactions(this.selectedBuilding));

        chartPanel1.setChart(IncomeExpenseChartProcessor.createBuildingChart(transactions));
    }

    private void setBuildingGeneralData(Building b) {
        txtName.setText(b.getName());
        txtStreet.setText(b.getAddress());
        txtStreetNumber.setText(b.getNumber());
        txtPostalCode.setText(b.getPostalCode());
        txtCity.setText(b.getCity());
        txtTotalArea.setText(b.getTotalArea()+"");
        txtTotalShares.setText(b.getTotalShares()+"");
    }

    private void onSelectTab() {
        Component selectedComponent = tabBuilding.getSelectedComponent();
        if (selectedComponent.equals(tabCredit)) {
            creditPane.setUI(this.selectedBuilding);
        } else if (selectedComponent.equals(tabChart)) {
            setChartBuildingData(this.selectedBuilding);
        } else if (selectedComponent.equals(tabGeneral)) {
            setBuildingGeneralData(this.selectedBuilding);
        } else if (selectedComponent.equals(tabTransaction)) {
            transactionPane.setUI(this.selectedBuilding);
        } else if (selectedComponent.equals(tabOverview)) {
            setBuildingOverviewData(this.selectedBuilding);
        }
    }

    private void setListeners() {
        tabBuilding.addChangeListener(e -> onSelectTab());

        btnBuildingGeneralSave.addActionListener(e -> onSaveGeneral());
    }

    private void onSaveGeneral() {
        Float fArea = validatePrice(txtTotalArea.getText(), res.getString("msgArea"));
        if (fArea == null || fArea <= 0) {
            return;
        }

        Float fShares = validatePrice(txtTotalShares.getText(), res.getString("msgShares"));
        if (fShares == null || fShares <= 0) {
            return;
        }

        BuildingService.updateBuilding(new Building(this.selectedBuilding.getId(), txtName.getText(), txtStreet.getText(), txtStreetNumber.getText(), txtPostalCode.getText(), txtCity.getText(), fArea, fShares));
        this.selectedBuilding = BuildingService.getBuilding(this.selectedBuilding).get(0);
    }

    private void createUIComponents() {
        chartPanel1 = new ChartPanel(null);
    }

    public Building getBuilding() {
        return this.selectedBuilding;
    }
}
