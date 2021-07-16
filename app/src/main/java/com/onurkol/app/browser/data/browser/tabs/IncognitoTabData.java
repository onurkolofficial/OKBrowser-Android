package com.onurkol.app.browser.data.browser.tabs;

import android.graphics.Bitmap;

import com.onurkol.app.browser.fragments.browser.tabs.IncognitoTabFragment;

public class IncognitoTabData {
    /**
     * Incognito Tabs not saved. This free.
     **/
    private String webTitle;
    private String webUrl;
    private IncognitoTabFragment webIncognitoTabFragment;
    private Bitmap webPreview;

    public IncognitoTabData(String tabTitle,String tabUrl,IncognitoTabFragment incognitoTabFragment, Bitmap tabPreview){
        webTitle=tabTitle;
        webUrl=tabUrl;
        webIncognitoTabFragment=incognitoTabFragment;
        webPreview=tabPreview;
    }

    public void setWebTabFragment(IncognitoTabFragment newIncognitoTabFragment){
        webIncognitoTabFragment=newIncognitoTabFragment;
    }
    public IncognitoTabFragment getWebTabFragment(){
        return webIncognitoTabFragment;
    }

    public void setTitle(String Title){
        this.webTitle=Title;
    }
    public String getTitle(){
        return webTitle;
    }

    public void setUrl(String Url){
        this.webUrl=Url;
    }
    public String getUrl(){
        return webUrl;
    }

    public void setTabPreview(Bitmap tabPreview){
        webPreview=tabPreview;
    }
    public Bitmap getTabPreview(){
        return webPreview;
    }
}
