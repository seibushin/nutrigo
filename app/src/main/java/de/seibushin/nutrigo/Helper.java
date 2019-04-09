package de.seibushin.nutrigo;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class Helper {
    private static DecimalFormat df;

    static {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        df = new DecimalFormat("0.0", symbols);
    }

    public static String formatDecimal(double d) {
        return df.format(d);
    }
}