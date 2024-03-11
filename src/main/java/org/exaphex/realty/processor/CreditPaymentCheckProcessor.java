package org.exaphex.realty.processor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.exaphex.realty.model.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CreditPaymentCheckProcessor {
    protected static final Logger logger = LogManager.getLogger();

    public static List<PaymentCheck> getCreditPaymentCheck(Unit u, Credit credit, List<Transaction> transactions) {
        return _calculatePaymentChecks(u.getId(), credit, transactions);
    }

    public static List<PaymentCheck> getCreditPaymentCheck(Building building, Credit credit, List<Transaction> transactions) {
        return _calculatePaymentChecks(building.getId(), credit, transactions);
    }

    private static List<PaymentCheck> _calculatePaymentChecks(String id, Credit credit, List<Transaction> transactions) {
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        DateFormat monthFormatter = new SimpleDateFormat("MM-yyyy");
        Calendar beginCalendar = Calendar.getInstance();
        Calendar finishCalendar = Calendar.getInstance();
        Calendar todayCalendar = Calendar.getInstance();
        List<PaymentCheck> retPaymentChecks = new ArrayList<>();
        float interest;
        float redemption;
        float totalInstallment = credit.getInstallmentAmount();
        float total = credit.getAmount();
        try {
            beginCalendar.setTime(formatter.parse(credit.getStartDate()));
            finishCalendar.setTime(formatter.parse(credit.getEndDate()));
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
                interest = (total * credit.getInterestRate()) / 12;
                redemption = totalInstallment - interest;
                total -= redemption;
                PaymentCheck tempPaymentCheck = new PaymentCheck("Credit payment " + monthFormatter.format(beginCalendar.getTime()), totalInstallment, formatter.format(beginCalendar.getTime()), 0 );
                tempPaymentCheck.setPaidAmount(donePayments(credit, tempPaymentCheck, transactions));
                tempPaymentCheck.setTransaction(new Transaction("Credit payment " + credit.getName() + " " + monthFormatter.format(beginCalendar.getTime()), credit.getId(), formatter.format(beginCalendar.getTime()), Transaction.CREDIT_PAYMENT, id ,redemption, interest,"", ""));
                retPaymentChecks.add(tempPaymentCheck);
                beginCalendar.add(Calendar.MONTH, 1);
            }
        } catch (ParseException e) {
            logger.error(e);
        }
        return retPaymentChecks;
    }

    private static float donePayments(Credit credit, PaymentCheck paymentCheck, List<Transaction> transactions) {
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
            retPaid = transactions.stream().filter(t -> t.getType() == Transaction.CREDIT_PAYMENT).filter(t -> credit.getId().equals(t.getReference())).filter(t -> {
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
