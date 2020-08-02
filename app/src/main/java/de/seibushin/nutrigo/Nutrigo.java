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

    public static void prevDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(selectedDay);
        toDay(calendar);
        calendar.add(Calendar.DATE, -1);

        setSelectedDay(calendar.getTimeInMillis());
    }

    public static void nextDay() {
        Calendar today = Calendar.getInstance();
        toDay(today);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(selectedDay);
        toDay(calendar);

        if (calendar.before(today)) {
            calendar.add(Calendar.DATE, 1);
            setSelectedDay(calendar.getTimeInMillis());
        }
    }

    public static long getDayAdjustedTimestamp(long currentTimeMillis) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(selectedDay);

        Calendar c2 = Calendar.getInstance();
        c2.setTimeInMillis(currentTimeMillis);

        c.set(Calendar.HOUR_OF_DAY, c2.get(Calendar.HOUR_OF_DAY));
        c.set(Calendar.MINUTE, c2.get(Calendar.MINUTE));

        return c.getTimeInMillis();
    }

    public static long adjustTimestamp(long ts) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(ts);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTimeInMillis();
    }

    public static long getToday() {
        return today().getTime();
    }
}
