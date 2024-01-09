package org.exaphex.realty.ui.settings;

import org.exaphex.realty.model.ExpenseCategory;
import org.exaphex.realty.model.ExpenseCategoryCalculationType;
import org.exaphex.realty.model.ui.cmb.CreditComboBoxModel;
import org.exaphex.realty.model.ui.cmb.ExpenseCategoryTypeComboBoxModel;
import org.exaphex.realty.ui.MainWindow;

import javax.swing.*;
import java.util.ResourceBundle;

public class CategoryModal {
    private final ResourceBundle res = ResourceBundle.getBundle("i18n");
    private final ExpenseCategoryTypeComboBoxModel ctm = new ExpenseCategoryTypeComboBoxModel();
    private JTextField txtName;
    private JCheckBox chkIsWrapable;
    private JComboBox<ExpenseCategoryCalculationType> cmbType;
    private JButton btnSave;
    private JPanel mainPanel;
    private MainWindow mw;
    private JDialog dialog;
    private ExpenseCategory selectedCategory;

    public CategoryModal(MainWindow mw) {
        this.mw = mw;
        setupUI();
        setupListeners();
    }

    public CategoryModal(MainWindow mw, ExpenseCategory category) {
        this.mw = mw;
        this.selectedCategory = category;
        setupEditUI();
        setupListeners();
    }

    private void setupEditUI() {
        setupUI();
        ExpenseCategoryTypeComboBoxModel categoryModel = (ExpenseCategoryTypeComboBoxModel) cmbType.getModel();
        this.txtName.setText(this.selectedCategory.getName());
        this.chkIsWrapable.setSelected(this.selectedCategory.isWrapable());

        int pos = categoryModel.getCategoryById(this.selectedCategory.getCalculationType());
        if (pos >= 0) {
            cmbType.setSelectedIndex(pos);
        }
    }
    private void setupUI() {
        cmbType.setModel(ctm);
        cmbType.setSelectedIndex(0);

        this.dialog = new JDialog();
        dialog.setTitle(res.getString("titleManageCategory"));
        dialog.add(mainPanel);
        dialog.pack();
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    private void setupListeners() {
        btnSave.addActionListener(
                e -> {
                    String strName = txtName.getText();
                    if (strName.isEmpty()) {
                        JOptionPane.showMessageDialog(new JFrame(), res.getString("msgNameInvalid"), res.getString("msgError"),
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    ExpenseCategoryCalculationType selectedType = (ExpenseCategoryCalculationType) cmbType.getSelectedItem();
                    assert selectedType != null;
                    if (this.selectedCategory != null) {
                        mw.eventEditCategory(new ExpenseCategory(this.selectedCategory.getId(), strName,chkIsWrapable.isSelected(),selectedType.getId()));
                    } else {
                        mw.eventAddNewCategory(new ExpenseCategory(strName,chkIsWrapable.isSelected(),selectedType.getId()));
                    }
                    dialog.dispose();
                });
    }
}
