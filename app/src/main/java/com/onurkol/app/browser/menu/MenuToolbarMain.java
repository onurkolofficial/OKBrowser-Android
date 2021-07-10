package com.onurkol.app.browser.menu;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.onurkol.app.browser.R;
import com.onurkol.app.browser.lib.ContextManager;
import com.onurkol.app.browser.lib.tabs.TabBuilder;
import com.onurkol.app.browser.lib.tabs.core.ToolbarTabCounter;

public class MenuToolbarMain {
    static PopupWindow popupWindow;

    // Classes
    static TabBuilder tabBuilder;
    static ToolbarTabCounter tabCounter;

    public static PopupWindow getMenu(){
        // Init Context
        ContextManager contextManager=ContextManager.getManager();
        Context context=contextManager.getContext();

        // Get Classes
        tabBuilder=TabBuilder.Build();
        tabCounter=new ToolbarTabCounter();

        // Elements
        LinearLayout exitBrowserLayoutButton, newTabLayoutButton, newIncognitoTabLayoutButton;

        // Get Popup Window
        popupWindow=new PopupWindow(context);

        // inflate your layout or dynamically add view
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.menu_toolbar_main, null);

        // Popup Window Settings
        popupWindow.setFocusable(true);
        popupWindow.setWidth(640);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setContentView(view);
        popupWindow.setElevation(20);

        // Get Elements
        exitBrowserLayoutButton=view.findViewById(R.id.exitBrowserLayoutButton);
        newTabLayoutButton=view.findViewById(R.id.newTabLayoutButton);
        newIncognitoTabLayoutButton=view.findViewById(R.id.newIncognitoTabLayoutButton);

        // Set Listeners
        exitBrowserLayoutButton.setOnClickListener(exitButtonListener);
        newTabLayoutButton.setOnClickListener(newTabButtonListener);
        newIncognitoTabLayoutButton.setOnClickListener(newIncognitoTabButtonListener);

        return popupWindow;
    }

    // Listeners
    // Exit Button
    static View.OnClickListener exitButtonListener=view -> {
        // Exit App
        System.exit(0);
    };
    // New Tab Button
    static View.OnClickListener newTabButtonListener=view -> {
        // Get Context Activity
        Activity activity=ContextManager.getManager().getContextActivity();
        // Get Elements
        ImageView incognitoIcon=activity.findViewById(R.id.incognitoIcon);
        ImageButton browserTabListButton=activity.findViewById(R.id.browserTabListButton);
        EditText browserSearch=activity.findViewById(R.id.browserSearch);

        // New Tab
        tabBuilder.createNewTab();
        // Show Incognito Icon
        incognitoIcon.setVisibility(View.GONE);
        // Update Tab Counts
        browserTabListButton.setImageDrawable(tabCounter.getTabCountDrawable());
        // Clear Url for New Tab
        browserSearch.setText("");
        // Dismiss Popup
        popupWindow.dismiss();
    };
    // New Incognito Tab Button
    static View.OnClickListener newIncognitoTabButtonListener=view -> {
        // Get Context Activity
        Activity activity=ContextManager.getManager().getContextActivity();
        // Get Elements
        ImageView incognitoIcon=activity.findViewById(R.id.incognitoIcon);
        ImageButton browserTabListButton=activity.findViewById(R.id.browserTabListButton);
        EditText browserSearch=activity.findViewById(R.id.browserSearch);

        // New Incognito Tab
        tabBuilder.createNewIncognitoTab();
        // Show Incognito Icon
        incognitoIcon.setVisibility(View.VISIBLE);
        // Update Tab Counts
        browserTabListButton.setImageDrawable(tabCounter.getIncognitoTabCountDrawable());
        // Clear Url for New Tab
        browserSearch.setText("");
        // Dismiss Popup
        popupWindow.dismiss();
    };
}
