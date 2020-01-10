package de.seibushin.nutrigo.view.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.textfield.TextInputEditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import de.seibushin.nutrigo.R;

import static android.app.Activity.RESULT_OK;

public class ServingDialog extends DialogFragment {
    public static final String EXTRA_SERVING = "de.seibushin.nutrigo.serving.REPLY";
    private static final String TAG = "ServingDialog";

    private TextInputEditText serving;

    public interface ResultListener {
        void result(Double serving);
    }

    public ResultListener resultListener;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_serving, null);
        serving = view.findViewById(R.id.ti_portion);
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setPositiveButton("Save", (dialog1, which) -> {
                    Intent reply = new Intent();
                    reply.putExtra(EXTRA_SERVING, Double.parseDouble(serving.getText().toString()));

                    try {
                        getTargetFragment().onActivityResult(getTargetRequestCode(), RESULT_OK, reply);
                    } catch (NullPointerException e) {
                        resultListener.result(Double.parseDouble(serving.getText().toString()));
                    }

                })
                .create();

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
