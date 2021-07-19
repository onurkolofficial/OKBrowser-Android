package com.onurkol.app.browser.menu.webview;

import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.onurkol.app.browser.R;
import com.onurkol.app.browser.lib.ContextManager;
import com.onurkol.app.browser.tools.CharLimiter;
import com.onurkol.app.browser.tools.JavascriptManager;

public class MenuWebViewContext {
    public static String KEY_MENU_IMAGE="BROWSER_MENU_IMAGE",
            KEY_MENU_ANCHOR="BROWSER_MENU_ANCHOR",
            KEY_MENU_IMAGE_ANCHOR="BROWSER_MENU_IMAGE_ANCHOR";

    // Get Image Menu
    public static synchronized PopupWindow getImageContextMenu() {
        return WebContextMenuClass(KEY_MENU_IMAGE, null, "", "");
    }
    public static synchronized PopupWindow getImageContextMenu(String imageUrl) {
        return WebContextMenuClass(KEY_MENU_IMAGE, null, imageUrl, "");
    }
    public static synchronized PopupWindow getImageContextMenu(String imageUrl, String title) {
        return WebContextMenuClass(KEY_MENU_IMAGE, null, imageUrl, title);
    }

    // Get Anchor Context Menu
    public static synchronized PopupWindow getAnchorContextMenu() {
        return WebContextMenuClass(KEY_MENU_ANCHOR, "", null, "");
    }
    public static synchronized PopupWindow getAnchorContextMenu(String url) {
        return WebContextMenuClass(KEY_MENU_ANCHOR, url, null, "");
    }
    public static synchronized PopupWindow getAnchorContextMenu(String url, String title) {
        return WebContextMenuClass(KEY_MENU_ANCHOR, url, null, title);
    }

    // Get Image Anchor Context Menu
    public static synchronized PopupWindow getImageAnchorContextMenu() {
        return WebContextMenuClass(KEY_MENU_IMAGE_ANCHOR, "", null, "");
    }
    public static synchronized PopupWindow getImageAnchorContextMenu(String webUrl) {
        return WebContextMenuClass(KEY_MENU_IMAGE_ANCHOR, webUrl, null, "");
    }
    public static synchronized PopupWindow getImageAnchorContextMenu(String webUrl, String imageUrl) {
        return WebContextMenuClass(KEY_MENU_IMAGE_ANCHOR, webUrl, imageUrl, "");
    }
    public static synchronized PopupWindow getImageAnchorContextMenu(String webUrl, String imageUrl, String title) {
        return WebContextMenuClass(KEY_MENU_IMAGE_ANCHOR, webUrl, imageUrl, title);
    }

    // Values
    static String getLink, getImageLink, getTitle;

    // Services
    static ClipboardManager clipboard = (ClipboardManager) ContextManager.getManager().getContext().getSystemService(Context.CLIPBOARD_SERVICE);

    // Popup Window
    static PopupWindow popupWindow;

    // Main Class
    static PopupWindow WebContextMenuClass(String type, String url, String imageUrl, String title) {
        Context context = ContextManager.getManager().getContext();
        // Set Popup Window
        popupWindow = new PopupWindow(context);
        // Layout Inflater
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        TextView showWebUrl;
        Button openNewTabButton = null,
                openIncognitoTabButton = null,
                openImageButton = null,
                openImageNewTabButton = null,
                openImageIncognitoTabButton = null,
                copyUrlButton = null,
                copyTitleButton = null,
                downloadImageButton = null;

        // Set Values
        getLink = url;
        getTitle = title;
        getImageLink = imageUrl;

        // Convert URL
        String convertLink = CharLimiter.Limit(url, 55);
        String convertImageLink = CharLimiter.Limit(imageUrl, 55);

        // Inflating Menu Layouts
        if(type.equals(KEY_MENU_IMAGE)){
            // Image Context Menu
            View view = inflater.inflate(R.layout.menu_webview_image, null);
            // Set Popup View
            popupWindow.setContentView(view);

            // Get Elements
            showWebUrl=view.findViewById(R.id.showWebUrl);

            // Set Text
            showWebUrl.setText(convertImageLink);
        }
        else if(type.equals(KEY_MENU_ANCHOR)){
            // Image Context Menu
            View view = inflater.inflate(R.layout.menu_webview_anchor, null);
            // Set Popup View
            popupWindow.setContentView(view);

            // Get Elements
            showWebUrl=view.findViewById(R.id.showWebUrl);

            // Set Text
            showWebUrl.setText(convertLink);
        }
        else if(type.equals(KEY_MENU_IMAGE_ANCHOR)){
            // Image Context Menu
            View view = inflater.inflate(R.layout.menu_webview_image_anchor, null);
            // Set Popup View
            popupWindow.setContentView(view);

            // Get Elements
            showWebUrl=view.findViewById(R.id.showWebUrl);

            // Set Text
            showWebUrl.setText(convertLink);
        }

        // Set Popup Window Settings
        popupWindow.setFocusable(true);
        popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setElevation(20);
        popupWindow.setOnDismissListener(dismissListenerContextMenu);

        return popupWindow;
    }

    // Listeners
    static PopupWindow.OnDismissListener dismissListenerContextMenu=() -> {
        // Reset Background
        JavascriptManager.getManager().getWebView().getRootView().setAlpha(1);
    };
}
