package de.seibushin.nutrigo.view.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import de.seibushin.nutrigo.R;
import de.seibushin.nutrigo.model.nutrition.Food;
import de.seibushin.nutrigo.model.nutrition.FoodDay;
import de.seibushin.nutrigo.model.nutrition.MealDay;
import de.seibushin.nutrigo.model.nutrition.NutritionUnit;

import static android.app.Activity.RESULT_OK;

public class ServingDialog extends DialogFragment {
    public static final String EXTRA_SERVING = "de.seibushin.nutrigo.serving.REPLY";
    public static final String EXTRA_HOUR = "de.seibushin.nutrigo.timestamp.HOUR";
    public static final String EXTRA_MINUTE = "de.seibushin.nutrigo.timestamp.MINUTE";
    private static final String TAG = "ServingDialog";

    private TextInputEditText serving;
    private TimePicker timePicker;
    private NutritionUnit nu;
    private ImageButton addServing;
    private ImageButton decServing;
    private TextView servingSize;

    private double addServ = 0;

    public interface ResultListener {
        void result(Double serving);
    }

    public ResultListener resultListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_serving, null);
        serving = view.findViewById(R.id.ti_portion);
        addServing = view.findViewById(R.id.addServing);
        addServing.setOnClickListener(v -> serving.setText("" + (Double.parseDouble(serving.getText().toString()) + addServ)));
        decServing = view.findViewById(R.id.decServing);
        decServing.setOnClickListener(v -> serving.setText("" + (Double.parseDouble(serving.getText().toString()) - addServ)));
        servingSize = view.findViewById(R.id.servingSize);
        timePicker = view.findViewById(R.id.timepicker);
        timePicker.setIs24HourView(true);
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setPositiveButton("Save", (dialog1, which) -> {
                    Intent reply = new Intent();
                    double servingSize = 0;
                    try {
                        servingSize = Double.parseDouble(serving.getText().toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    reply.putExtra(EXTRA_SERVING, servingSize);
                    reply.putExtra(EXTRA_HOUR, timePicker.getHour());
                    reply.putExtra(EXTRA_MINUTE, timePicker.getMinute());

                    try {
                        getTargetFragment().onActivityResult(getTargetRequestCode(), RESULT_OK, reply);
                    } catch (NullPointerException e) {
                        resultListener.result(servingSize);
                    }
                })
                .create();

        setValues();

        return dialog;
    }

    private void setValues() {
        if (nu.getClass() == FoodDay.class) {
            FoodDay food = (FoodDay) nu;
            serving.setText("" + food.serving);
            servingSize.setText("" + food.getOgPortion());
            addServ = food.getOgPortion();

            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(food.timestamp);

            timePicker.setHour(c.get(Calendar.HOUR_OF_DAY));
            timePicker.setMinute(c.get(Calendar.MINUTE));
        } else if (nu.getClass() == MealDay.class) {
            MealDay meal = (MealDay) nu;
            serving.setText("" + meal.serving);
            servingSize.setText("1.0");
            addServ = 1.0;

            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(meal.timestamp);

            timePicker.setHour(c.get(Calendar.HOUR_OF_DAY));
            timePicker.setMinute(c.get(Calendar.MINUTE));
        } else{
            serving.setText("" + nu.getPortion());
            servingSize.setText("" + nu.getPortion());
            addServ = nu.getPortion();

            timePicker.setVisibility(View.GONE);
        }
    }

    /**
     * show the dialog
     *
     * @param fragmentManager
     */
    public void show(FragmentManager fragmentManager, NutritionUnit nu) {
        this.nu = nu;
        show(fragmentManager, TAG);
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            resultListener = (ResultListener) getActivity();
        } catch (ClassCastException e) {
            Log.e("ServingDialog", e.getMessage(), e);
        }
    }
}
