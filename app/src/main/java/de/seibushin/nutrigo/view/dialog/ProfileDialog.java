package de.seibushin.nutrigo.view.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
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

        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        profileViewModel.getProfile().observe(this, profile -> {
            this.profile = profile;
            updateUI();
        });

        return dialog;
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
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }
}
