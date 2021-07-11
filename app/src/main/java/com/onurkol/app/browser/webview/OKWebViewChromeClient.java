package com.onurkol.app.browser.webview;

import android.media.MediaPlayer;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.VideoView;

import androidx.annotation.Nullable;

import com.onurkol.app.browser.R;

public class OKWebViewChromeClient extends WebChromeClient {
    private View rootView;

    // WebView Chrome Clients
    public OKWebViewChromeClient(){/*NULL*/}

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        // Get RootView
        rootView = view.getRootView();

        if(rootView != null) {
            // Get Elements
            ProgressBar toolbarProgressBar = rootView.findViewById(R.id.browserProgressbar);

            if(toolbarProgressBar!=null) {
                // Show & Loading Progressbar
                if (newProgress >= 100) {
                    toolbarProgressBar.setProgress(0);
                    toolbarProgressBar.setVisibility(View.GONE);
                } else {
                    toolbarProgressBar.setProgress(newProgress);
                    toolbarProgressBar.setVisibility(View.VISIBLE);
                }
            }
        }
        super.onProgressChanged(view, newProgress);
    }
}
