package de.seibushin.nutrigo.view.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;

import java.util.Calendar;

import de.seibushin.nutrigo.R;

public class CalendarActivity extends AppCompatActivity {
    private CalendarView calendarView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        calendarView = findViewById(R.id.calendarView);

        setupCalendar();
    }

    @Override
    protected void onStart() {
        super.onStart();

//        try {
////            calendarView.setDate(new Date(Database.getInstance().getSelectedDay().getTime()));
//        } catch (OutOfDateRangeException e) {
//            try {
//                calendarView.setDate(new Date());
//            } catch (OutOfDateRangeException e1) {
//                e1.printStackTrace();
//            }
//        }
    }

    private void setupCalendar() {
        calendarView.setMaximumDate(Calendar.getInstance());

//        calendarView.setEvents(Database.getInstance().getAllDays());
        calendarView.setOnDayClickListener(eventDay -> {
            try {
                calendarView.setDate(eventDay.getCalendar());
//                Database.getInstance().selectDay(eventDay.getCalendar());
            } catch (OutOfDateRangeException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}