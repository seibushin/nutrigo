package de.seibushin.nutrigo.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.seibushin.nutrigo.Nutrigo;
import de.seibushin.nutrigo.R;
import de.seibushin.nutrigo.model.nutrition.FoodDay;
import de.seibushin.nutrigo.model.nutrition.MealDay;
import de.seibushin.nutrigo.model.nutrition.NutritionType;
import de.seibushin.nutrigo.model.nutrition.NutritionUnit;
import de.seibushin.nutrigo.view.activity.CalendarActivity;
import de.seibushin.nutrigo.view.adapter.ClickListener;
import de.seibushin.nutrigo.view.adapter.ItemTouchListener;
import de.seibushin.nutrigo.view.adapter.NutritionAdapter;
import de.seibushin.nutrigo.view.dialog.ProfileDialog;
import de.seibushin.nutrigo.view.dialog.ServingDialog;
import de.seibushin.nutrigo.view.widget.ProgressCircle;
import de.seibushin.nutrigo.viewmodel.DayFoodViewModel;
import de.seibushin.nutrigo.viewmodel.DayMealViewModel;
import de.seibushin.nutrigo.viewmodel.ProfileViewModel;

import static android.app.Activity.RESULT_OK;

/**
 * A fragment representing a list of Items.
 * <p/>
 */
public class FragmentDay extends Fragment {
    private DayFoodViewModel dayFoodViewModel;
    private DayMealViewModel dayMealViewModel;
    private ProfileViewModel profileViewModel;

    private final DateFormat df = SimpleDateFormat.getDateInstance();

    public static final int NEW_SERVING = 101;
    private NutritionUnit currentNu;

    private RecyclerView recyclerView;
    private NutritionAdapter adapter;

    private ProgressCircle pc_kcal;
    private ProgressCircle pc_fat;
    private ProgressCircle pc_carbs;
    private ProgressCircle pc_protein;

