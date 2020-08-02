package de.seibushin.nutrigo.view.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.CheckBox;
import android.widget.Switch;
import android.widget.TextView;

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
import de.seibushin.nutrigo.view.widget.Chart;
import de.seibushin.nutrigo.view.widget.ChartData;
import de.seibushin.nutrigo.viewmodel.DayFoodViewModel;
import de.seibushin.nutrigo.viewmodel.ProfileViewModel;

public class ChartActivity extends AppCompatActivity {
    private Chart chart;

    private DayFoodViewModel dayFoodViewModel;
    private ProfileViewModel profileViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        chart = findViewById(R.id.chart);

        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        profileViewModel.getProfile().observe(this, profile -> {
//            profile.
            ChartData p = new ChartData();
            p.kcal = profile.getKcal();
            p.fat = profile.getFat();
            p.carbs = profile.getCarbs();
            p.protein = profile.getProtein();
            chart.setTarget(p);
        });

        dayFoodViewModel = new ViewModelProvider(this).get(DayFoodViewModel.class);
        dayFoodViewModel.getDaily().observe(this, daily -> {
            System.out.println("new Daily");
            List<ChartData> data = new ArrayList<>();
            daily.forEach(day -> {
                System.out.println(day.timestamp + " " + day.kcal + " " + day.fat + " " + day.carbs + " " + day.sugar + " " + day.protein);
                ChartData dail = new ChartData();
                dail.timestamp = day.timestamp;
                dail.kcal = day.kcal;
                dail.fat = day.fat;
                dail.carbs = day.carbs;
                dail.protein = day.protein;
                data.add(dail);
            });
            chart.setData(data);
        });

        CheckBox kcal = findViewById(R.id.kcal_cb);
        kcal.setOnClickListener(v -> chart.setKcal(((CheckBox) v).isChecked()));
        CheckBox fat = findViewById(R.id.fat_cb);
        fat.setOnClickListener(v -> chart.setFat(((CheckBox) v).isChecked()));
        CheckBox carbs = findViewById(R.id.carbs_cb);
        carbs.setOnClickListener(v -> chart.setCarbs(((CheckBox) v).isChecked()));
        CheckBox protein = findViewById(R.id.protein_cb);
        protein.setOnClickListener(v -> chart.setProtein(((CheckBox) v).isChecked()));

        TextView lastx = findViewById(R.id.lastx);
        lastx.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    int xdays =  Integer.parseInt(s.toString());
                    chart.setLastX(xdays);
                } catch (NumberFormatException e) {
                    // ignore
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}