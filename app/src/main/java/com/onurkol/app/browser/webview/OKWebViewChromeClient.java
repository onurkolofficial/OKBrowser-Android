package com.onurkol.app.browser.webview;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.onurkol.app.browser.R;
import com.onurkol.app.browser.controller.settings.GUIController;

public class OKWebViewChromeClient extends WebChromeClient {
    private View mVideoProgressView, mCustomView;
    private final FrameLayout customViewContainer;
    private final OKWebView mWebView;

    private final Context wcContext;
    private WebChromeClient.CustomViewCallback customViewCallback;

    private boolean isVideoFullscreen;

    public OKWebViewChromeClient(Context context, OKWebView webView, FrameLayout viewContainer){
        mWebView=webView;
        customViewContainer=viewContainer;
        wcContext=context;
        isVideoFullscreen=false;
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        // Get RootView
        View rootView = view.getRootView();
        if(rootView!=null) {
            // Get Elements
            ProgressBar browserToolbarProgressbar=rootView.findViewById(R.id.browserToolbarProgressbar);

            if(browserToolbarProgressbar!=null) {
                // Show & Loading Progressbar
                if (newProgress >= 100) {
                    browserToolbarProgressbar.setProgress(0);
                    browserToolbarProgressbar.setVisibility(View.GONE);
                } else {
                    browserToolbarProgressbar.setProgress(newProgress);
                    browserToolbarProgressbar.setVisibility(View.VISIBLE);
                }
            }
        }
        super.onProgressChanged(view, newProgress);
    }

    @Override
    public void onShowCustomView(View view, CustomViewCallback callback) {
        // if a view already exists then immediately terminate the new one
        if (mCustomView != null) {
            callback.onCustomViewHidden();
            return;
        }
        // Hide Status Bar
        WindowManager.LayoutParams attrs = ((Activity)wcContext).getWindow().getAttributes();
        attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        attrs.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        ((Activity)wcContext).getWindow().setAttributes(attrs);
        ((Activity)wcContext).getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);

        // Hide Bottom toolbar (dense mode)
        if(GUIController.getController().isDenseMode())
            ((Activity)wcContext).findViewById(R.id.toolbarMainDenseBottomView).setVisibility(View.GONE);
        mCustomView = view;
        mWebView.setVisibility(View.GONE);
        customViewContainer.setVisibility(View.VISIBLE);
        customViewContainer.addView(view);
        customViewCallback = callback;
        isVideoFullscreen = true;
    }

    @Override
    public View getVideoLoadingProgressView() {
        if (mVideoProgressView == null) {
            LayoutInflater inflater = LayoutInflater.from(wcContext);
            mVideoProgressView = inflater.inflate(R.layout.screen_video_loading, null);
        }
        return mVideoProgressView;
    }

    @Override
    public void onHideCustomView() {
        super.onHideCustomView(); //To change body of overridden methods use File | Settings | File Templates.
        if (mCustomView == null)
            return;

        // Show Status Bar
        WindowManager.LayoutParams attrs = ((Activity)wcContext).getWindow().getAttributes();
        attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
        attrs.flags &= ~WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        ((Activity)wcContext).getWindow().setAttributes(attrs);
        ((Activity)wcContext).getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);

        // Show Bottom toolbar (dense mode)
        if(GUIController.getController().isDenseMode())
            ((Activity)wcContext).findViewById(R.id.toolbarMainDenseBottomView).setVisibility(View.VISIBLE);
        mWebView.setVisibility(View.VISIBLE);
        customViewContainer.setVisibility(View.GONE);

        // Hide the custom view.
        mCustomView.setVisibility(View.GONE);

        // Remove the custom view from its container.
        customViewContainer.removeView(mCustomView);
        customViewCallback.onCustomViewHidden();

        mCustomView = null;
        isVideoFullscreen=false;
    }

    public boolean onBackPressed(){
        if (isVideoFullscreen) {
            onHideCustomView();
            return true;
        }
        else
            return false;
    }
}
