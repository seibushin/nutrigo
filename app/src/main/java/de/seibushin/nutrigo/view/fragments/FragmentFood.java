package de.seibushin.nutrigo.view.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

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
import de.seibushin.nutrigo.view.adapter.ClickListener;
import de.seibushin.nutrigo.view.adapter.ItemTouchListener;
import de.seibushin.nutrigo.view.adapter.NutritionAdapter;
import de.seibushin.nutrigo.viewmodel.DayFoodViewModel;
import de.seibushin.nutrigo.viewmodel.FoodViewModel;

/**
 * A fragment representing a list of Items.
 * <p/>
 */
public class FragmentFood extends FragmentList {
    private FoodViewModel foodViewModel;
    private DayFoodViewModel dayFoodViewModel;
    private RelativeLayout outer;

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
        outer = view.findViewById(R.id.food_outer);

        LinearLayoutManager llm = new LinearLayoutManager(context);
        DividerItemDecoration did = new DividerItemDecoration(getActivity(), llm.getOrientation());
        did.setDrawable(getContext().getDrawable(R.drawable.divider));

        adapter = new NutritionAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(llm);
        recyclerView.addItemDecoration(did);
        recyclerView.addOnItemTouchListener(new ItemTouchListener(getContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Food food = (Food) adapter.getItem(position);

                FoodDay df = dayFoodViewModel.insert(food);

                Snackbar snack = Snackbar.make(view, getString(R.string.undo_food_added_day, food.getName()), Snackbar.LENGTH_LONG)
                        .setAction(getString(R.string.undo), v -> Executors.newSingleThreadExecutor().execute(() -> {
                            // remove food from day
                            dayFoodViewModel.delete(df);
                        }));
//                snack.show();
                showSnackbar(snack);
            }

            @Override
            public void onLongClick(View view, int position) {
                Food food = (Food) adapter.getItem(position);
                food.portionize = false;
                foodViewModel.delete(food);

                Snackbar snack = Snackbar.make(view, getString(R.string.undo_food_deleted, food.getName()), Snackbar.LENGTH_LONG)
                        .setAction(getString(R.string.undo), v -> Executors.newSingleThreadExecutor().execute(() -> foodViewModel.insert(food)));
//                snack.show();
                showSnackbar(snack);
            }
        }));

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
