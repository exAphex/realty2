package org.exaphex.realty.ui.overview;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.exaphex.realty.model.*;

import javax.swing.*;
import java.text.*;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class OverviewPane {
    protected static final Logger logger = LogManager.getLogger();
    private JLabel lblObjectValue;
    private JLabel lblOwnCapital;
    private JLabel lblROI;
    private JLabel lblROC;
    private JLabel lblRentMonthly;
    private JLabel lblRentPerAnnum;
    private JLabel lblRentAcc;
    private JLabel lblTotalCredit;
    private JLabel lblPaidInterest;
    private JLabel lblPaidPrincipal;
    private JLabel lblLeftCredit;
    private JPanel mainPanel;
    private JLabel lblAverageRentPerSQM;
    private final NumberFormat formatter = NumberFormat.getCurrencyInstance();
    private final DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
    private final DecimalFormat decimalFormatter = new DecimalFormat("##.##%");

    public void clearData() {
        lblRentMonthly.setText(formatter.format(0));
        lblTotalCredit.setText(formatter.format(0));
        lblPaidPrincipal.setText(formatter.format(0));
        lblLeftCredit.setText(formatter.format(0));
        lblPaidInterest.setText(formatter.format(0));
        lblObjectValue.setText(formatter.format(0));
        lblOwnCapital.setText(formatter.format(0));
        lblRentPerAnnum.setText(formatter.format(0));
        lblRentAcc.setText(formatter.format(0));
        lblROC.setText(decimalFormatter.format(0));
        lblROI.setText(decimalFormatter.format(0));
    }
    public void loadData(List<Unit> units, List<Rent> rents, List<Transaction> transactions, List<Valuation> valuations, List<Credit> credits) {
        List<Transaction> rentTransactions = transactions.stream().filter(t -> t.getType() == Transaction.RENT_PAYMENT).toList();
        Date now = new Date();
        Calendar beginCalendar = Calendar.getInstance();
        beginCalendar.setTime(now);
        beginCalendar.set(Calendar.DAY_OF_MONTH, 1);
        beginCalendar.add(Calendar.MONTH, -12);

        float fTotalCredit = credits.stream().map(Credit::getAmount).reduce(0f, Float::sum);
        float fPaidPrincipal = transactions.stream().filter(t -> t.getType() == Transaction.CREDIT_PAYMENT).map(Transaction::getAmount).reduce(0f, Float::sum);
        float fRemaining = fTotalCredit - fPaidPrincipal;
        float fPaidInterest = transactions.stream().filter(t -> t.getType() == Transaction.CREDIT_PAYMENT).map(Transaction::getSecondaryAmount).reduce(0f, Float::sum);
        float fTotalValue = valuations.stream().map(Valuation::getValue).reduce(0f, Float::sum);
        float fOwnCapital = fTotalValue - fRemaining;
        float fRentMonthly = rents.stream().map(Rent::getRentalPrice).reduce(0f, Float::sum);
        float paidRentAcc = rentTransactions.stream().map(Transaction::getAmount).reduce(0f, Float::sum);
        float paidRentPerAnnum = rentTransactions.stream().filter(t -> {
            try {
                Date tempTransactionDate = dateFormatter.parse(t.getDate());
                return (tempTransactionDate.after(beginCalendar.getTime()));
            } catch (ParseException e) {
                logger.error(e);
            }
            return false;
        }).map(Transaction::getAmount).reduce(0f, Float::sum);
        float fROI = fTotalValue > 0 ? (fRentMonthly * 12) / fTotalValue : 0;
        float fROC = fOwnCapital > 0 ? (fRentMonthly * 12) / fOwnCapital : 0;
        float fTotalArea = units.stream().map(Unit::getArea).reduce(0f, Float::sum);
        float fAverageRentPerSQM = fRentMonthly / fTotalArea;

        lblRentMonthly.setText(formatter.format(fRentMonthly));
        lblTotalCredit.setText(formatter.format(fTotalCredit));
        lblPaidPrincipal.setText(formatter.format(fPaidPrincipal));
        lblLeftCredit.setText(formatter.format(fRemaining));
        lblPaidInterest.setText(formatter.format(fPaidInterest));
        lblObjectValue.setText(formatter.format(fTotalValue));
        lblOwnCapital.setText(formatter.format(fOwnCapital));
        lblRentPerAnnum.setText(formatter.format(paidRentPerAnnum));
        lblRentAcc.setText(formatter.format(paidRentAcc));
        lblROC.setText(decimalFormatter.format(fROC));
        lblROI.setText(decimalFormatter.format(fROI));
        lblAverageRentPerSQM.setText(formatter.format(fAverageRentPerSQM)+"/m²");
    }

}
