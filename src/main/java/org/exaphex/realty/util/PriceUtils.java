package org.exaphex.realty.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Comparator;
import java.util.ResourceBundle;

public class PriceUtils {
    private static final ResourceBundle res = ResourceBundle.getBundle("i18n");
    private static final Logger logger = LogManager.getLogger();
    public static Float validatePrice(String val, String fieldName) {
        try {
            return Float.parseFloat(val);
        } catch (NumberFormatException n) {
            JOptionPane.showMessageDialog(new JFrame(), fieldName + res.getString("msgIsNotValid"), res.getString("msgError"),
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public static Integer validateInteger(String val, String fieldName) {
        try {
            return Integer.parseInt(val);
        } catch (NumberFormatException n) {
            JOptionPane.showMessageDialog(new JFrame(), fieldName + res.getString("msgIsNotValid"), res.getString("msgError"),
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public static float parseSafePrice(String val) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        try {
            return formatter.parse(val).floatValue();
        } catch (ParseException e) {
            logger.error(e);
            return 0f;
        }
    }

    public static void setPriceSorter(TableRowSorter<TableModel> sorter, int column) {
        sorter.setComparator(column, (Comparator<String>) (name1, name2) -> {
            float fLhs = parseSafePrice(name1);
            float fRhs = parseSafePrice(name2);
            return Float.compare(fLhs, fRhs);
        });
    }
}
