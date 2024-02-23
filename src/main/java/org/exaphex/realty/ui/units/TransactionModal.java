package org.exaphex.realty.ui.units;

import org.exaphex.realty.db.service.*;
import org.exaphex.realty.model.*;
import org.exaphex.realty.model.ui.cmb.*;

import javax.swing.*;
import javax.swing.text.DateFormatter;
import javax.swing.text.DefaultFormatterFactory;
import java.awt.event.ItemEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import static org.exaphex.realty.model.Transaction.formatTransactionType;
import static org.exaphex.realty.model.Transaction.getTypeIdByTransactionType;
import static org.exaphex.realty.util.PriceUtils.validatePrice;

public class TransactionModal {
    private final ResourceBundle res = ResourceBundle.getBundle("i18n");
    private final CreditComboBoxModel ccm = new CreditComboBoxModel(new ArrayList<>());
    private final RentComboBoxModel rcm = new RentComboBoxModel(new ArrayList<>());
    private final ExpenseCategoryComboBoxModel ecm = new ExpenseCategoryComboBoxModel(new ArrayList<>());
    private final AccountComboBoxModel acm = new AccountComboBoxModel(new ArrayList<>());
    private final UnitWindow uw;
    private Unit unit;
    private Transaction transaction;
    private JTextField txtAmount;
    private JFormattedTextField txtDate;
    private JButton btnSave;
    private JPanel mainPanel;
    private JDialog dialog;
    private JComboBox<String> cmbTypes;
    private JFormattedTextField txtSecondary;
    private JLabel lblSecondary;
    private JTextField txtDescription;
    private JLabel lblCreditReference;
    private JLabel lblRentReference;
    private JComboBox<Credit> cmbCredit;
    private JComboBox<Rent> cmbRent;
    private JComboBox<ExpenseCategory> cmbExpenseType;
    private JLabel lblExpenseCategory;
    private JComboBox<Account> cmbAccount;

    public TransactionModal(UnitWindow uw, Unit u) {
        this.uw = uw;
        this.unit = u;
        setupUI();
        setupListeners();
        loadData();
    }

    public TransactionModal(UnitWindow uw, Transaction transaction) {
        this.uw = uw;
        this.transaction = transaction;
        setupUI();
        setupListeners();
        loadData();
        setupEditUI();
    }

    private void setupEditUI() {
        String reference = this.transaction.getReference();
        txtDate.setText(this.transaction.getDate());
        txtDescription.setText(this.transaction.getDescription());
        txtSecondary.setText(this.transaction.getSecondaryAmount()+"");
        txtAmount.setText(this.transaction.getAmount()+"");
        cmbTypes.setSelectedItem(formatTransactionType(this.transaction.getType()));
        CreditComboBoxModel creditModel = (CreditComboBoxModel) cmbCredit.getModel();
        RentComboBoxModel rentModel = (RentComboBoxModel) cmbRent.getModel();
        AccountComboBoxModel accountModel = (AccountComboBoxModel) cmbAccount.getModel();
        ExpenseCategoryComboBoxModel expenseCategoryModel = (ExpenseCategoryComboBoxModel) cmbExpenseType.getModel();
        switch (this.transaction.getType()) {
            case Transaction.CREDIT_PAYMENT -> {
                int pos = creditModel.getCreditIndexById(reference);
                if (pos >= 0) {
                    cmbCredit.setSelectedIndex(pos);
                }
            }
            case Transaction.RENT_PAYMENT -> {
                int pos = rentModel.getRentIndexById(reference);
                if (pos >= 0) {
                    cmbRent.setSelectedIndex(pos);
                }
            }
            case Transaction.EXPENSE -> {
                int pos = expenseCategoryModel.getExpenseCategoryIndexById(this.transaction.getExpenseCategory());
                if (pos >= 0) {
                    cmbExpenseType.setSelectedIndex(pos);
                }
            }
        }

        int pos = accountModel.getAccountById(this.transaction.getAccountId());
        if (pos >= 0) {
            cmbAccount.setSelectedIndex(pos);
        }
    }

    private void loadData() {
        if (this.unit != null) {
            loadCredits(this.unit);
            loadRents(this.unit);
        } else {
            Unit u = UnitService.getUnitById(this.transaction.getUnitId());
            loadCredits(u);
            loadRents(u);
        }
        loadExpenseCategories();
        loadAccounts();
    }

