package org.exaphex.realty.ui.units;

import org.exaphex.realty.processor.chart.IncomeExpenseChartProcessor;
import com.opencsv.bean.CsvToBeanBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.exaphex.realty.db.service.*;
import org.exaphex.realty.model.*;
import org.exaphex.realty.model.transport.ValuationTransportModel;
import org.exaphex.realty.model.ui.cmb.UnitComboBoxModel;
import org.exaphex.realty.model.ui.table.*;
import org.exaphex.realty.processor.CreditProcessor;
import org.exaphex.realty.processor.RentPaymentCheckProcessor;
import org.jfree.chart.ChartPanel;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.List;

import static org.exaphex.realty.processor.CreditProcessor.getPaidAmount;
import static org.exaphex.realty.processor.CreditProcessor.getTotalAmount;
import static org.exaphex.realty.util.DateUtils.safeFormatDate;
import static org.exaphex.realty.util.DateUtils.setDateSorter;
import static org.exaphex.realty.util.PriceUtils.setPriceSorter;

public class UnitWindow extends JFrame {
    protected static final Logger logger = LogManager.getLogger();
    UnitComboBoxModel utm = new UnitComboBoxModel(new ArrayList<>());
    final ValuationTableModel vtm = new ValuationTableModel(new ArrayList<>());
    final RentTableModel rtm = new RentTableModel(new ArrayList<>());
    final TransactionTableModel ttm = new TransactionTableModel(new ArrayList<>());
    final CreditTableModel ctm = new CreditTableModel(new ArrayList<>());
    Unit selectedUnit;
    private final Building building;
    private JComboBox<Unit> cmbUnits;
    private JButton btnAddUnit;
    private JButton btnDeleteUnit;
    private JTabbedPane tabPane;
    private JTextField txtName;
    private JTextField txtArea;
    private JButton btnSave;
    private JPanel mainPanel;
    private JPanel paneGeneral;
    private JPanel paneRent;
    private JPanel paneValuation;
    private JButton btnAddValuation;
    private JButton btnDeleteValuation;
    private JTable tblValuations;
    private JButton btnImportValuation;
    private JButton btnAddRent;
    private JButton btnDeleteRent;
    private JTable tblRents;
    private JTable tblAccount;
    private JPanel paneAccount;
    private JButton btnAddTransaction;
    private JPanel paneOverview;
    private JLabel lblCurrentValue;
    private JLabel lblEquity;
    private JLabel lblReturnOnEquity;
    private JLabel lblReturnOnInvestment;
    private JLabel lblReceivedRents;
    private JButton btnDeleteTransaction;
    private JPanel paneCredit;
    private JButton btnAddCredit;
    private JButton btnDeleteCredit;
    private JTable tblCredit;
    private JButton btnCheckCredit;
    private JButton btnCheckRent;
    private JLabel lblTotalCredit;
    private JLabel lblPaidBackCredit;
    private JLabel lblRemainedCredit;
    private JTabbedPane tabBuilding;
    private JLabel lblPaidRent;
    private JLabel lblPaidRentNumber;
    private JLabel lblUnpaidRent;
    private JLabel lblUnpaidRentNumber;
    private ChartPanel chartPanel1;
    private JPanel panelChart;

    public UnitWindow(Building b) {
        super();
        this.building = b;
        buildUI();
        setListeners();
        loadUnits(this.building);
    }

    private void buildUI() {
        setTitle(this.building.getName());
        setBuildingOverviewData(this.building);
        cmbUnits.setModel(utm);
        tblValuations.setModel(vtm);
        tblRents.setModel(rtm);
        tblAccount.setModel(ttm);
        tblCredit.setModel(ctm);

        // Sorter for Account table
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(tblAccount.getModel());
        List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(0, SortOrder.DESCENDING));
        setDateSorter(sorter, 0);
        setPriceSorter(sorter, 3);
        sorter.setSortKeys(sortKeys);
        sorter.setSortsOnUpdates(true);
        tblAccount.setRowSorter(sorter);

