package org.exaphex.realty.ui.units;

import com.opencsv.bean.CsvToBeanBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.exaphex.realty.db.service.*;
import org.exaphex.realty.model.*;
import org.exaphex.realty.model.transport.ValuationTransportModel;
import org.exaphex.realty.model.ui.cmb.UnitComboBoxModel;
import org.exaphex.realty.model.ui.table.*;
import org.exaphex.realty.processor.CreditProcessor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
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

public class UnitWindow extends JFrame {
    protected static final Logger logger = LogManager.getLogger();
    UnitComboBoxModel utm = new UnitComboBoxModel(new ArrayList<>());
    ValuationTableModel vtm = new ValuationTableModel(new ArrayList<>());
    RentTableModel rtm = new RentTableModel(new ArrayList<>());
    ReceiveableTableModel rvtm = new ReceiveableTableModel(new ArrayList<>());
    TransactionTableModel ttm = new TransactionTableModel(new ArrayList<>());
    CreditTableModel ctm = new CreditTableModel(new ArrayList<>());
    Unit selectedUnit;
    private final Building building;
    private JComboBox cmbUnits;
    private JButton btnAddUnit;
    private JButton btnDeleteUnit;
    private JTabbedPane tabPane;
    private JTextField txtName;
    private JTextField textField2;
    private JButton saveButton;
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
    private JButton btnFullPayment;
    private JButton btnPartialPayment;
    private JButton btnDeletePayment;
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
    private JButton rentCheckButton;
    private JLabel lblTotalCredit;
    private JLabel lblPaidBackCredit;
    private JLabel lblRemainedCredit;

    public UnitWindow(Building b) {
        super();
        this.building = b;
        buildUI();
        setListeners();
        loadUnits(this.building);
    }

    private void buildUI() {
        setTitle(this.building.getName());
        cmbUnits.setModel(utm);
        tblValuations.setModel(vtm);
        tblRents.setModel(rtm);
        tblAccount.setModel(ttm);
        tblCredit.setModel(ctm);
        /*tblAccount.setDefaultRenderer(Object.class, new DefaultTableCellRenderer(){
            @Override
            public Component getTableCellRendererComponent(JTable table,Object value,boolean isSelected,boolean hasFocus,int row,int column) {
                Component c = super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);
                Receivable r = rvtm.getReceivableAt(row);
                c.setForeground(Color.RED);
                if (r.getTransaction() != null) {
                    if (r.getAmount() <= r.getTransaction().getAmount()) {
                        c.setForeground(Color.GREEN);
                    } else if (r.getTransaction().getAmount() > 0){
                        c.setForeground(Color.ORANGE);
                    }
                }
                return c;
            }
        });*/
        setContentPane(mainPanel);
        setTabPanelStatus(false);
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
        btnAddUnit.addActionListener(e -> this.onAddNewUnit());
        btnDeleteUnit.addActionListener(e -> this.onDeleteUnit());
        btnAddValuation.addActionListener(e -> this.onAddNewValuation());
        btnDeleteValuation.addActionListener(e -> this.onDeleteValuation());
        btnImportValuation.addActionListener(e -> this.onImportValuation());
        btnAddRent.addActionListener(e -> this.onAddNewRent());
        btnDeleteRent.addActionListener(e -> this.onDeleteRent());
        btnFullPayment.addActionListener(e -> this.onMarkFullPayment());
        btnPartialPayment.addActionListener(e -> this.onMarkPartialPayment());
        btnDeletePayment.addActionListener(e -> this.onDeletePayment());
        btnAddTransaction.addActionListener(e -> this.onAddTransaction());
        btnDeleteTransaction.addActionListener(e -> this.onDeleteTransaction());
        btnAddCredit.addActionListener(e -> this.onAddCredit());
        btnDeleteCredit.addActionListener(e -> this.onDeleteCredit());
        btnCheckCredit.addActionListener(e -> this.onCheckCredit());

        cmbUnits.addItemListener(event -> {
            if (event.getStateChange() == ItemEvent.SELECTED) {
                Unit item = (Unit) event.getItem();
                selectUnit(item);
            }
        });
    }

    private void selectUnit(Unit u) {
        this.selectedUnit = u;
        setTabPanelStatus(true);
        loadValuations(this.selectedUnit);
        loadRents(this.selectedUnit);
        loadReceivables(this.selectedUnit);
        loadTransactions(this.selectedUnit);
        loadCredits(this.selectedUnit);
        setFields(u);
    }

    private void setFields(Unit u) {
        setOverviewData(u);
        this.txtName.setText(u != null ? u.getName() : "");
    }

    private void setOverviewData(Unit u) {
        List<Valuation> valuations = vtm.getValuations();
        List<Rent> rents = rtm.getRents();
        List<Transaction> transactions = TransactionService.getTransactions(u);
        List<Credit> credits = ctm.getCredits();

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
        } else {
            lblCurrentValue.setText("-");
            lblEquity.setText("-");
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
        } else {
            lblReturnOnEquity.setText("0%");
            lblReturnOnInvestment.setText("0%");
        }

