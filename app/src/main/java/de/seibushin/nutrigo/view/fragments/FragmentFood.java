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

import java.util.ArrayList;
import java.util.concurrent.Executors;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.seibushin.nutrigo.R;
import de.seibushin.nutrigo.model.nutrition.Food;
import de.seibushin.nutrigo.model.nutrition.FoodDay;
import de.seibushin.nutrigo.view.activity.CreateFoodActivity;
import de.seibushin.nutrigo.view.adapter.NutritionAdapter;
import de.seibushin.nutrigo.view.dialog.FoodDialog;
import de.seibushin.nutrigo.viewmodel.DayFoodViewModel;
import de.seibushin.nutrigo.viewmodel.FoodViewModel;

/**
 * A fragment representing a list of Items.
 * <p/>
 */
public class FragmentFood extends FragmentList {
    private FoodViewModel foodViewModel;
    private DayFoodViewModel dayFoodViewModel;
    private FoodDialog foodDialog;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FragmentFood() {
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

        LinearLayoutManager llm = new LinearLayoutManager(context);
        DividerItemDecoration did = new DividerItemDecoration(getActivity(), llm.getOrientation());
        did.setDrawable(getContext().getDrawable(R.drawable.divider));

        adapter = new NutritionAdapter();
        adapter.onClick((nu, pos) -> {
            Food food = (Food) nu;

            FoodDay df = dayFoodViewModel.insert(food);

            Snackbar snack = Snackbar.make(view, getString(R.string.undo_food_added_day, food.getName()), Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.undo), v -> Executors.newSingleThreadExecutor().execute(() -> {
                        // remove food from day
                        dayFoodViewModel.delete(df);
                    }));
            snack.show();
        });
        adapter.onEdit((nu, pos) -> showFoodEdit((Food) nu));
        // todo: implement clone
        adapter.onClone((nu, pos) -> {
            Toast.makeText(getContext(), "Clone " + nu.getName(), Toast.LENGTH_SHORT).show();
        });
        adapter.onDelete((nu, pos) -> {
            Food food = (Food) nu;
            food.portionize = false;
            foodViewModel.delete(food);

            Snackbar snack = Snackbar.make(view, getString(R.string.undo_food_deleted, food.getName()), Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.undo), v -> Executors.newSingleThreadExecutor().execute(() -> foodViewModel.insert(food)));
            snack.show();
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(llm);
        recyclerView.addItemDecoration(did);

        foodViewModel = new ViewModelProvider(this).get(FoodViewModel.class);
        foodViewModel.getAllFood().observe(getViewLifecycleOwner(), foods -> {
            for (Food food : foods) {
                food.served = food.getPortion();
                food.portionize = true;
            }
            adapter.setItems(new ArrayList<>(foods));
        });

        dayFoodViewModel = new ViewModelProvider(this).get(DayFoodViewModel.class);

        return view;
    }

    /**
     * Show edit food dialog
     */
    private void showFoodEdit(Food food) {
        if (foodDialog == null) {
            foodDialog = new FoodDialog();
        }
        foodDialog.show(getParentFragmentManager(), food);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                startActivity(new Intent(getContext(), CreateFoodActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
