package org.exaphex.realty.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    protected static final Logger logger = LogManager.getLogger();
    public static Date safeFormatDate(String date) {
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date retDate = null;
        try {
            retDate = formatter.parse(date);
        } catch (ParseException e) {
            logger.error(e);
        }
        return retDate;
    }
}
