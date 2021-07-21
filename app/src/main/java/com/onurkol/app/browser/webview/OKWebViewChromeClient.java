package com.onurkol.app.browser.webview;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.util.Log;
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

public class OKWebViewChromeClient extends WebChromeClient implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {
    private View rootView;

    public interface ToggledFullscreenCallback {
        public void toggledFullscreen(boolean fullscreen);
    }

    private View mWebViewLayout;
    private ViewGroup mWebViewVideoLayout;
    private View mLoadingView;
    private OKWebView mWebView;

    private boolean isVideoFullscreen; // Indicates if the video is being displayed using a custom view (typically full-screen)
    private FrameLayout videoViewContainer;
    private CustomViewCallback videoViewCallback;

    private ToggledFullscreenCallback toggledFullscreenCallback;

    // WebView Chrome Clients
    public OKWebViewChromeClient(){/*NULL*/}
    public OKWebViewChromeClient(View webViewLayout, ViewGroup webViewVideoLayout){
        this.mWebViewLayout = webViewLayout;
        this.mWebViewVideoLayout = webViewVideoLayout;
        this.mLoadingView = null;
        this.mWebView = null;
        this.isVideoFullscreen = false;
    }
    public OKWebViewChromeClient(View webViewLayout, ViewGroup webViewVideoLayout, View loadingView){
        this.mWebViewLayout = webViewLayout;
        this.mWebViewVideoLayout = webViewVideoLayout;
        this.mLoadingView = loadingView;
        this.mWebView = null;
        this.isVideoFullscreen = false;
    }
    public OKWebViewChromeClient(View webViewLayout, ViewGroup webViewVideoLayout, View loadingView, OKWebView webView){
        this.mWebViewLayout = webViewLayout;
        this.mWebViewVideoLayout = webViewVideoLayout;
        this.mLoadingView = loadingView;
        this.mWebView = webView;
        this.isVideoFullscreen = false;
    }

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

    // Fullscreen Video View
    public boolean isVideoFullscreen(){
        return isVideoFullscreen;
    }
    public void setOnToggledFullscreen(ToggledFullscreenCallback callback) {
        this.toggledFullscreenCallback = callback;
    }

    @Override
    public void onShowCustomView(View view, CustomViewCallback callback) {
        if (view instanceof FrameLayout){
            FrameLayout frameLayout = (FrameLayout) view;
            View focusedChild = frameLayout.getFocusedChild();

            this.isVideoFullscreen = true;
            this.videoViewContainer = frameLayout;
            this.videoViewCallback = callback;

            // Hide WebView and show Video View.
            mWebViewLayout.setVisibility(View.INVISIBLE);
            mWebViewVideoLayout.addView(videoViewContainer, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            mWebViewVideoLayout.setVisibility(View.VISIBLE);

            if(focusedChild instanceof android.widget.VideoView){
                VideoView videoView = (VideoView)focusedChild;

                videoView.setOnPreparedListener(this);
                videoView.setOnCompletionListener(this);
                videoView.setOnErrorListener(this);
            }
            else{
                // Other classes
                if(mWebView != null && mWebView.getSettings().getJavaScriptEnabled() && focusedChild instanceof SurfaceView){
                    // Run javascript code that detects the video end and notifies the Javascript interface
                    String js = "javascript:";
                    js += "var _ytrp_html5_video_last;";
                    js += "var _ytrp_html5_video = document.getElementsByTagName('video')[0];";
                    js += "if (_ytrp_html5_video != undefined && _ytrp_html5_video != _ytrp_html5_video_last) {";
                    {
                        js += "_ytrp_html5_video_last = _ytrp_html5_video;";
                        js += "function _ytrp_html5_video_ended() {";
                        {
                            js += "OKWebView.notifyVideoEnd();"; // Must match Javascript interface name and method of OKWebView
                        }
                        js += "}";
                        js += "_ytrp_html5_video.addEventListener('ended', _ytrp_html5_video_ended);";
                    }
                    js += "}";
                    mWebView.loadUrl(js);
                }
            }
            // Notify full-screen change
            if (toggledFullscreenCallback != null) {
                toggledFullscreenCallback.toggledFullscreen(true);
            }
        }
    }

    @Override
    public void onHideCustomView() {
        if(isVideoFullscreen){
            mWebViewVideoLayout.setVisibility(View.INVISIBLE);
            mWebViewVideoLayout.removeView(videoViewContainer);
            mWebViewLayout.setVisibility(View.VISIBLE);
        }

        if (videoViewCallback != null && !videoViewCallback.getClass().getName().contains(".chromium."))
            videoViewCallback.onCustomViewHidden();

        // Reset values
        isVideoFullscreen = false;
        videoViewContainer = null;
        videoViewCallback = null;

        if (toggledFullscreenCallback != null) {
            toggledFullscreenCallback.toggledFullscreen(false);
        }
    }

    @Nullable
    @Override
    public View getVideoLoadingProgressView() {
        if (mLoadingView != null){
            mLoadingView.setVisibility(View.VISIBLE);
            return mLoadingView;
        }
        else{
            return super.getVideoLoadingProgressView();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        onHideCustomView();
    }
    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        return false;
    }
    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        if (mLoadingView != null) {
            mLoadingView.setVisibility(View.GONE);
        }
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
