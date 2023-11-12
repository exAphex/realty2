package org.exaphex.realty.db.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.exaphex.realty.model.Receivable;
import org.exaphex.realty.model.Rent;
import org.exaphex.realty.model.Transaction;
import org.exaphex.realty.model.Unit;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.exaphex.realty.util.DateUtils.safeFormatDate;

public class ReceivableService {
    protected static final Logger logger = LogManager.getLogger();

    public static List<Receivable> getReceivables(Unit u) {
        List<Rent> rents = RentService.getRents(u);
        List<Receivable> receivables = new ArrayList<>();
        for (Rent r : rents) {
            receivables.addAll(calculateReceivables(r, u));
        }
        enrichReceivablesWithTransactions(u, receivables);
        return receivables;
    }

    public static void setFullPayment(Receivable receivable) {
        setPartialPayment(receivable, receivable.getAmount());
    }

    public static void setPartialPayment(Receivable receivable, float amount) {
        Transaction transaction = receivable.getTransaction();
        if (transaction == null) {
            transaction = new Transaction("","",receivable.getDue(), receivable.getType(), receivable.getUnit().getId(), amount);
            TransactionService.addTransaction(transaction);
        } else {
            transaction.setAmount(amount);
            TransactionService.updateTransaction(transaction);
        }
    }

    public static void deletePayment(Receivable receivable) {
        Transaction transaction = receivable.getTransaction();
        if (transaction == null) {
            return;
        }

        TransactionService.deleteTransaction(transaction);
    }

    private static void enrichReceivablesWithTransactions(Unit u, List<Receivable> receivables) {
        Calendar tmpReceivableCalendar = Calendar.getInstance();
        Calendar tmpTransactionCalendar = Calendar.getInstance();
        List<Transaction> transactions = TransactionService.getTransactions(u);
        for (Receivable r : receivables) {

            Date formattedDate = safeFormatDate(r.getDue());
            if (formattedDate == null) {
                break;
            }

            tmpReceivableCalendar.setTime(formattedDate);
            for (Transaction t : transactions) {
                Date formattedTransactionDate = safeFormatDate(t.getDate());
                if (formattedTransactionDate == null) {
                    break;
                }

                tmpTransactionCalendar.setTime(formattedTransactionDate);
                if (t.getType() == r.getType() && tmpTransactionCalendar.get(Calendar.YEAR) == tmpReceivableCalendar.get(Calendar.YEAR) && tmpTransactionCalendar.get(Calendar.MONTH) == tmpReceivableCalendar.get(Calendar.MONTH)) {
                    r.setTransaction(t);
                    break;
                }
            }
        }
    }

    private static List<Receivable> calculateReceivables(Rent r, Unit u) {
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        DateFormat monthFormatter = new SimpleDateFormat("MM-yyyy");
        Calendar beginCalendar = Calendar.getInstance();
        Calendar finishCalendar = Calendar.getInstance();
        Calendar todayCalendar = Calendar.getInstance();
        List<Receivable> retReceivables = new ArrayList<>();
        try {
            beginCalendar.setTime(formatter.parse(r.getStartDate()));
            finishCalendar.setTime(formatter.parse(r.getEndDate()));
            todayCalendar.setTime(new Date());

            // Rent starts in the future, quit
            if (todayCalendar.before(beginCalendar)) {
                return retReceivables;
            }

            // Deposit
            retReceivables.add(new Receivable("Deposit", r.getDeposit(), formatter.format(beginCalendar.getTime()), Transaction.DEPOSIT, u));

            // Only list receivables till today
            if (finishCalendar.after(todayCalendar)) {
                finishCalendar.setTime(new Date());
                finishCalendar.add(Calendar.MONTH,1);
            }

            while (beginCalendar.before(finishCalendar)) {
                Receivable tempReceivable = new Receivable("Rent " + monthFormatter.format(beginCalendar.getTime()), r.getRentalPrice() + r.getExtraCosts(), formatter.format(beginCalendar.getTime()), Transaction.RENT_PAYMENT, u);
                retReceivables.add(tempReceivable);
                beginCalendar.add(Calendar.MONTH, 1);
            }
        } catch (ParseException e) {
            logger.error(e);
        }
        return retReceivables;
    }


}
