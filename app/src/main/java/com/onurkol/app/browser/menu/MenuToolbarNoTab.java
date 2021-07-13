package com.onurkol.app.browser.menu;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.onurkol.app.browser.R;
import com.onurkol.app.browser.activity.SettingsActivity;
import com.onurkol.app.browser.activity.browser.BookmarkActivity;
import com.onurkol.app.browser.activity.browser.HistoryActivity;
import com.onurkol.app.browser.activity.browser.core.DownloadsActivity;
import com.onurkol.app.browser.lib.ContextManager;

public class MenuToolbarNoTab {
    public static PopupWindow getMenu(){
        // Init Context
        ContextManager contextManager=ContextManager.getManager();
        Context context=contextManager.getContext();

        // Elements
        LinearLayout noTabExitBrowserLayoutButton,noTabHistoryLayoutButton,noTabBookmarksLayoutButton,
                noTabDownloadsLayoutButton,noTabSettingsLayoutButton;
        // Intents
        Intent historyIntent,downloadsIntent,bookmarksIntent,settingsIntent;

        // Get Popup Window
        final PopupWindow popupWindow=new PopupWindow(context);

        // inflate your layout or dynamically add view
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.menu_toolbar_no_tab, null);

        // Popup Window Settings
        popupWindow.setFocusable(true);
        popupWindow.setWidth(640);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setContentView(view);
        popupWindow.setElevation(20);

        // Get Intents
        historyIntent=new Intent(context, HistoryActivity.class);
        downloadsIntent=new Intent(context, DownloadsActivity.class);
        bookmarksIntent=new Intent(context, BookmarkActivity.class);
        settingsIntent=new Intent(context, SettingsActivity.class);

        // Get Elements
        noTabExitBrowserLayoutButton=view.findViewById(R.id.noTabExitBrowserLayoutButton);
        noTabHistoryLayoutButton=view.findViewById(R.id.noTabHistoryLayoutButton);
        noTabBookmarksLayoutButton=view.findViewById(R.id.noTabBookmarksLayoutButton);
        noTabDownloadsLayoutButton=view.findViewById(R.id.noTabDownloadsLayoutButton);
        noTabSettingsLayoutButton=view.findViewById(R.id.noTabSettingsLayoutButton);

        // Set Listeners
        noTabExitBrowserLayoutButton.setOnClickListener(exitButtonListener);
        noTabBookmarksLayoutButton.setOnClickListener(v -> {
                    context.startActivity(bookmarksIntent);
                    popupWindow.dismiss();
                });
        noTabSettingsLayoutButton.setOnClickListener(v -> {
            context.startActivity(settingsIntent);
            popupWindow.dismiss();
        });
        noTabDownloadsLayoutButton.setOnClickListener(v -> {
            context.startActivity(downloadsIntent);
            popupWindow.dismiss();
        });
        noTabHistoryLayoutButton.setOnClickListener(v -> {
            context.startActivity(historyIntent);
            popupWindow.dismiss();
        });

        return popupWindow;
    }

    // Listeners
    static View.OnClickListener exitButtonListener=view -> {
        // Exit App
        System.exit(0);
    };
}