    private final ServingDialog servingDialog = new ServingDialog();

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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_SERVING && resultCode == RESULT_OK) {
            double serving = data.getDoubleExtra(ServingDialog.EXTRA_SERVING, 0);
            double old_serving = currentNu.getPortion();

            if (currentNu.getType() == NutritionType.FOOD) {
                FoodDay currentFood = (FoodDay) currentNu;
                dayFoodViewModel.update(currentFood, serving);

                Snackbar.make(getView(), getString(R.string.undo_serving_changed, currentFood.getName()), Snackbar.LENGTH_LONG)
                        .setAction(getString(R.string.undo), v -> Executors.newSingleThreadExecutor().execute(() -> dayFoodViewModel.update(currentFood, old_serving)))
                        .show();
            } else if (currentNu.getType() == NutritionType.MEAL) {
                MealDay currentMeal = (MealDay) currentNu;
                dayMealViewModel.update(currentMeal, serving);

                Snackbar.make(getView(), getString(R.string.undo_serving_changed, currentMeal.getName()), Snackbar.LENGTH_LONG)
                        .setAction(getString(R.string.undo), v -> Executors.newSingleThreadExecutor().execute(() -> dayMealViewModel.update(currentMeal, old_serving)))
                        .show();
            }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_day, container, false);

        // progress circle
        pc_kcal = view.findViewById(R.id.pc_kcal);
        pc_fat = view.findViewById(R.id.pc_fat);
        pc_carbs = view.findViewById(R.id.pc_carbs);
        pc_protein = view.findViewById(R.id.pc_protein);

        view.findViewById(R.id.show_profile).setOnClickListener(v -> showProfile());

        view.findViewById(R.id.prevDay).setOnClickListener(v -> prevDay());
        view.findViewById(R.id.nextDay).setOnClickListener(v -> nextDay());

        LinearLayoutManager llm = new LinearLayoutManager(view.getContext());
        DividerItemDecoration did = new DividerItemDecoration(getActivity(), llm.getOrientation());
        did.setDrawable(getContext().getDrawable(R.drawable.divider));

        servingDialog.setTargetFragment(this, NEW_SERVING);

        adapter = new NutritionAdapter(true);
        recyclerView = view.findViewById(R.id.list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(llm);
        recyclerView.addItemDecoration(did);
        recyclerView.addOnItemTouchListener(new ItemTouchListener(getContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                currentNu = adapter.getItem(position);

                servingDialog.show(getParentFragmentManager());
            }

            @Override
            public void onLongClick(View view, int position) {
                NutritionUnit nu = adapter.getItem(position);
                if (nu.getType() == NutritionType.FOOD) {
                    FoodDay food = (FoodDay) nu;
                    dayFoodViewModel.delete(food);

                    Snackbar.make(view, getString(R.string.undo_food_deleted, food.getName()), Snackbar.LENGTH_LONG)
                            .setAction(getString(R.string.undo), v -> Executors.newSingleThreadExecutor().execute(() -> dayFoodViewModel.insertDayFood(food)))
                            .show();
                } else if (nu.getType() == NutritionType.MEAL) {
                    MealDay meal = (MealDay) nu;
                    dayMealViewModel.delete(meal);

                    Snackbar.make(view, getString(R.string.undo_food_deleted, meal.getName()), Snackbar.LENGTH_LONG)
                            .setAction(getString(R.string.undo), v -> Executors.newSingleThreadExecutor().execute(() -> dayMealViewModel.insertDayMeal(meal)))
                            .show();
                }
            }
        }));

        dayMealViewModel = new ViewModelProvider(this).get(DayMealViewModel.class);
        dayFoodViewModel = new ViewModelProvider(this).get(DayFoodViewModel.class);
        observeMeal();
        observeFood();

        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        profileViewModel.getProfile().observe(getViewLifecycleOwner(), profile -> {
            if (profile != null) {
                pc_kcal.setMax(profile.getKcal());
                pc_carbs.setMax(profile.getCarbs());
                pc_fat.setMax(profile.getFat());
                pc_protein.setMax(profile.getProtein());
            }
        });

        return view;
    }

    private void observeMeal() {
        dayMealViewModel.getDayMeal().observe(getViewLifecycleOwner(), meals -> {
            adapter.setMeals(dayMealViewModel.getServedMeals());
            calcDay();
        });
    }

    private void observeFood() {
        dayFoodViewModel.getDayFood().observe(getViewLifecycleOwner(), foods -> {
            adapter.setFoods(new ArrayList<>(foods));
            calcDay();
        });
    }

    private void calcDay() {
        double kcal = 0;
        double protein = 0;
        double fat = 0;
        double carbs = 0;
        double sugar = 0;

        for (NutritionUnit nu : adapter.getData()) {
            kcal += nu.getKcal();
            protein += nu.getProtein();
            fat += nu.getFat();
            carbs += nu.getCarbs();
            sugar += nu.getSugar();
        }

        // update progress
        pc_kcal.setProgress((int) kcal);
        pc_carbs.setProgress((int) carbs);
        pc_fat.setProgress((int) fat);
        pc_protein.setProgress((int) protein);
    }

    /**
     * Show profile dialog
     */
    private void showProfile() {
        ProfileDialog dialog = new ProfileDialog();
        dialog.show(getParentFragmentManager());
    }

    private void removeNuObserver() {
        dayMealViewModel.getDayMeal().removeObservers(getViewLifecycleOwner());
        dayFoodViewModel.getDayFood().removeObservers(getViewLifecycleOwner());
    }

    private void prevDay() {
        Nutrigo.prevDay();
        getActivity().setTitle(getString(R.string.title_activity_main) + " -  " + df.format(new Date(Nutrigo.selectedDay)));
        removeNuObserver();
        observeMeal();
        observeFood();
        recyclerView.getLayoutManager().scrollToPosition(0);
    }

    private void nextDay() {
        Nutrigo.nextDay();
        getActivity().setTitle(getString(R.string.title_activity_main) + " -  " + df.format(new Date(Nutrigo.selectedDay)));
        removeNuObserver();
        observeMeal();
        observeFood();
        recyclerView.getLayoutManager().scrollToPosition(0);
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
