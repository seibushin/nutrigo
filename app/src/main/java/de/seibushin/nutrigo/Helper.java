package de.seibushin.nutrigo;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class Helper {
    private static DecimalFormat df;
    private static DecimalFormat df_int;

    static {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        df = new DecimalFormat("0.0", symbols);
        df_int = new DecimalFormat("0", symbols);
    }

    public static String formatDecimal(double d) {
        return df.format(d);
    }

    public static String formatInt(float f) {
        return df_int.format(f);
    }
}