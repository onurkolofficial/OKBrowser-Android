package com.onurkol.app.browser.controller.gui;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.onurkol.app.browser.R;
import com.onurkol.app.browser.activity.browser.BookmarkActivity;
import com.onurkol.app.browser.activity.browser.DownloadsActivity;
import com.onurkol.app.browser.activity.browser.HistoryActivity;
import com.onurkol.app.browser.activity.browser.SettingsActivity;
import com.onurkol.app.browser.libs.ActivityActionAnimator;

public class CommonMenuNoTabController {
    // This class is a continuation of 'MenuToolbarNoTab' and 'BottomSheetToolbarNoTab'.
    // 'java/menu/toolbars/MenuToolbarNoTab' , 'java/bottomsheets/toolbars/BottomSheetToolbarNoTab'

    public static void loadMenuController(Context context, View view, PopupWindow popupWindow){
        loadMenuControllerClass(context, view, popupWindow, null);
    }

    public static void loadMenuController(Context context, View view, BottomSheetDialog bottomSheetDialog){
        loadMenuControllerClass(context, view, null, bottomSheetDialog);
    }

    private static void loadMenuControllerClass(Context context, View view, PopupWindow popupWindow, BottomSheetDialog bottomSheetDialog){
        LinearLayout settingsLayoutButton, downloadsLayoutButton, historyLayoutButton, bookmarksLayoutButton, exitBrowserLayoutButton;

        settingsLayoutButton=view.findViewById(R.id.settingsLayoutButton);
        downloadsLayoutButton=view.findViewById(R.id.downloadsLayoutButton);
        historyLayoutButton=view.findViewById(R.id.historyLayoutButton);
        bookmarksLayoutButton=view.findViewById(R.id.bookmarksLayoutButton);
        exitBrowserLayoutButton=view.findViewById(R.id.exitBrowserLayoutButton);

        settingsLayoutButton.setOnClickListener(v -> {
            ActivityActionAnimator.startActivity(context, new Intent(context, SettingsActivity.class));
            dismissDialog(popupWindow, bottomSheetDialog);
        });
        downloadsLayoutButton.setOnClickListener(v -> {
            ActivityActionAnimator.startActivity(context, new Intent(context, DownloadsActivity.class));
            dismissDialog(popupWindow, bottomSheetDialog);
        });
        historyLayoutButton.setOnClickListener(v -> {
            ActivityActionAnimator.startActivity(context, new Intent(context, HistoryActivity.class));
            dismissDialog(popupWindow, bottomSheetDialog);
        });
        bookmarksLayoutButton.setOnClickListener(v -> {
            ActivityActionAnimator.startActivity(context, new Intent(context, BookmarkActivity.class));
            dismissDialog(popupWindow, bottomSheetDialog);
        });
        exitBrowserLayoutButton.setOnClickListener(v -> System.exit(0));
    }

    private static void dismissDialog(PopupWindow popupWindow, BottomSheetDialog bottomSheetDialog){
        if(popupWindow!=null)
            popupWindow.dismiss();
        if(bottomSheetDialog!=null)
            bottomSheetDialog.dismiss();
    }
}
