package org.exaphex.realty.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    public static Date safeFormatDate(String date) {
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date retDate = null;
        try {
            retDate = formatter.parse(date);
        } catch (ParseException e) {
            // TODO: better error handling
        }
        return retDate;
    }
}
