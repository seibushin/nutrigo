package de.seibushin.nutrigo.view.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import de.seibushin.nutrigo.Helper;
import de.seibushin.nutrigo.R;
import de.seibushin.nutrigo.model.nutrition.Food;
import de.seibushin.nutrigo.view.widget.TextInputEditTextWithSuggestion;
import de.seibushin.nutrigo.viewmodel.FoodViewModel;

public class FoodDialog extends DialogFragment {
    public static final String TAG = "EditFoodFragment";

    private TextInputEditTextWithSuggestion name;
    private TextInputEditText weight;
    private TextInputEditText portion;
    private TextInputEditText kcal;
    private EditText fat;
    private EditText carbs;
    private EditText sugar;
    private EditText protein;
    private CheckBox ignore;

    private Food food;
    private FoodViewModel foodViewModel;

    private TextView checkMacro;
    private TextWatcher tw = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            checkMacros();
        }
    };

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        View view = getActivity().getLayoutInflater().inflate(R.layout.edit_food, null);
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setPositiveButton("Save", null)
                .create();

        dialog.setOnShowListener(dialog1 -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                if (updateFood(v)) {
                    dialog.dismiss();
                }
            });
        });

        checkMacro = view.findViewById(R.id.checkMacro);
        ignore = view.findViewById(R.id.ignore);

        name = view.findViewById(R.id.ti_name);
        weight = view.findViewById(R.id.ti_weight);
        portion = view.findViewById(R.id.ti_portion);
        kcal = view.findViewById(R.id.ti_kcal);
        fat = view.findViewById(R.id.ti_fat);
        carbs = view.findViewById(R.id.ti_carbs);
        sugar = view.findViewById(R.id.ti_sugar);
        protein = view.findViewById(R.id.ti_protein);

        kcal.addTextChangedListener(tw);
        fat.addTextChangedListener(tw);
        carbs.addTextChangedListener(tw);
        protein.addTextChangedListener(tw);

        foodViewModel = new ViewModelProvider(this).get(FoodViewModel.class);

        if (food != null) {
            updateUI();
        }

        return dialog;
    }

    private boolean updateFood(View view) {
        try {
            if (checkMacros() || ignore.isChecked()) {
                food.setName(name.getText().toString().trim());
                food.setWeight(Double.valueOf(weight.getText().toString()));
                food.setPortion(Double.valueOf(portion.getText().toString()));
                food.setKcal(Double.valueOf(kcal.getText().toString()));
                food.setProtein(Double.valueOf(protein.getText().toString()));
                food.setFat(Double.valueOf(fat.getText().toString()));
                food.setCarbs(Double.valueOf(carbs.getText().toString()));
                food.setSugar(Double.valueOf(sugar.getText().toString()));

                foodViewModel.update(food);
                return true;
            }
        } catch (NumberFormatException e) {
            // ignore
        }
        Snackbar.make(view, R.string.edit_food_insert_error, Snackbar.LENGTH_LONG).show();
        return false;
    }

    public void show(FragmentManager fragmentManager, Food food) {
        this.food = food;
        this.food.portionize = false;
        show(fragmentManager, TAG);
    }

    private void updateUI() {
        name.setText(food.getName());
        kcal.setText(Helper.formatDecimal(food.getKcal()));
        fat.setText(Helper.formatDecimal(food.getFat()));
        carbs.setText(Helper.formatDecimal(food.getCarbs()));
        sugar.setText(Helper.formatDecimal(food.getSugar()));
        protein.setText(Helper.formatDecimal(food.getProtein()));
        weight.setText(Helper.formatDecimal(food.getWeight()));
        portion.setText(Helper.formatDecimal(food.getPortion()));
    }

    private boolean checkMacros() {
        try {
            double _kcal = Double.valueOf(kcal.getText().toString());
            double _protein = Double.valueOf(protein.getText().toString());
            double _fat = Double.valueOf(fat.getText().toString());
            double _carbs = Double.valueOf(carbs.getText().toString());

            double kCalced = _protein * 4 + _carbs * 4 + _fat * 9;

            if (kCalced > _kcal * 0.95 && kCalced < _kcal * 1.05) {
                checkMacro.setTextColor(getActivity().getColor(R.color.colorPrimary));
                checkMacro.setText("All Macros are okay!");
                return true;
            } else {
                checkMacro.setTextColor(getActivity().getColor(R.color.colorAccent));
                checkMacro.setText(getString(R.string.macroCheck, Helper.formatDecimal(kCalced)));
                return false;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            checkMacro.setTextColor(getActivity().getColor(R.color.colorAccent));
            checkMacro.setText("Please fill all fields!");
            return false;
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }
}