        TableRowSorter<TableModel> sorterValuation = new TableRowSorter<>(tblValuations.getModel());
        List<RowSorter.SortKey> sortKeysValuation = new ArrayList<>();
        sortKeysValuation.add(new RowSorter.SortKey(0, SortOrder.DESCENDING));
        setDateSorter(sorterValuation, 0);
        setPriceSorter(sorterValuation, 1);
        sorterValuation.setSortKeys(sortKeysValuation);
        sorterValuation.setSortsOnUpdates(true);
        tblValuations.setRowSorter(sorterValuation);
        setContentPane(mainPanel);
        setTabPanelStatus(false);
    }

    private void createUIComponents() {
        chartPanel1 = new ChartPanel(null);
    }

    void setTabPanelStatus(Boolean isEnabled) {
        tabPane.setEnabled(isEnabled);
        setPanelEnabled(paneOverview, isEnabled);
        setPanelEnabled(paneGeneral, isEnabled);
        setPanelEnabled(paneRent, isEnabled);
        setPanelEnabled(paneValuation, isEnabled);
        setPanelEnabled(paneAccount, isEnabled);
        setPanelEnabled(paneCredit, isEnabled);
    }

    void setPanelEnabled(JPanel panel, Boolean isEnabled) {
        panel.setEnabled(isEnabled);
        Component[] components = panel.getComponents();
        for (Component component : components) {
            if (component instanceof JPanel) {
                setPanelEnabled((JPanel) component, isEnabled);
            }
            component.setEnabled(isEnabled);
        }
    }

    private void setListeners() {
        UnitWindow self = this;
        btnAddUnit.addActionListener(e -> this.onAddNewUnit());
        btnDeleteUnit.addActionListener(e -> this.onDeleteUnit());
        btnAddValuation.addActionListener(e -> this.onAddNewValuation());
        btnDeleteValuation.addActionListener(e -> this.onDeleteValuation());
        btnImportValuation.addActionListener(e -> this.onImportValuation());
        btnAddRent.addActionListener(e -> this.onAddNewRent());
        btnDeleteRent.addActionListener(e -> this.onDeleteRent());
        btnAddTransaction.addActionListener(e -> this.onAddTransaction());
        btnDeleteTransaction.addActionListener(e -> this.onDeleteTransaction());
        btnAddCredit.addActionListener(e -> this.onAddCredit());
        btnDeleteCredit.addActionListener(e -> this.onDeleteCredit());
        btnCheckCredit.addActionListener(e -> this.onCheckCredit());
        btnCheckRent.addActionListener(e -> this.onCheckRent());

        cmbUnits.addItemListener(event -> {
            if (event.getStateChange() == ItemEvent.SELECTED) {
                Unit item = (Unit) event.getItem();
                selectUnit(item);
            }
        });

        tabPane.addChangeListener(e -> {
            if (this.selectedUnit == null) {
                return;
            }

            if (tabPane.getSelectedIndex() == 0) {
                setOverviewData(this.selectedUnit);
            } else if (tabPane.getSelectedIndex() == 1) {
                setFields(this.selectedUnit);
            }
        });

        tabBuilding.addChangeListener(e -> {
            if (tabBuilding.getSelectedIndex() == 0) {
                setBuildingOverviewData(this.building);
            }
        });

        tblRents.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouseEvent) {
                JTable table = (JTable) mouseEvent.getSource();
                int selectedRow = table.getSelectedRow();
                if (mouseEvent.getClickCount() == 2 && selectedRow != -1) {
                    int selectedModelRow = tblRents.convertColumnIndexToModel(selectedRow);
                    Rent selectedRent = ((RentTableModel) tblRents.getModel()).getRentAt(selectedModelRow);
                    new RentModal(self, selectedRent);
                }
            }
        });

        tblValuations.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouseEvent) {
                JTable table = (JTable) mouseEvent.getSource();
                int selectedRow = table.getSelectedRow();
                if (mouseEvent.getClickCount() == 2 && selectedRow != -1) {
                    Valuation valuation = vtm.getValuationAt(tblValuations.convertRowIndexToModel(selectedRow));
                    new ValuationModal(self, valuation);
                }
            }
        });

        tblAccount.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouseEvent) {
                JTable table = (JTable) mouseEvent.getSource();
                int selectedRow = table.getSelectedRow();
                if (mouseEvent.getClickCount() == 2 && selectedRow != -1) {
                    Transaction transaction = ttm.getTransactionAt(tblAccount.convertRowIndexToModel(selectedRow));
                    new TransactionModal(self, transaction);
                }
            }
        });
    }

    private void selectUnit(Unit u) {
        this.selectedUnit = u;
        setTabPanelStatus(true);
        loadValuations(this.selectedUnit);
        loadRents(this.selectedUnit);
        loadTransactions(this.selectedUnit);
        loadCredits(this.selectedUnit);
        setOverviewData(u);
    }

    private void setFields(Unit u) {
        this.txtName.setText(u != null ? u.getName() : "");
        this.txtArea.setText(u!= null ? ""+u.getArea() : "");
    }

    private void setBuildingOverviewData(Building b) {
        List<Unit> units = UnitService.getUnits(b);
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        List<PaymentCheck> payments = RentPaymentCheckProcessor.getRentPaymentCheck(this.building);
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
        //panelChart.add(IncomeExpenseChartProcessor.createBuildingChart(transactions));
    }

    private void setOverviewData(Unit u) {
        List<Valuation> valuations = vtm.getValuations();
        List<Rent> rents = rtm.getRents();
        List<Transaction> transactions = TransactionService.getTransactions(u);
        List<Credit> credits = ctm.getCredits();

        lblCurrentValue.setText("-");
        lblEquity.setText("-");
        lblReturnOnEquity.setText("0%");
        lblReturnOnInvestment.setText("0%");
        lblReceivedRents.setText("-");
        lblTotalCredit.setText("-");
        lblRemainedCredit.setText("-");
        lblPaidBackCredit.setText("-");

        if (u == null) {
            return;
        }

        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        DecimalFormat decimalFormatter = new DecimalFormat("##.##%");
        float currValue = 0;
        float currEquityValue = 0;
        if (!valuations.isEmpty()) {
            valuations.sort((lhs, rhs) -> {
                Date sfLhs = safeFormatDate(lhs.getDate());
                Date sfRhs = safeFormatDate(rhs.getDate());
                return sfLhs.after(sfRhs) ? -1 : sfLhs.before(sfRhs) ? 1 : 0;
            });
            currValue = valuations.get(0).getValue();
            float creditTotal = getTotalAmount(CreditService.getCredit(this.selectedUnit));
            currEquityValue = currValue - creditTotal + CreditProcessor.getTotalPaidAmount(transactions);

            lblCurrentValue.setText(formatter.format(currValue));
            lblEquity.setText(formatter.format(currEquityValue));
        }

        if (!rents.isEmpty()) {
            rents.sort((lhs, rhs) -> {
                Date sfLhs = safeFormatDate(lhs.getStartDate());
                Date sfRhs = safeFormatDate(rhs.getStartDate());
                return sfLhs.after(sfRhs) ? -1 : sfLhs.before(sfRhs) ? 1 : 0;
            });

            float currentNetRent = rents.get(0).getRentalPrice();
            float returnOnInvestment = currValue > 0 ? (currentNetRent * 12) / currValue : 0;
            float returnOnEquity = currEquityValue > 0 ? (currentNetRent * 12) / currEquityValue : 0;
            lblReturnOnEquity.setText(decimalFormatter.format(returnOnEquity));
            lblReturnOnInvestment.setText(decimalFormatter.format(returnOnInvestment));
        }

        if (!transactions.isEmpty()) {
            float tempTotalRents = transactions.stream().filter(t -> t.getType() == Transaction.RENT_PAYMENT).map(Transaction::getAmount).reduce(0f, Float::sum);
            lblReceivedRents.setText(formatter.format(tempTotalRents));
        }

        if (!credits.isEmpty()) {
            float totalCredit = credits.stream().map(Credit::getAmount).reduce(0f, Float::sum);
            float paidBackCredit = credits.stream().map(Credit::getRepaidAmount).reduce(0f, Float::sum);
            float remainedCredit = totalCredit - paidBackCredit;
            float repaidPercent = paidBackCredit / totalCredit;
            lblTotalCredit.setText(formatter.format(totalCredit));
            lblRemainedCredit.setText(formatter.format(remainedCredit));
            lblPaidBackCredit.setText("(" + decimalFormatter.format(repaidPercent) + ") " + formatter.format(paidBackCredit));
        }
    }

    private void loadUnits(Building b) {
        List<Unit> units = UnitService.getUnits(b);
        utm.setUnits(units);
    }

    private void loadValuations(Unit u) {
        List<Valuation> valuations = ValuationService.getValuations(u);
        vtm.setValuations(valuations);
    }

    private void loadRents(Unit u) {
        List<Rent> rents = RentService.getRents(u);
        rtm.setRents(rents);
    }

    public void loadTransactions(Unit u) {
        List<Transaction> transactions = TransactionService.getTransactions(u);
        ttm.setTransactions(transactions);
    }

    public void loadCredits(Unit u) {
        List<Credit> credits = CreditService.getCredit(u);
        List<Transaction> transactions = TransactionService.getTransactions(u);
        for (Credit c : credits) {
            c.setRepaidAmount(getPaidAmount(c, transactions));
        }
        ctm.setCredits(credits);
    }

    public Building getBuilding() {
        return this.building;
    }

    private void onAddNewUnit() {
        new UnitModal(this, this.building);
    }

    private void onAddNewRent() {
        new RentModal(this, this.selectedUnit);
    }

    private void onAddNewValuation() {
        new ValuationModal(this, this.selectedUnit);
    }

    private void onAddTransaction() {
        new TransactionModal(this, this.selectedUnit);
    }

    private void onAddCredit() { new CreditModal(this, this.selectedUnit);}

    private void onImportValuation() {
        JFileChooser chooser = new JFileChooser();
        int retOption = chooser.showOpenDialog(null);
        if (retOption == JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();
            try {
                List<ValuationTransportModel> valuations = new CsvToBeanBuilder<ValuationTransportModel>(new FileReader(f))
                        .withSeparator(';')
                        .withType(ValuationTransportModel.class)
                        .build()
                        .parse();
                for (ValuationTransportModel v : valuations) {
                    try {
                        ValuationService.addValuation(v.getValuation(this.selectedUnit.getId()));
                    } catch (Exception e) {
                        logger.error(e);
                    }
                }
                loadValuations(this.selectedUnit);
            } catch (FileNotFoundException e) {
                logger.error(e);
            }
        }
    }

    private void onDeleteValuation() {
        int[] selectedRows = tblValuations.getSelectedRows();
        for (int i : selectedRows) {
            Valuation valuation = vtm.getValuationAt(tblValuations.convertRowIndexToModel(i));
            ValuationService.deleteValuation(valuation);
        }
        loadValuations(this.selectedUnit);
    }

    private void onDeleteTransaction() {
        int[] selectedRows = tblAccount.getSelectedRows();
        for (int i : selectedRows) {
            Transaction transaction = ttm.getTransactionAt(tblAccount.convertRowIndexToModel(i));
            TransactionService.deleteTransaction(transaction);
        }
        loadTransactions(this.selectedUnit);
    }

    private void onDeleteCredit() {
        int[] selectedRows = tblCredit.getSelectedRows();
        for (int i : selectedRows) {
            Credit credit = ctm.getCreditAt(tblCredit.convertRowIndexToModel(i));
            CreditService.deleteCredit(credit);
        }
        loadCredits(this.selectedUnit);
    }

    private void onDeleteRent() {
        if (tblRents.getSelectedRow() == -1)
            return;

        Rent rent = rtm.getRentAt(tblRents.convertRowIndexToModel(tblRents.getSelectedRow()));
        RentService.deleteRent(rent);
        loadRents(this.selectedUnit);
    }

    private void onCheckCredit() {
        if (tblCredit.getSelectedRow() == -1)
            return;

        Credit credit = ctm.getCreditAt(tblCredit.convertRowIndexToModel(tblCredit.getSelectedRow()));
        new PaymentCheckWindow(this, this.selectedUnit, credit);
    }

    private void onCheckRent() {
        if (tblRents.getSelectedRow() == -1)
            return;

        Rent rent = rtm.getRentAt(tblRents.convertRowIndexToModel(tblRents.getSelectedRow()));
        new PaymentCheckWindow(this, this.selectedUnit, rent);
    }

    private void onDeleteUnit() {
        if (this.selectedUnit == null) { return;}
        UnitService.deleteUnit(this.selectedUnit);

        this.selectedUnit = null;
        utm = new UnitComboBoxModel(new ArrayList<>());
        cmbUnits.setModel(utm);
        setTabPanelStatus(false);
        setOverviewData(null);
        tabPane.setSelectedIndex(0);

        loadUnits(this.building);
    }

    public void eventAddNewUnit(Unit u) {
        UnitService.addUnit(u);
        loadUnits(this.building);

        utm.setSelectedItem(u);
        selectUnit(u);
    }

    public void eventAddNewValuation(Valuation v) {
        ValuationService.addValuation(v);
        loadValuations(this.selectedUnit);
    }

    public void eventEditValuation(Valuation valuation) {
        ValuationService.updateValuation(valuation);
        loadValuations(this.selectedUnit);
    }

    public void eventAddNewRent(Rent r) {
        RentService.addRent(r);
        loadRents(this.selectedUnit);
    }

    public void eventEditRent(Rent r) {
        RentService.updateRent(r);
        loadRents(this.selectedUnit);
    }

    public void eventAddNewTransaction(Transaction t) {
        TransactionService.addTransaction(t);
        loadTransactions(this.selectedUnit);
    }

    public void eventEditTransaction(Transaction t) {
        TransactionService.updateTransaction(t);
        loadTransactions(this.selectedUnit);
    }

    public void eventAddNewCredit(Credit c) {
        CreditService.addCredit(c);
        loadCredits(this.selectedUnit);
    }
}