        if (!transactions.isEmpty()) {
            float tempTotalRents = transactions.stream().filter(t -> t.getType() == Transaction.RENT_PAYMENT).map(Transaction::getAmount).reduce(0f, Float::sum);
            lblReceivedRents.setText(formatter.format(tempTotalRents));
        } else {
            lblReceivedRents.setText("-");
        }

        if (!credits.isEmpty()) {
            float totalCredit = credits.stream().map(Credit::getAmount).reduce(0f, Float::sum);
            float paidBackCredit = credits.stream().map(Credit::getRepaidAmount).reduce(0f, Float::sum);
            float remainedCredit = totalCredit - paidBackCredit;
            float repaidPercent = paidBackCredit / totalCredit;
            lblTotalCredit.setText(formatter.format(totalCredit));
            lblRemainedCredit.setText(formatter.format(remainedCredit));
            lblPaidBackCredit.setText("(" + decimalFormatter.format(repaidPercent) + ") " + formatter.format(paidBackCredit));
        } else {
            lblTotalCredit.setText("-");
            lblRemainedCredit.setText("-");
            lblPaidBackCredit.setText("-");
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

    private void loadReceivables(Unit u) {
        List<Receivable> receivables = ReceivableService.getReceivables(u);
        rvtm.setReceivables(receivables);
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
                List<ValuationTransportModel> valuations = new CsvToBeanBuilder(new FileReader(f))
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
        if (tblValuations.getSelectedRow() == -1)
            return;

        Valuation valuation = vtm.getValuationAt(tblValuations.getSelectedRow());
        ValuationService.deleteValuation(valuation);
        loadValuations(this.selectedUnit);
    }

    private void onDeleteTransaction() {
        int[] selectedRows = tblAccount.getSelectedRows();
        for (int i : selectedRows) {
            Transaction transaction = ttm.getTransactionAt(i);
            TransactionService.deleteTransaction(transaction);
        }
        loadTransactions(this.selectedUnit);
    }

    private void onDeleteCredit() {
        int[] selectedRows = tblCredit.getSelectedRows();
        for (int i : selectedRows) {
            Credit credit = ctm.getCreditAt(i);
            CreditService.deleteCredit(credit);
        }
        loadCredits(this.selectedUnit);
    }

    private void onMarkFullPayment() {
        int[] selectedRows = tblAccount.getSelectedRows();
        for (int i : selectedRows) {
            Receivable receivable = rvtm.getReceivableAt(i);
            ReceivableService.setFullPayment(receivable);
        }
        loadReceivables(this.selectedUnit);
    }

    private void onMarkPartialPayment() {
        String s = JOptionPane.showInputDialog("Amount:");
        try {
            float f = Float.parseFloat(s);
            int[] selectedRows = tblAccount.getSelectedRows();
            for (int i : selectedRows) {
                Receivable receivable = rvtm.getReceivableAt(i);
                ReceivableService.setPartialPayment(receivable, f);
            }
            loadReceivables(this.selectedUnit);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(new JFrame(), "Value is not valid!", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onDeletePayment() {
        int[] selectedRows = tblAccount.getSelectedRows();
        for (int i : selectedRows) {
            Receivable receivable = rvtm.getReceivableAt(i);
            ReceivableService.deletePayment(receivable);
        }
        loadReceivables(this.selectedUnit);
    }

    private void onDeleteRent() {
        if (tblRents.getSelectedRow() == -1)
            return;

        Rent rent = rtm.getRentAt(tblRents.getSelectedRow());
        RentService.deleteRent(rent);
        loadRents(this.selectedUnit);
        loadReceivables(this.selectedUnit);
    }

    private void onCheckCredit() {
        if (tblCredit.getSelectedRow() == -1)
            return;

        Credit credit = ctm.getCreditAt(tblCredit.getSelectedRow());
        new CreditCheckWindow(this, this.selectedUnit, credit);
    }

    private void onDeleteUnit() {
        if (this.selectedUnit == null) { return;}
        UnitService.deleteUnit(this.selectedUnit);

        this.selectedUnit = null;
        utm = new UnitComboBoxModel(new ArrayList<>());
        cmbUnits.setModel(utm);
        setTabPanelStatus(false);
        setFields(null);
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

    public void eventAddNewRent(Rent r) {
        RentService.addRent(r);
        loadRents(this.selectedUnit);
        loadReceivables(this.selectedUnit);
    }

    public void eventAddNewTransaction(Transaction t) {
        TransactionService.addTransaction(t);
        loadTransactions(this.selectedUnit);
    }

    public void eventAddNewCredit(Credit c) {
        CreditService.addCredit(c);
        loadCredits(this.selectedUnit);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
