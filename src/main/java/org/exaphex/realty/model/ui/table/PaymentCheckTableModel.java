package org.exaphex.realty.model.ui.table;

import org.exaphex.realty.model.PaymentCheck;

import javax.swing.table.AbstractTableModel;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class PaymentCheckTableModel extends AbstractTableModel {
    private List<PaymentCheck> paymentChecks;

    public PaymentCheckTableModel(List<PaymentCheck> paymentChecks) {
        this.paymentChecks = new ArrayList<>(paymentChecks);
    }

    public void setPaymentChecks(List<PaymentCheck> paymentChecks) {
        this.paymentChecks = new ArrayList<>(paymentChecks);
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return paymentChecks.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public String getColumnName(int column) {
        return switch (column) {
            case 0:
                yield "Name";
            case 1:
                yield "Amount";
            case 2:
                yield "Date";
            case 3:
                yield "Paid amount";
            default:
                yield "";
        };
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        PaymentCheck paymentCheck = paymentChecks.get(rowIndex);
        return switch (columnIndex) {
            case 0:
                yield paymentCheck.getName();
            case 1:
                yield formatter.format(paymentCheck.getAmount());
            case 2:
                yield paymentCheck.getDate();
            case 3:
                yield formatter.format(paymentCheck.getPaidAmount());
            default:
                yield "??";
        };
    }

    public PaymentCheck getPaymentCheck(int row) {
        return paymentChecks.get(row);
    }
}
