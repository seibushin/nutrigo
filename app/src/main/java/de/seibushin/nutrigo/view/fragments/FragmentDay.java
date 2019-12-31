package de.seibushin.nutrigo.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.concurrent.Executors;

import de.seibushin.nutrigo.R;
import de.seibushin.nutrigo.model.nutrition.FoodDay;
import de.seibushin.nutrigo.model.nutrition.NutritionType;
import de.seibushin.nutrigo.model.nutrition.NutritionUnit;
import de.seibushin.nutrigo.view.activity.CalendarActivity;
import de.seibushin.nutrigo.view.adapter.ClickListener;
import de.seibushin.nutrigo.view.adapter.ItemTouchListener;
import de.seibushin.nutrigo.view.adapter.NutritionAdapter;
import de.seibushin.nutrigo.view.dialog.ProfileDialog;
import de.seibushin.nutrigo.view.widget.ProgressCircle;
import de.seibushin.nutrigo.viewmodel.DayFoodViewModel;
import de.seibushin.nutrigo.viewmodel.ProfileViewModel;

/**
 * A fragment representing a list of Items.
 * <p/>
 */
public class FragmentDay extends Fragment {
    private DayFoodViewModel dayFoodViewModel;
    private ProfileViewModel profileViewModel;

    private NutritionAdapter adapter;

    private ProgressCircle pc_kcal;
    private ProgressCircle pc_fat;
    private ProgressCircle pc_carbs;
    private ProgressCircle pc_protein;
    private TextView tv_date;

    private final DateFormat df = SimpleDateFormat.getDateInstance();

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FragmentDay() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_day, container, false);

        // progress circle
        pc_kcal = view.findViewById(R.id.pc_kcal);
        pc_fat = view.findViewById(R.id.pc_fat);
        pc_carbs = view.findViewById(R.id.pc_carbs);
        pc_protein = view.findViewById(R.id.pc_protein);
        tv_date = view.findViewById(R.id.tv_date);

        view.findViewById(R.id.show_profile).setOnClickListener(v -> showProfile());

        LinearLayoutManager llm = new LinearLayoutManager(view.getContext());
        DividerItemDecoration did = new DividerItemDecoration(getActivity(), llm.getOrientation());
        did.setDrawable(getContext().getDrawable(R.drawable.divider));

        adapter = new NutritionAdapter();
        RecyclerView recyclerView = view.findViewById(R.id.list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(llm);
        recyclerView.addItemDecoration(did);
        recyclerView.addOnItemTouchListener(new ItemTouchListener(getContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                // set serving
            }

            @Override
            public void onLongClick(View view, int position) {
                NutritionUnit nu = adapter.getItem(position);
                if (nu.getType() == NutritionType.FOOD) {
                    FoodDay food = (FoodDay) nu;
                    dayFoodViewModel.delete(food);

                    Snackbar.make(view, getString(R.string.undo_food_deleted, food.getName()), BaseTransientBottomBar.LENGTH_LONG)
                        .setAction(getString(R.string.undo), v -> Executors.newSingleThreadExecutor().execute(() -> dayFoodViewModel.insertDayFood(food)))
                        .show();
                };
            }
        }));

        dayFoodViewModel = new ViewModelProvider(this).get(DayFoodViewModel.class);
        dayFoodViewModel.getDayFood().observe(getViewLifecycleOwner(), foods -> {
            adapter.setItems(new ArrayList<>(foods));

            double kcal = 0;
            double protein = 0;
            double fat = 0;
            double carbs = 0;
            double sugar = 0;

            for (FoodDay food : foods) {
                kcal += food.getKcal();
                protein+= food.getProtein();
                fat += food.getFat();
                carbs += food.getCarbs();
                sugar += food.getSugar();
            }

            // update progress
            pc_kcal.setProgress((int)kcal);
            pc_carbs.setProgress((int)carbs);
            pc_fat.setProgress((int)fat);
            pc_protein.setProgress((int)protein);
        });

        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        profileViewModel.getProfile().observe(getViewLifecycleOwner(), profile -> {
            pc_kcal.setMax(profile.getKcal());
            pc_carbs.setMax(profile.getCarbs());
            pc_fat.setMax(profile.getFat());
            pc_protein.setMax(profile.getProtein());
        });

        return view;
    }

    /**
     * Show profile dialog
     */
    private void showProfile() {
        ProfileDialog dialog = new ProfileDialog();
        dialog.show(getParentFragmentManager());
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);

        menu.findItem(R.id.action_add).setVisible(false);
        menu.findItem(R.id.action_search).setVisible(false);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_day:
                // select Day
                startActivity(new Intent(getContext(), CalendarActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
