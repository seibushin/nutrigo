package de.seibushin.nutrigo.view.activity;

import android.os.Bundle;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import de.seibushin.nutrigo.Nutrigo;
import de.seibushin.nutrigo.R;
import de.seibushin.nutrigo.viewmodel.DayFoodViewModel;

public class CalendarActivity extends AppCompatActivity {
    private CalendarView calendarView;

    private DayFoodViewModel dayFoodViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        calendarView = findViewById(R.id.calendarView);

        setupCalendar();

        dayFoodViewModel = new ViewModelProvider(this).get(DayFoodViewModel.class);
        dayFoodViewModel.getDays().observe(this, days -> {
            List<EventDay> events = new ArrayList<>();
            days.forEach(day -> {
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(day);
                events.add(new EventDay(c, getDrawable(R.drawable.ic_food)));
            });
            calendarView.setEvents(events);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        try {
            calendarView.setDate(new Date(Nutrigo.selectedDay));
        } catch (OutOfDateRangeException e) {
            try {
                calendarView.setDate(new Date());
            } catch (OutOfDateRangeException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void setupCalendar() {
        calendarView.setMaximumDate(Calendar.getInstance());

        calendarView.setOnDayClickListener(eventDay -> {
            try {
                calendarView.setDate(eventDay.getCalendar());
                Nutrigo.setSelectedDay(eventDay.getCalendar().getTime().getTime());
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