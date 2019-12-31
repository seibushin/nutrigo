package de.seibushin.nutrigo;

import java.util.Calendar;
import java.util.Date;

public class Nutrigo {
    public static long selectedDay = today().getTime();

    public static void setSelectedDay(long selectedDay) {
        Nutrigo.selectedDay = selectedDay;
    }

    /**
     * Get the current day at 0:00:00
     *
     * @return
     */
    private static Date today() {
        Calendar calendar = Calendar.getInstance();
        toDay(calendar);

        return calendar.getTime();
    }

    private static void toDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }
}
