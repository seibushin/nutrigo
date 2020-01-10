package de.seibushin.nutrigo.viewmodel;

import java.util.Calendar;
import java.util.Date;

import androidx.room.TypeConverter;

public class DateConverter {
    @TypeConverter
    public static Date toDate(long date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);

        return calendar.getTime();
    }

    @TypeConverter
    public static Long fromDate(Date date) {
        return date.getTime();
    }
}