package de.seibushin.nutrigo.view.widget;

import android.content.Context;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;

import com.google.android.material.textfield.TextInputEditText;

public class TextInputEditTextWithSuggestion extends TextInputEditText {
    public TextInputEditTextWithSuggestion(Context context) {
        super(context);
    }

    public TextInputEditTextWithSuggestion(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextInputEditTextWithSuggestion(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            System.out.println("DROP FOCUS");
            clearFocus();
        }

        return super.onKeyPreIme(keyCode, event);
    }

    @Override
    public void onEditorAction(int actionCode) {
        if (actionCode == EditorInfo.IME_ACTION_NEXT) {
            System.out.println("NEXT");
            clearFocus();
        }

        super.onEditorAction(actionCode);
    }

    @Override
    public void addTextChangedListener(TextWatcher watcher) {
        super.addTextChangedListener(watcher);
    }
}
