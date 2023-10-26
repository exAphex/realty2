package org.exaphex.realty.db.service;

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

public class ReceivableService {

    public static List<Receivable> getReceivables(Unit u) {
        List<Rent> rents = RentService.getRents(u);
        List<Receivable> receivables = new ArrayList<>();
        for (Rent r : rents) {
            receivables.addAll(calculateReceivables(r));
        }
        return receivables;
    }

    private static List<Receivable> calculateReceivables(Rent r) {
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
            retReceivables.add(new Receivable("Deposit", r.getDeposit(), formatter.format(beginCalendar.getTime()), Transaction.DEPOSIT));

            // Only list receivables till today
            if (finishCalendar.after(todayCalendar)) {
                finishCalendar.setTime(new Date());
                finishCalendar.add(Calendar.MONTH,1);
            }

            while (beginCalendar.before(finishCalendar)) {
                Receivable tempReceivable = new Receivable("Rent " + monthFormatter.format(beginCalendar.getTime()), r.getRentalPrice() + r.getExtraCosts(), formatter.format(beginCalendar.getTime()), Transaction.RENT_PAYMENT);
                retReceivables.add(tempReceivable);
                beginCalendar.add(Calendar.MONTH, 1);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return retReceivables;
    }
}
