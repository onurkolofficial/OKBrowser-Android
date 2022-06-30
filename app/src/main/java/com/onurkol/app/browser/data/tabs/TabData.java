package com.onurkol.app.browser.data.tabs;

import android.graphics.Bitmap;

import androidx.annotation.Nullable;

import com.onurkol.app.browser.fragments.tabs.TabFragment;

public class TabData {
    private int webTabIndex;
    private String webTitle;
    private String webUrl;
    private Bitmap webPreview;
    private TabFragment webTabFragment;

    public TabData(int tabIndex, String tabTitle, String tabUrl, Bitmap tabPreview, TabFragment tabFragment){
        webTabIndex=tabIndex;
        webTitle=tabTitle;
        webUrl=tabUrl;
        webPreview=tabPreview;
        webTabFragment=tabFragment;
    }

    public void setTitle(String Title){
        webTitle=Title;
    }
    public String getTitle(){
        return webTitle;
    }

    public void setUrl(String Url){
        webUrl=Url;
    }
    public String getUrl(){
        return webUrl;
    }

    public void setTabIndex(int Index){
        webTabIndex=Index;
    }
    public int getTabIndex(){
        return webTabIndex;
    }

    public void setPreviewBitmap(Bitmap bitmap){
        webPreview=bitmap;
    }
    @Nullable
    public Bitmap getPreviewBitmap(){
        return webPreview;
    }

    public void setTabFragment(TabFragment fragment){
        webTabFragment=fragment;
    }
    public TabFragment getTabFragment(){
        return webTabFragment;
    }
}
