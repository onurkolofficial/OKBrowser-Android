package com.onurkol.app.browser.controller;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class KeyboardController {
    static InputMethodManager inputMethodManager;

    public static void hideKeyboard(Context context, View view){
        inputMethodManager=(InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        // Hide Virtual Keyboard
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    public static void showKeyboard(Context context, View view){
        inputMethodManager=(InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        // Show Virtual Keyboard
        inputMethodManager.showSoftInput(view,0);
    }
}
