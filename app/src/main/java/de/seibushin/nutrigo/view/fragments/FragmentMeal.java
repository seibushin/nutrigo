package de.seibushin.nutrigo.view.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.concurrent.Executors;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.seibushin.nutrigo.R;
import de.seibushin.nutrigo.model.nutrition.Food;
import de.seibushin.nutrigo.model.nutrition.Meal;
import de.seibushin.nutrigo.model.nutrition.MealDay;
import de.seibushin.nutrigo.view.activity.CreateMealActivity;
import de.seibushin.nutrigo.view.adapter.NutritionAdapter;
import de.seibushin.nutrigo.view.dialog.FoodDialog;
import de.seibushin.nutrigo.view.dialog.MealDialog;
import de.seibushin.nutrigo.viewmodel.DayMealViewModel;
import de.seibushin.nutrigo.viewmodel.MealViewModel;

/**
 * A fragment representing a list of Items.
 * <p/>
 */
public class FragmentMeal extends FragmentList {
    private MealViewModel mealViewModel;
    private DayMealViewModel dayMealViewModel;
    private RelativeLayout outer;
    private MealDialog mealDialog;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FragmentMeal() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        // Set the adapter
        Context context = view.getContext();
        RecyclerView recyclerView = view.findViewById(R.id.nu_items);
        outer = view.findViewById(R.id.food_outer);

        LinearLayoutManager llm = new LinearLayoutManager(context);
        DividerItemDecoration did = new DividerItemDecoration(getActivity(), llm.getOrientation());
        did.setDrawable(getContext().getDrawable(R.drawable.divider));

        adapter = new NutritionAdapter();
        adapter.onClick((nu, pos) -> {
            Meal meal = (Meal) nu;

            MealDay md = dayMealViewModel.insert(meal);

            Snackbar snack = Snackbar.make(outer, getString(R.string.undo_food_added_day, meal.getName()), Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.undo), v -> Executors.newSingleThreadExecutor().execute(() -> {
                        // remove food from day
                        dayMealViewModel.delete(md);
                    }));
            snack.show();
        });

        adapter.onEdit((nu, pos) -> showMealEdit((Meal) nu));

        adapter.onDelete((nu, pos) -> {
            Meal meal = (Meal) nu;
            mealViewModel.delete(meal);

            Snackbar snack = Snackbar.make(outer, getString(R.string.undo_food_deleted, meal.getName()), Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.undo), v -> Executors.newSingleThreadExecutor().execute(() -> mealViewModel.insert(meal)));
            snack.show();
        });
        adapter.onClone((nu, pos) -> {
            Toast.makeText(getContext(), "Clone " + nu.getName(), Toast.LENGTH_SHORT).show();
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(llm);
        recyclerView.addItemDecoration(did);

        mealViewModel = new ViewModelProvider(this).get(MealViewModel.class);
        mealViewModel.getAllMeal().observe(getViewLifecycleOwner(), meals -> {
            adapter.setItems(mealViewModel.getServedMeals());
        });

        dayMealViewModel = new ViewModelProvider(this).get(DayMealViewModel.class);
        return view;
    }

    /**
     * Show edit food dialog
     */
    private void showMealEdit(Meal meal) {
        if (mealDialog == null) {
            mealDialog = new MealDialog();
        }
        mealDialog.show(getParentFragmentManager(), meal);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                startActivity(new Intent(getContext(), CreateMealActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
