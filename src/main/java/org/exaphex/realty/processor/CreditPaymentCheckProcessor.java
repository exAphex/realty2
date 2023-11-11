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

import static org.exaphex.realty.util.DateUtils.safeFormatDate;

public class CreditPaymentCheckProcessor {
    protected static final Logger logger = LogManager.getLogger();

    public static List<PaymentCheck> getCreditPaymentCheck(Unit u, List<Credit> credits, List<Transaction> transactions) {
        List<PaymentCheck> retPaymentChecks = new ArrayList<>();
        transactions.sort((lhs, rhs) -> {
            Date sfLhs = safeFormatDate(lhs.getDate());
            Date sfRhs = safeFormatDate(rhs.getDate());
            return sfLhs.after(sfRhs) ? -1 : sfLhs.before(sfRhs) ? 1 : 0;
        });
        for (Credit credit : credits) {
            retPaymentChecks.addAll(calculatePaymentChecks(credit, transactions));
        }
        return enrichWithTransaction(u,retPaymentChecks);
    }

    private static List<PaymentCheck> calculatePaymentChecks(Credit credit, List<Transaction> transactions) {
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        DateFormat monthFormatter = new SimpleDateFormat("MM-yyyy");
        Calendar beginCalendar = Calendar.getInstance();
        Calendar finishCalendar = Calendar.getInstance();
        Calendar todayCalendar = Calendar.getInstance();
        List<PaymentCheck> retPaymentChecks = new ArrayList<>();
        float installment = credit.getInstallmentAmount();
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
                finishCalendar.add(Calendar.MONTH,1);
            }

            while (beginCalendar.before(finishCalendar)) {
                PaymentCheck tempPaymentCheck = new PaymentCheck("Credit payment " + monthFormatter.format(beginCalendar.getTime()), installment, formatter.format(beginCalendar.getTime()), 0 );
                tempPaymentCheck.setPaidAmount(donePayments(tempPaymentCheck, transactions));
                retPaymentChecks.add(tempPaymentCheck);
                beginCalendar.add(Calendar.MONTH, 1);
            }
        } catch (ParseException e) {
            logger.error(e);
        }
        return retPaymentChecks;
    }

    private static float donePayments(PaymentCheck paymentCheck, List<Transaction> transactions) {
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
            retPaid = transactions.stream().filter(t -> t.getType() == Transaction.CREDIT_PAYMENT).filter(t -> {
                try {
                    Date tempTransactionDate = formatter.parse(t.getDate());
                    return (tempTransactionDate.after(paymentCheckDateL) && tempTransactionDate.before(paymentCheckDateR));
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

    private static List<PaymentCheck> enrichWithTransaction(Unit u, List<PaymentCheck> paymentChecks) {
        for (PaymentCheck p : paymentChecks) {
            p.setTransaction(new Transaction(p.getDate(),Transaction.CREDIT_PAYMENT, u.getId(),0,0));
        }
        return paymentChecks;
    }
}
