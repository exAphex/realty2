package org.exaphex.realty.util;

import javax.swing.*;

public class PriceUtils {
    public static Float validatePrice(String val, String fieldName) {
        try {
            return Float.parseFloat(val);
        } catch (NumberFormatException n) {
            JOptionPane.showMessageDialog(new JFrame(), fieldName + " is not valid!", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
}
