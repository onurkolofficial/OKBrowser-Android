package com.onurkol.app.browser.menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.onurkol.app.browser.R;
import com.onurkol.app.browser.lib.ContextManager;

public class MenuToolbarNoTab {
    public static PopupWindow getMenu(){
        // Init Context
        ContextManager contextManager=ContextManager.getManager();
        Context context=contextManager.getContext();

        // Elements
        LinearLayout noTabExitBrowserLayoutButton;

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

        // Get Elements
        noTabExitBrowserLayoutButton=view.findViewById(R.id.noTabExitBrowserLayoutButton);

        // Set Listeners
        noTabExitBrowserLayoutButton.setOnClickListener(exitButtonListener);

        return popupWindow;
    }

    // Listeners
    static View.OnClickListener exitButtonListener=view -> {
        // Exit App
        System.exit(0);
    };
}
