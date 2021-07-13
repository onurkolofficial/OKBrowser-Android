package com.onurkol.app.browser.webview.listeners;

import android.app.Activity;
import android.view.View;
import android.view.WindowManager;

import com.onurkol.app.browser.R;
import com.onurkol.app.browser.lib.ContextManager;
import com.onurkol.app.browser.webview.OKWebViewChromeClient;

public class VideoFullScreen {
    public static OKWebViewChromeClient.ToggledFullscreenCallback fullscreenCallback=fullscreen -> {
        Activity activity=ContextManager.getManager().getContextActivity();
        WindowManager.LayoutParams attrs = activity.getWindow().getAttributes();
        if (fullscreen) {
            // Hide Toolbar
            activity.findViewById(R.id.includeTabToolbar).setVisibility(View.GONE);

            attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            attrs.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
            activity.getWindow().setAttributes(attrs);
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
        }
        else{
            // Show Toolbar
            activity.findViewById(R.id.includeTabToolbar).setVisibility(View.VISIBLE);

            attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
            attrs.flags &= ~WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
            activity.getWindow().setAttributes(attrs);
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }
    };
}
