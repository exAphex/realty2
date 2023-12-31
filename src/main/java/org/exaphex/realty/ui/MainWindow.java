package org.exaphex.realty.ui;

import org.exaphex.realty.db.service.*;
import org.exaphex.realty.model.*;
import org.exaphex.realty.model.ui.table.BuildingTableModel;
import org.exaphex.realty.processor.CreditProcessor;
import org.exaphex.realty.processor.export.ExportProcessor;
import org.exaphex.realty.ui.buildings.BuildingModal;
import org.exaphex.realty.ui.units.UnitWindow;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import static org.exaphex.realty.processor.CreditProcessor.getTotalAmount;
import static org.exaphex.realty.util.DateUtils.safeFormatDate;

public class MainWindow extends JFrame {
    private final ResourceBundle res = ResourceBundle.getBundle("i18n");
    final BuildingTableModel btm = new BuildingTableModel(new ArrayList<>());

    final List<UnitWindow> uw = new ArrayList<>();
    private JTabbedPane tabbedPane1;
    private JPanel mainPanel;
    private JButton addButton;
    private JButton deleteButton;
    private JTable buildingsTable ;
    private JButton refreshButton;
    private JLabel lblPortfolioValue;
    private JLabel lblRent;
    private JLabel lblCreditAmount;
    private JLabel lblCreditPaid;
    private JLabel lblCreditLeft;
    private JLabel lblPaidRent;
    private JLabel lblPaidInterest;
    private JMenuBar menuBar;
    private JMenu menuFile;
    private JMenuItem menuImportFile;
    private JMenuItem menuExportFile;

    public MainWindow() {
        setContentPane(this.mainPanel);
        setMenu();
        buildingsTable.setModel(btm);
        setListeners();
        loadBuildings();
    }

    private void setMenu() {
        menuBar = new JMenuBar();
        menuFile = new JMenu(res.getString("menuFile"));
        menuImportFile = new JMenuItem(res.getString("menuImport"));
        menuExportFile = new JMenuItem(res.getString("menuExport"));

        menuFile.add(menuImportFile);
        menuFile.add(menuExportFile);

        menuBar.add(menuFile);

        this.setJMenuBar(menuBar);
    }

    public void setListeners() {
        addButton.addActionListener(e -> this.onAddNewBuilding());
        deleteButton.addActionListener(e -> this.onDeleteBuilding());
        buildingsTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouseEvent) {
                JTable table = (JTable) mouseEvent.getSource();
                int selectedRow = table.getSelectedRow();
                if (mouseEvent.getClickCount() == 2 && selectedRow != -1) {
                    int selectedModelRow = buildingsTable.convertColumnIndexToModel(selectedRow);
                    Building selectedBuilding = ((BuildingTableModel) buildingsTable.getModel()).getBuildingAt(selectedModelRow);
                    createBuildingDetailView(selectedBuilding);
                }
            }
        });
        tabbedPane1.addChangeListener(e -> {
            if (tabbedPane1.getSelectedIndex() == 1) {
                loadStatistics();
            }
        });
        menuImportFile.addActionListener(e -> this.onImportFile());
        menuExportFile.addActionListener(e -> this.onExportFile());
    }

    public void onAddNewBuilding() {
        new BuildingModal(this);
    }

    public void onDeleteBuilding() {
        if (buildingsTable.getSelectedRow() == -1)
            return;

        Building building = btm.getBuildingAt(buildingsTable.getSelectedRow());
        int dialogResult = JOptionPane.showConfirmDialog(null,
                res.getString("msgSureYouWantDelete") + building.getName() + "?",
                res.getString("msgWarning"), JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.YES_OPTION) {
            BuildingService.deleteBuilding(building);
            loadBuildings();
        }
    }

    public void eventAddNewBuilding(Building b) {
        BuildingService.addBuilding(b);
        loadBuildings();
    }

    public void loadBuildings() {
        List<Building> buildings = BuildingService.getAllBuildings();
        btm.setBuildings(buildings);
    }

    private void createBuildingDetailView(Building b) {
        for (UnitWindow db : uw) {
            if (db.getBuilding().equals(b)) {
                db.setVisible(true);
                return;
            }
        }

        UnitWindow u = new UnitWindow(b);
        u.pack();
        u.setLocationRelativeTo(null);
        u.setVisible(true);
        this.uw.add(u);
    }

    private void loadStatistics() {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        List<Building> buildings = BuildingService.getAllBuildings();
        List<Transaction> transactions = new ArrayList<>();
        List<Credit> credits = new ArrayList<>();
        List<Unit> units = new ArrayList<>();
        float totalValue = 0;
        float totalRent = 0;
        for (Building building : buildings) {
            units.addAll(UnitService.getUnits(building));
        }

        for (Unit unit : units) {
            transactions.addAll(TransactionService.getTransactions(unit));
            credits.addAll(CreditService.getCredit(unit));
            List<Valuation> valuations = ValuationService.getValuations(unit);
            if (!valuations.isEmpty()) {
                valuations.sort((lhs, rhs) -> {
                    Date sfLhs = safeFormatDate(lhs.getDate());
                    Date sfRhs = safeFormatDate(rhs.getDate());
                    return sfLhs.after(sfRhs) ? -1 : sfLhs.before(sfRhs) ? 1 : 0;
                });
                totalValue += valuations.get(0).getValue();
            }
            List<Rent> rents = RentService.getRents(unit);
            totalRent += rents.stream().filter(e -> {
                Date startDate = safeFormatDate(e.getStartDate());
                Date endDate = safeFormatDate(e.getEndDate());
                Date now = new Date();
                if ((now.before(endDate) || now.equals(endDate)) && (now.after(startDate) || now.equals(startDate))) {
                    return true;
                } else {
                    return false;
                }
            }).map(Rent::getRentalPrice).reduce(0f, Float::sum);
        }

        float totalCredit = credits.stream().map(Credit::getAmount).reduce(0f, Float::sum);
        float paidAmount = transactions.stream().filter(t -> t.getType() == Transaction.CREDIT_PAYMENT).map(Transaction::getAmount).reduce(0f, Float::sum);
        float leftAmount = totalCredit - paidAmount;
        float paidRent = transactions.stream().filter(t -> t.getType() == Transaction.RENT_PAYMENT).map(Transaction::getAmount).reduce(0f, Float::sum);
        float paidInterest = transactions.stream().filter(t -> t.getType() == Transaction.CREDIT_PAYMENT).map(Transaction::getSecondaryAmount).reduce(0f, Float::sum);
        lblPortfolioValue.setText(formatter.format(totalValue));
        lblCreditAmount.setText(formatter.format(totalCredit));
        lblRent.setText(formatter.format(totalRent));
        lblCreditLeft.setText(formatter.format(leftAmount));
        lblCreditPaid.setText(formatter.format(paidAmount));
        lblPaidRent.setText(formatter.format(paidRent));
        lblPaidInterest.setText(formatter.format(paidInterest));
    }

    private void onImportFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(res.getString("titleFileDialog"));

        int userSelection = fileChooser.showOpenDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileImport = fileChooser.getSelectedFile();
            ExportProcessor.importFromFile(fileImport);
            loadBuildings();
            JOptionPane.showMessageDialog(new JFrame(), res.getString("msgFileImported"), res.getString("msgInfo"),
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void onExportFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(res.getString("titleFileDialog"));

        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileExport = fileChooser.getSelectedFile();
            ExportProcessor.exportToFile(fileExport);
            JOptionPane.showMessageDialog(new JFrame(), res.getString("msgFileExported"), res.getString("msgInfo"),
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
