package de.seibushin.nutrigo.view.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import de.seibushin.nutrigo.Helper;
import de.seibushin.nutrigo.R;
import de.seibushin.nutrigo.model.Profile;
import de.seibushin.nutrigo.viewmodel.ProfileViewModel;

public class ProfileDialog extends DialogFragment {
    private static final String TAG = "TargetFragment";
    private ProfileViewModel profileViewModel;
    private Profile profile;

    private TextInputEditText kcal;
    private EditText fat;
    private EditText carbs;
    private EditText sugar;
    private EditText protein;
    private TextView checkMacro;

    private TextInputEditText weight;
    private TextInputEditText height;
    private TextInputEditText age;
    private Spinner pal;
    private RadioButton male;
    private RadioButton female;

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

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_profile, null);
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setPositiveButton("Save", (dialog1, which) -> updateProfile())
                .create();

        kcal = view.findViewById(R.id.ti_kcal);
        fat = view.findViewById(R.id.ti_fat);
        carbs = view.findViewById(R.id.ti_carbs);
        sugar = view.findViewById(R.id.ti_sugar);
        protein = view.findViewById(R.id.ti_protein);
        checkMacro = view.findViewById(R.id.macroCheck);

        kcal.addTextChangedListener(tw);
        fat.addTextChangedListener(tw);
        carbs.addTextChangedListener(tw);
        protein.addTextChangedListener(tw);

        weight = view.findViewById(R.id.weight);
        height = view.findViewById(R.id.height);
        age = view.findViewById(R.id.age);
        male = view.findViewById(R.id.male);
        female = view.findViewById(R.id.female);
        pal = view.findViewById(R.id.pal);
        setupPal();

        Button calc = view.findViewById(R.id.calc);
        calc.setOnClickListener(v -> {
            int TEE = calorieCalculator();
            if (TEE > 0) {
                kcal.setText("" + TEE);
                int f = (int) Math.ceil(TEE * 0.3f / 9);
                int c = (int) Math.ceil(TEE * 0.5f / 4);
                int s = (int) Math.ceil(TEE * 0.15f / 4);
                int p = (int) Math.ceil(TEE * 0.2f / 4);
                fat.setText("" + f);
                carbs.setText("" + c);
                sugar.setText("" + s);
                protein.setText("" + p);
            }
        });

        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        profileViewModel.getProfile().observe(this, profile -> {
            this.profile = profile;
            updateUI();
        });

        return dialog;
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

    private void setupPal() {
        String[] items = new String[]{
                "1.0 - No PAL",
                "1.2 - Surviving",
                "1.4 - Living",
                "1.6 - Exercise 3x",
                "1.8 - Heavy Exercise",
                "2.0 - Day 'n Night"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, items);
        pal.setAdapter(adapter);
    }

    /**
     * show the dialog
     *
     * @param fragmentManager
     */
    public void show(FragmentManager fragmentManager) {
        show(fragmentManager, TAG);
    }

    /**
     * Update the profile with the new values
     */
    private void updateProfile() {
        profile.setKcal(Integer.parseInt(kcal.getText().toString()));
        profile.setFat(Integer.parseInt(fat.getText().toString()));
        profile.setCarbs(Integer.parseInt(carbs.getText().toString()));
        profile.setSugar(Integer.parseInt(sugar.getText().toString()));
        profile.setProtein(Integer.parseInt(protein.getText().toString()));

        profile.weight = Float.parseFloat(weight.getText().toString());
        profile.height = Integer.parseInt(height.getText().toString());
        profile.age = Integer.parseInt(age.getText().toString());
        profile.male = this.male.isChecked();

        profileViewModel.update(profile);
    }

    /**
     * Update the ui showing the current values for the profile
     */
    private void updateUI() {
        kcal.setText("" + profile.getKcal());
        fat.setText("" + profile.getFat());
        carbs.setText("" + profile.getCarbs());
        sugar.setText("" + profile.getSugar());
        protein.setText("" + profile.getProtein());

        weight.setText("" + profile.weight);
        height.setText("" + profile.height);
        age.setText("" + profile.age);
        male.setChecked(profile.male);
        female.setChecked(!profile.male);
    }

    private int calorieCalculator() {
        try {
            boolean male = this.male.isChecked();
            float height = Float.parseFloat(this.height.getText().toString());
            float weight = Float.parseFloat(this.weight.getText().toString());
            int age = Integer.parseInt(this.age.getText().toString());

            float paLevel = Float.parseFloat(((String) pal.getSelectedItem()).replaceAll(" - .*", ""));

            // Mifflin-St Jeor Equation
            int s = -161;
            if (male) {
                s = 5;
            }
            float bmr = 10f * weight + 6.25f * height - 5f * age + s;

            return (int) Math.ceil(bmr * paLevel);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }
}
