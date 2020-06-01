package de.seibushin.nutrigo.view.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;

import de.seibushin.nutrigo.Helper;
import de.seibushin.nutrigo.R;
import de.seibushin.nutrigo.model.nutrition.Food;
import de.seibushin.nutrigo.model.nutrition.Meal;
import de.seibushin.nutrigo.model.nutrition.NutritionType;
import de.seibushin.nutrigo.model.nutrition.NutritionUnit;
import de.seibushin.nutrigo.view.adapter.NutritionAdapter;
import de.seibushin.nutrigo.view.widget.TextInputEditTextWithSuggestion;
import de.seibushin.nutrigo.viewmodel.FoodViewModel;
import de.seibushin.nutrigo.viewmodel.MealViewModel;

public class MealDialog extends DialogFragment implements ServingDialog.ResultListener {
    public static final String TAG = "EditMealFragment";

    private CoordinatorLayout outerWrapper;
    private TextInputEditText name;
    private TextInputEditText tag;
    private TextInputEditText search;

    private RecyclerView rv_foods;
    private RecyclerView rv_search;
    private NutritionAdapter search_adapter;
    private NutritionAdapter food_adapter;

    private Meal meal;
    private FoodViewModel foodViewModel;
    private final ServingDialog servingDialog = new ServingDialog();
    private Food currentFood;

    private MealViewModel mealViewModel;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        servingDialog.resultListener = this;

        View view = getActivity().getLayoutInflater().inflate(R.layout.edit_meal, null);
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setPositiveButton("Save", null)
                .create();

        dialog.setOnShowListener(dialog1 -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                if (updateMeal(v)) {
                    dialog.dismiss();
                }
            });
        });

        outerWrapper = view.findViewById(R.id.outer_wrapper);

        name = view.findViewById(R.id.ti_name);
        tag = view.findViewById(R.id.ti_tag);
        tag.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // hide keyboard
                InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                if (imm != null && imm.isActive(tag)) {
                    imm.hideSoftInputFromWindow(tag.getWindowToken(), 0);
                }
                // remove focus by focusing the wrapper
                outerWrapper.requestFocus();
            }
            return false;
        });
        search = view.findViewById(R.id.ti_search);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().endsWith("  ")) {
                    InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
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

        LinearLayoutManager llm = new LinearLayoutManager(view.getContext());
        DividerItemDecoration did = new DividerItemDecoration(view.getContext(), llm.getOrientation());
        did.setDrawable(view.getContext().getDrawable(R.drawable.divider));

        food_adapter = new NutritionAdapter();
        food_adapter.onEdit((nu, pos) -> {
            if (nu.getType() == NutritionType.FOOD) {
                currentFood = (Food) nu;
            }

            servingDialog.show(getParentFragmentManager(), nu);
        });
        food_adapter.onDelete((nu, pos) -> {
            food_adapter.remove(pos);
        });
        rv_foods = view.findViewById(R.id.rv_foods);
        // todo update layout to work for activity and dialog current issue is the dialog
        rv_foods.getLayoutParams().width = 900;
        rv_foods.setAdapter(food_adapter);
        rv_foods.setLayoutManager(llm);
        rv_foods.addItemDecoration(did);

        LinearLayoutManager llm2 = new LinearLayoutManager(view.getContext());
        DividerItemDecoration did2 = new DividerItemDecoration(view.getContext(), llm2.getOrientation());
        did2.setDrawable(view.getContext().getDrawable(R.drawable.divider));

        search_adapter = new NutritionAdapter();
        search_adapter.onClick((nu, pos) -> {
            Food food = (Food) nu;

            // add searched food to actual meal food
            food_adapter.add(food);
        });
        rv_search = view.findViewById(R.id.rv_search);
        rv_search.setAdapter(search_adapter);
        rv_search.setLayoutManager(llm2);
        rv_search.addItemDecoration(did2);

        foodViewModel = new ViewModelProvider(this).get(FoodViewModel.class);
        foodViewModel.getAllFood().observe(this, foods -> {
            for (Food food : foods) {
                food.served = food.getPortion();
                food.portionize = true;
            }
            search_adapter.setItems(new ArrayList<>(foods));
        });

        mealViewModel = new ViewModelProvider(this).get(MealViewModel.class);

        if (meal != null) {
            updateUI();
        }

        return dialog;
    }

    private boolean updateMeal(View view) {
        try {
            meal.mealInfo.name = name.getText().toString().trim();
            meal.foods = new ArrayList<>();
            food_adapter.getData().forEach(food -> meal.foods.add((Food) food));
            mealViewModel.update(meal);
            return true;
        } catch (NumberFormatException e) {
            // ignore
        }
        Snackbar.make(view, R.string.edit_food_insert_error, Snackbar.LENGTH_LONG).show();
        return false;
    }

    public void show(FragmentManager fragmentManager, Meal meal) {
        this.meal = meal;
        show(fragmentManager, TAG);
    }

    private void updateUI() {
        name.setText(meal.getName());
        food_adapter.setFoods(new ArrayList<>(meal.foods));
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    @Override
    public void result(Double serving) {
        if (currentFood != null  && serving > 0) {
            currentFood.served = serving;
            food_adapter.notifyDataSetChanged();
            currentFood = null;
        }
    }
}