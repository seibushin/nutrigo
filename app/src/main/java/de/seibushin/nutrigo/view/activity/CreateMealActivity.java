package de.seibushin.nutrigo.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.concurrent.Executors;

import de.seibushin.nutrigo.R;
import de.seibushin.nutrigo.model.nutrition.Food;
import de.seibushin.nutrigo.model.nutrition.FoodDay;
import de.seibushin.nutrigo.model.nutrition.FoodPortion;
import de.seibushin.nutrigo.model.nutrition.Meal;
import de.seibushin.nutrigo.model.nutrition.NutritionType;
import de.seibushin.nutrigo.model.nutrition.NutritionUnit;
import de.seibushin.nutrigo.view.adapter.ClickListener;
import de.seibushin.nutrigo.view.adapter.ItemTouchListener;
import de.seibushin.nutrigo.view.adapter.NutritionAdapter;
import de.seibushin.nutrigo.view.dialog.ServingDialog;
import de.seibushin.nutrigo.viewmodel.FoodViewModel;

public class CreateMealActivity extends AppCompatActivity implements ServingDialog.ResultListener {
    private CoordinatorLayout outerWrapper;

    private TextInputEditText name;
    private TextInputEditText tag;
    private TextInputEditText search;

    private RecyclerView rv_foods;
    private RecyclerView rv_search;
    private NutritionAdapter search_adapter;
    private NutritionAdapter food_adapter;

    private FoodViewModel foodViewModel;
    private final ServingDialog servingDialog = new ServingDialog();
    private FoodPortion currentFood;
    private int currentPosition;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_meal);

        outerWrapper = findViewById(R.id.outer_wrapper);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name = findViewById(R.id.ti_name);
        tag = findViewById(R.id.ti_tag);
        tag.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // hide keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                if (imm != null && imm.isActive(tag)) {
                    imm.hideSoftInputFromWindow(tag.getWindowToken(), 0);
                }
                // remove focus by focusing the wrapper
                outerWrapper.requestFocus();
            }
            return false;
        });
        search = findViewById(R.id.ti_search);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().endsWith("  ")) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(name.getWindowToken(), 0);
                    search.clearFocus();
                } else {
                    if (s.toString().length() > 0) {
                        rv_search.setVisibility(View.VISIBLE);
                        search_adapter.getFilter().filter(s);
                    } else {
                        rv_search.setVisibility(View.INVISIBLE);
                    }
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        LinearLayoutManager llm = new LinearLayoutManager(this);
        DividerItemDecoration did = new DividerItemDecoration(this, llm.getOrientation());
        did.setDrawable(getApplicationContext().getDrawable(R.drawable.divider));

        food_adapter = new NutritionAdapter();
        rv_foods = findViewById(R.id.rv_foods);
        rv_foods.setAdapter(food_adapter);
        rv_foods.setLayoutManager(llm);
        rv_foods.addItemDecoration(did);
        rv_foods.addOnItemTouchListener(new ItemTouchListener(getApplicationContext(), rv_foods, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                NutritionUnit nu = food_adapter.getItem(position);
                if (nu.getType() == NutritionType.FOOD) {
                    currentFood = (FoodPortion) nu;
                    currentPosition = position;
                }

                servingDialog.show(getSupportFragmentManager());
            }

            @Override
            public void onLongClick(View view, int position) {
                NutritionUnit food = food_adapter.getItem(position);

                // add searched food to actual meal food
                food_adapter.remove(food);
                food_adapter.notifyDataSetChanged();
//                food_adapter.notifyItemRemoved(position);
            }
        }));

        LinearLayoutManager llm2 = new LinearLayoutManager(this);
        DividerItemDecoration did2 = new DividerItemDecoration(this, llm2.getOrientation());
        did2.setDrawable(getApplicationContext().getDrawable(R.drawable.divider));

        search_adapter = new NutritionAdapter();
        rv_search = findViewById(R.id.rv_search);
        rv_search.setAdapter(search_adapter);
        rv_search.setLayoutManager(llm2);
        rv_search.addItemDecoration(did2);
        rv_search.addOnItemTouchListener(new ItemTouchListener(getApplicationContext(), rv_search, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                FoodPortion food = (FoodPortion) search_adapter.getItem(position);

                // add searched food to actual meal food
                food_adapter.add(food);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        foodViewModel = new ViewModelProvider(this).get(FoodViewModel.class);
        foodViewModel.getAllFood().observe(this, foods -> search_adapter.setItems(new ArrayList<>(foods)));

    }

    /**
     * Create the actual meal
     *
     * @return
     * @throws NumberFormatException
     */
    private void createMeal() {
        try {
            String _name = name.getText().toString().trim();
            String _tag = tag.getText().toString().trim();
            Meal meal = new Meal(0, _name);
            // add all selected food portions
            food_adapter.getData().forEach(food -> meal.addFood((FoodPortion) food));
//            Database.getInstance().addMeal(meal);
            Snackbar.make(outerWrapper, R.string.add_meal_insert, Snackbar.LENGTH_LONG).show();
            resetMeal();
        } catch (NumberFormatException e) {
            Snackbar.make(outerWrapper, R.string.add_meal_insert_error, Snackbar.LENGTH_LONG).show();
        }
    }

    /**
     * Reset the view / clear inputs
     */
    private void resetMeal() {
        // reset values
        name.setText("");
        tag.setText("");
        food_adapter.clear();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.action_add) {
            // insert into database
            if (food_adapter.getItemCount() > 0 && !"".equals(name.getText().toString())) {
                createMeal();
            }

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_food, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        if (food_adapter.getItemCount() > 0 || !"".equals(name.getText().toString())) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.add_meal_dialog)
                    .setPositiveButton(R.string.add_meal_dialog_pos, (dialog, which) -> {
                        createMeal();

                        super.onBackPressed();
                    })
                    .setNegativeButton(R.string.add_meal_dialog_neg, (dialog, which) -> super.onBackPressed());
            builder.create().show();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void result(Double serving) {
        if (currentFood != null && currentPosition != -1) {
            currentFood.setPortion(serving);
            food_adapter.notifyItemChanged(currentPosition);
            currentPosition = -1;
            currentFood = null;
        }
    }
}
