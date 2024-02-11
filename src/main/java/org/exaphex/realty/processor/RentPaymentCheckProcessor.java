package org.exaphex.realty.processor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.exaphex.realty.db.service.RentService;
import org.exaphex.realty.db.service.TransactionService;
import org.exaphex.realty.db.service.UnitService;
import org.exaphex.realty.model.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RentPaymentCheckProcessor {
    protected static final Logger logger = LogManager.getLogger();
    public static List<PaymentCheck> getRentPaymentCheck(Unit u, Rent r, List<Transaction> transactions) {
        return calculatePaymentChecks(u, r, transactions);
    }

    public static List<PaymentCheck> getRentPaymentCheck(Building b) {
        List<Unit> units = UnitService.getUnits(b);
        List<PaymentCheck> paymentChecks = new ArrayList<>();
        for (Unit u : units) {
            List<Rent> rents = RentService.getRents(u);
            List<Transaction> transactions = TransactionService.getTransactions(u);
            for (Rent r : rents) {
                paymentChecks.addAll(calculatePaymentChecks(u, r, transactions));
            }
        }
        return paymentChecks;
    }

    private static List<PaymentCheck> calculatePaymentChecks(Unit u, Rent r, List<Transaction> transactions) {
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        DateFormat monthFormatter = new SimpleDateFormat("MM-yyyy");
        Calendar beginCalendar = Calendar.getInstance();
        Calendar finishCalendar = Calendar.getInstance();
        Calendar todayCalendar = Calendar.getInstance();
        List<PaymentCheck> retPaymentChecks = new ArrayList<>();
        float rent = r.getRentalPrice();
        float extraCosts = r.getExtraCosts();
        float total = rent + extraCosts;
        try {
            beginCalendar.setTime(formatter.parse(r.getStartDate()));
            finishCalendar.setTime(formatter.parse(r.getEndDate()));
            todayCalendar.setTime(new Date());

            // Credits starts in the future, quit
            if (todayCalendar.before(beginCalendar)) {
                return retPaymentChecks;
            }

            // Only list credit payments til today
            if (finishCalendar.after(todayCalendar)) {
                finishCalendar.setTime(new Date());
            }

            while (beginCalendar.before(finishCalendar)) {
                PaymentCheck tempPaymentCheck = new PaymentCheck("Rent payment " + monthFormatter.format(beginCalendar.getTime()), total, formatter.format(beginCalendar.getTime()), 0 );
                tempPaymentCheck.setPaidAmount(donePayments(r, tempPaymentCheck, transactions));
                tempPaymentCheck.setTransaction(new Transaction("Rent " + r.getContact().getFirstName() + " " + r.getContact().getLastName() + " " + monthFormatter.format(beginCalendar.getTime()), r.getId(), formatter.format(beginCalendar.getTime()), Transaction.RENT_PAYMENT, u.getId() ,rent, extraCosts, ""));
                retPaymentChecks.add(tempPaymentCheck);
                beginCalendar.add(Calendar.MONTH, 1);
            }
        } catch (ParseException e) {
            logger.error(e);
        }
        return retPaymentChecks;
    }

    private static float donePayments(Rent r, PaymentCheck paymentCheck, List<Transaction> transactions) {
        Calendar monthCalendar = Calendar.getInstance();
        Date paymentCheckDateL;
        Date paymentCheckDateR;
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        float retPaid = 0;
        try {
            monthCalendar.setTime(formatter.parse(paymentCheck.getDate()));
            monthCalendar.set(Calendar.DAY_OF_MONTH, 1);
            paymentCheckDateL = monthCalendar.getTime();
            monthCalendar.add(Calendar.MONTH, 1);
            paymentCheckDateR = monthCalendar.getTime();
            retPaid = transactions.stream().filter(t -> t.getType() == Transaction.RENT_PAYMENT).filter(t -> r.getId().equals(t.getReference())).filter(t -> {
                try {
                    Date tempTransactionDate = formatter.parse(t.getDate());
                    return (tempTransactionDate.equals(paymentCheckDateL) || (tempTransactionDate.after(paymentCheckDateL) && tempTransactionDate.before(paymentCheckDateR)));
                } catch (ParseException e) {
                    logger.error(e);
                }
                return false;
            }).map(t -> t.getAmount() + t.getSecondaryAmount()).reduce(0f, Float::sum);
        } catch (ParseException e) {
            logger.error(e);
        }
        return retPaid;
    }
}
