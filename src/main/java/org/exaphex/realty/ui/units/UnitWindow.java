package org.exaphex.realty.ui.units;

import com.opencsv.bean.CsvToBeanBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.exaphex.realty.db.service.*;
import org.exaphex.realty.model.*;
import org.exaphex.realty.model.transport.ValuationTransportModel;
import org.exaphex.realty.model.ui.cmb.UnitComboBoxModel;
import org.exaphex.realty.model.ui.table.*;
import org.exaphex.realty.ui.credit.CreditPane;
import org.exaphex.realty.ui.overview.OverviewPane;

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
import java.util.*;
import java.util.List;

import static org.exaphex.realty.processor.CreditProcessor.getPaidAmount;
import static org.exaphex.realty.util.DateUtils.safeFormatDate;
import static org.exaphex.realty.util.DateUtils.setDateSorter;
import static org.exaphex.realty.util.PriceUtils.setPriceSorter;
import static org.exaphex.realty.util.PriceUtils.validatePrice;

public class UnitWindow {
    private final ResourceBundle res = ResourceBundle.getBundle("i18n");
    protected static final Logger logger = LogManager.getLogger();
    UnitComboBoxModel utm = new UnitComboBoxModel(new ArrayList<>());
    final ValuationTableModel vtm = new ValuationTableModel(new ArrayList<>());
    final RentTableModel rtm = new RentTableModel(new ArrayList<>());
    final TransactionTableModel ttm = new TransactionTableModel(new ArrayList<>());
    final CreditTableModel ctm = new CreditTableModel(new ArrayList<>());
    Unit selectedUnit;
    private Building building;
    private JComboBox<Unit> cmbUnits;
    private JButton btnAddUnit;
    private JButton btnDeleteUnit;
    private JTabbedPane tabPane;
    private JTextField txtName;
    private JTextField txtArea;
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
    private JButton btnDeleteTransaction;
    private JPanel paneCredit;
    private JTable tblCredit;
    private JButton btnCheckCredit;
    private JButton btnCheckRent;
    private JPanel mainUnitPanel;
    private JButton btnSave;
    private OverviewPane overviewPane;
    private JTextField txtShares;
    private CreditPane creditPane;

    public void setUI(Building b) {
        this.building = b;
        buildUI();
        setListeners();
        this.creditPane.init();
        loadUnits(this.building);
    }

    private void buildUI() {
        cmbUnits.setModel(utm);
        tblValuations.setModel(vtm);
        tblRents.setModel(rtm);
        tblAccount.setModel(ttm);

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

        cmbUnits.addItemListener(event -> {
            if (event.getStateChange() == ItemEvent.SELECTED) {
                Unit item = (Unit) event.getItem();
                selectUnit(item);
            }
        });

        btnSave.addActionListener( e -> onUpdateUnit());

        tabPane.addChangeListener(e -> {
            onSelectTab();



        });



        tblRents.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouseEvent) {
                JTable table = (JTable) mouseEvent.getSource();
                int selectedRow = table.getSelectedRow();
                if (mouseEvent.getClickCount() == 2 && selectedRow != -1) {
                    int selectedModelRow = tblRents.convertRowIndexToModel(selectedRow);
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

    private void onSelectTab() {
        Component selectedComponent = tabPane.getSelectedComponent();

        if (this.selectedUnit == null) {
            return;
        }

        if (selectedComponent.equals(paneOverview)) {
            loadOverviewData(this.selectedUnit);
        } else if (selectedComponent.equals(paneGeneral)) {
            setFields(this.selectedUnit);
        } else if (selectedComponent.equals(paneCredit)) {
            creditPane.setUI(this.selectedUnit);
        }
    }

    private void selectUnit(Unit u) {
        this.selectedUnit = u;
        setTabPanelStatus(true);
        loadValuations(this.selectedUnit);
        loadRents(this.selectedUnit);
        loadTransactions(this.selectedUnit);
        loadCredits(this.selectedUnit);
        loadOverviewData(u);
        setFields(u);
    }

    private void setFields(Unit u) {
        Unit unit = UnitService.getUnitById(u.getId());
        this.txtName.setText(unit != null ? unit.getName() : "");
        this.txtArea.setText(unit!= null ? ""+unit.getArea() : "");
        this.txtShares.setText(unit != null ? ""+unit.getShares():"");
    }

    private void loadOverviewData(Unit u) {
        List<Rent> rents = new ArrayList<>();
        List<Transaction> transactions = TransactionService.getTransactions(u);
        List<Credit> credits = CreditService.getCredit(u);
        List<Valuation> valuations = new ArrayList<>();
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

        this.overviewPane.loadData(rents, transactions, valuations, credits);
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

    private void onUpdateUnit() {
        Float fArea = validatePrice(txtArea.getText(), res.getString("msgArea"));
        if (fArea == null || fArea <= 0) {
            return;
        }

        Float fShares = validatePrice(txtShares.getText(), res.getString("msgShares"));
        if (fShares == null || fShares <= 0) {
            return;
        }

        UnitService.updateUnit(new Unit(this.selectedUnit.getId(), this.selectedUnit.getBuildingId(), this.selectedUnit.getName(), fArea, fShares));
        setFields(this.selectedUnit);
    }

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

    private void onDeleteRent() {
        if (tblRents.getSelectedRow() == -1)
            return;

        Rent rent = rtm.getRentAt(tblRents.convertRowIndexToModel(tblRents.getSelectedRow()));
        RentService.deleteRent(rent);
        loadRents(this.selectedUnit);
    }

    private void onDeleteUnit() {
        if (this.selectedUnit == null) { return;}
        UnitService.deleteUnit(this.selectedUnit);

        this.selectedUnit = null;
        utm = new UnitComboBoxModel(new ArrayList<>());
        cmbUnits.setModel(utm);
        setTabPanelStatus(false);
        this.overviewPane.clearData();
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
}
