package com.onurkol.app.browser.tools;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.onurkol.app.browser.lib.ContextManager;

public class KeyboardController {
    static InputMethodManager inputMethodManager;

    public static void hideKeyboard(View view){
        Context context= ContextManager.getManager().getContext();
        inputMethodManager=(InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        // Hide Virtual Keyboard
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    public static void showKeyboard(View view){
        Context context= ContextManager.getManager().getContext();
        inputMethodManager=(InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        // Show Virtual Keyboard
        inputMethodManager.showSoftInput(view,0);
    }
}
