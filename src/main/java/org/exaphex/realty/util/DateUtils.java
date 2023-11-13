package org.exaphex.realty.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
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

    public static void setDateSorter(TableRowSorter<TableModel> sorter, int column) {
        sorter.setComparator(column, (Comparator<String>) (name1, name2) -> {
            Date sfLhs = safeFormatDate(name1);
            Date sfRhs = safeFormatDate(name2);
            return sfLhs.after(sfRhs) ? 1 : sfLhs.before(sfRhs) ? -1 : 0;
        });
    }
}
