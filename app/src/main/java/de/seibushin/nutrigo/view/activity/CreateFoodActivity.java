package de.seibushin.nutrigo.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.android.volley.Response;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.seibushin.nutrigo.R;
import de.seibushin.nutrigo.model.nutrition.Food;
import de.seibushin.nutrigo.request.FoodLink;
import de.seibushin.nutrigo.request.FoodSearch;
import de.seibushin.nutrigo.view.adapter.SuggestionAdapter;
import de.seibushin.nutrigo.view.widget.TextInputEditTextWithSuggestion;
import de.seibushin.nutrigo.viewmodel.FoodViewModel;

/**
 * Created by Uni on 15.08.2018.
 */

public class CreateFoodActivity extends AppCompatActivity {
    private CoordinatorLayout outerWrapper;

    private TextInputEditTextWithSuggestion name;
    private TextInputEditText weight;
    private TextInputEditText portion;
    private TextInputEditText kcal;
    private EditText fat;
    private EditText carbs;
    private EditText sugar;
    private EditText protein;

    private FoodViewModel foodViewModel;

    private Response.Listener<List<FoodLink>> searchListener;
    private Response.Listener<Food> foodListener;
    private RecyclerView rvSuggestion;
    private SuggestionAdapter suggestionAdapter;

    private View.OnClickListener suggestionClickListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_food);

        outerWrapper = findViewById(R.id.outer_wrapper);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        DividerItemDecoration did = new DividerItemDecoration(this, llm.getOrientation());
        did.setDrawable(getDrawable(R.drawable.divider));

        suggestionClickListener = v -> {
            FoodLink link = suggestionAdapter.getItem(rvSuggestion.getChildLayoutPosition(v));
            FoodSearch.getInstance(getApplicationContext()).getFood(link.getLink(), foodListener);
            suggestionAdapter.clear();
        };

        suggestionAdapter = new SuggestionAdapter(this, suggestionClickListener);
        rvSuggestion = findViewById(R.id.rv_suggestion);
        rvSuggestion.setAdapter(suggestionAdapter);
        rvSuggestion.setLayoutManager(llm);
        rvSuggestion.addItemDecoration(did);

        setupListener();

        name = findViewById(R.id.ti_name);
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                System.out.println("Text changed " + s);
                if (s.toString().endsWith("  ")) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(name.getWindowToken(), 0);
                    name.clearFocus();
                } else {
                    FoodSearch.getInstance(getApplicationContext()).search(s.toString(), searchListener);
                }
            }
        });
        name.setOnFocusChangeListener((view, b) -> suggestionAdapter.clear());
        weight = findViewById(R.id.ti_weight);
        portion = findViewById(R.id.ti_portion);
        kcal = findViewById(R.id.ti_kcal);
        fat = findViewById(R.id.ti_fat);
        carbs = findViewById(R.id.ti_carbs);
        sugar = findViewById(R.id.ti_sugar);
        protein = findViewById(R.id.ti_protein);
        protein.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // hide keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                if (imm != null && imm.isActive(protein)) {
                    imm.hideSoftInputFromWindow(protein.getWindowToken(), 0);
                }
                // remove focus by focusing the wrapper
                outerWrapper.requestFocus();
            }
            return false;
        });

        foodViewModel = new ViewModelProvider(this).get(FoodViewModel.class);
    }

    private void setupListener() {
        searchListener = links -> {
            if (name.hasFocus()) {
                suggestionAdapter.setData(links);
            }
        };

        foodListener = food -> {
            // update UI
            updateUI(food);
        };
    }

    private void updateUI(Food food) {
        // add whitespace to the event to guarantee that it is handled as suggestion selection
        name.setText(food.getName() + "  ");
        weight.setText("" + food.getWeight());
        portion.setText("" + food.getPortion());
        kcal.setText("" + food.getKcal());
        fat.setText("" + food.getFat());
        carbs.setText("" + food.getCarbs());
        sugar.setText("" + food.getSugar());
        protein.setText("" + food.getProtein());
    }

    /**
     * Create the Food from the values of the view
     */
    private void createFood() {
        try {
            String _name = name.getText().toString().trim();
            double _weight = Double.valueOf(weight.getText().toString());
            double _portion = Double.valueOf(portion.getText().toString());
            double _kcal = Double.valueOf(kcal.getText().toString());
            double _protein = Double.valueOf(protein.getText().toString());
            double _fat = Double.valueOf(fat.getText().toString());
            double _carbs = Double.valueOf(carbs.getText().toString());
            double _sugar = Double.valueOf(sugar.getText().toString());

            Food food = new Food(_name, _kcal, _fat, _carbs, _sugar, _protein, _weight, _portion);
            foodViewModel.insert(food);

            Snackbar.make(outerWrapper, R.string.add_food_insert, Snackbar.LENGTH_LONG).show();
            resetFood();
        } catch (NumberFormatException e) {
            Snackbar.make(outerWrapper, R.string.add_food_insert_error, Snackbar.LENGTH_LONG).show();
        }
    }

    /**
     * Reset the view / clear inputs
     */
    private void resetFood() {
        // reset values
        name.setText("");
        weight.setText("");
        portion.setText("");
        kcal.setText("");
        fat.setText("");
        carbs.setText("");
        sugar.setText("");
        protein.setText("");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.action_add) {
            // insert into database
            if (!"".equals(name.getText().toString())) {
                createFood();
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
        if (!"".equals(name.getText().toString())) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.add_food_dialog)
                    .setPositiveButton(R.string.add_food_dialog_pos, (dialog, which) -> {
                        createFood();

                        super.onBackPressed();
                    })
                    .setNegativeButton(R.string.add_food_dialog_neg, (dialog, which) -> super.onBackPressed());
            builder.create().show();
        } else {
            super.onBackPressed();
        }
    }
}