    private void setupUI() {
        DateFormatter df = new DateFormatter(new SimpleDateFormat("dd-MM-yyyy"));
        DefaultFormatterFactory dff = new DefaultFormatterFactory(df, df, df, df);
        txtDate.setFormatterFactory(dff);

        SimpleDateFormat DateFor = new SimpleDateFormat("dd-MM-yyyy");
        txtDate.setText(DateFor.format(new Date()));
        txtAmount.setText("0.00");
        txtSecondary.setText("0.00");

        cmbTypes.addItem(formatTransactionType(0));
        cmbTypes.addItem(formatTransactionType(1));
        cmbTypes.addItem(formatTransactionType(2));
        cmbTypes.addItem(formatTransactionType(3));

        cmbCredit.setModel(ccm);
        cmbRent.setModel(rcm);
        cmbExpenseType.setModel(ecm);
        cmbAccount.setModel(acm);

        this.dialog = new JDialog();
        dialog.setTitle(res.getString("titleAddTransaction"));
        dialog.add(mainPanel);
        dialog.pack();
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);

    }

    private void setupListeners() {
        btnSave.addActionListener(
                e -> {
                    String reference = "";
                    String expenseCategory = "";
                    Account account = (Account) cmbAccount.getSelectedItem();
                    if (account == null) {
                        JOptionPane.showMessageDialog(new JFrame(), res.getString("msgInvalidAccount"), res.getString("msgError"),
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (txtDate.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(new JFrame(), res.getString("msgDateInvalid"), res.getString("msgError"),
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    Float fAmount = validatePrice(txtAmount.getText(), res.getString("msgAmount"));
                    if (fAmount == null) {
                        return;
                    }

                    Float fSecondaryAmount = validatePrice(txtSecondary.getText(), res.getString("msgSecondaryAmount"));
                    if (fSecondaryAmount == null) {
                        return;
                    }

                    String selectedType = (String) cmbTypes.getSelectedItem();
                    assert selectedType != null;
                    int type = getTypeIdByTransactionType(selectedType);

                    if (type == Transaction.RENT_PAYMENT) {
                        Rent rent = (Rent) cmbRent.getSelectedItem();
                        if (rent == null) {
                            JOptionPane.showMessageDialog(new JFrame(), res.getString("msgInvalidRentReference"), res.getString("msgError"),
                                    JOptionPane.ERROR_MESSAGE);
                            return;
                        } else {
                            reference = rent.getId();
                        }
                    }

                    if (type == Transaction.CREDIT_PAYMENT) {
                        Credit credit = (Credit) cmbCredit.getSelectedItem();
                        if (credit == null) {
                            JOptionPane.showMessageDialog(new JFrame(), res.getString("msgInvalidCreditReference"), res.getString("msgError"),
                                    JOptionPane.ERROR_MESSAGE);
                            return;
                        } else {
                            reference = credit.getId();
                        }
                    }

                    if (type == Transaction.EXPENSE) {
                        ExpenseCategory ec = (ExpenseCategory) cmbExpenseType.getSelectedItem();
                        if (ec == null) {
                            JOptionPane.showMessageDialog(new JFrame(), res.getString("msgInvalidExpenseCategory"), res.getString("msgError"),
                                    JOptionPane.ERROR_MESSAGE);
                            return;
                        } else {
                            expenseCategory = ec.getId();
                        }
                    }

                    if (this.transaction != null) {
                        uw.eventEditTransaction(new Transaction(this.transaction.getId(), txtDescription.getText(), reference, txtDate.getText(),type, this.transaction.getUnitId(), fAmount, fSecondaryAmount,expenseCategory, account.getId()));
                    } else {
                        uw.eventAddNewTransaction(new Transaction(txtDescription.getText(), reference, txtDate.getText(),type, this.unit.getId(), fAmount, fSecondaryAmount, expenseCategory, account.getId()));
                    }
                    dialog.dispose();
                });

        cmbTypes.addItemListener(event -> {
            if (event.getStateChange() == ItemEvent.SELECTED) {
                String item = (String) cmbTypes.getSelectedItem();
                int type = 0;
                if (item != null) {
                    type = getTypeIdByTransactionType(item);
                }
                setVisibilities(type);
            }
        });
    }

    private void setVisibilities(int type) {
        lblSecondary.setVisible(false);
        txtSecondary.setVisible(false);
        cmbCredit.setVisible(false);
        cmbRent.setVisible(false);
        lblCreditReference.setVisible(false);
        lblRentReference.setVisible(false);
        lblExpenseCategory.setVisible(false);
        cmbExpenseType.setVisible(false);

        if (type == Transaction.RENT_PAYMENT) {
            lblSecondary.setVisible(true);
            txtSecondary.setVisible(true);
            cmbRent.setVisible(true);
            lblRentReference.setVisible(true);
            lblSecondary.setText(res.getString("lblExtraCosts"));
        } else if (type == Transaction.CREDIT_PAYMENT) {
            lblSecondary.setVisible(true);
            txtSecondary.setVisible(true);
            cmbCredit.setVisible(true);
            lblCreditReference.setVisible(true);
            lblSecondary.setText(res.getString("lblInterest"));
        } else if (type == Transaction.EXPENSE) {
            cmbExpenseType.setVisible(true);
            lblExpenseCategory.setVisible(true);
        }
        this.dialog.pack();
    }

    private void loadCredits(Unit u) {
        List<Credit> credits = CreditService.getCredit(u);
        ccm.setCredits(credits);
    }

    private void loadRents(Unit u) {
        List<Rent> rents = RentService.getRents(u);
        rcm.setRents(rents);
    }

    private void loadExpenseCategories() {
        List<ExpenseCategory> expenseCategories = ExpenseCategoryService.getCategories();
        ecm.setExpenseCategories(expenseCategories);
    }

    private void loadAccounts() {
        List<Account> accounts = AccountService.getAccounts();
        acm.setAccounts(accounts);
    }
}
