package com.onurkol.app.browser.data.tabs;

public class TabDataPreference {
    private int webTabIndex;
    private String webTitle;
    private String webUrl;

    public TabDataPreference(int tabIndex, String tabTitle, String tabUrl){
        webTabIndex=tabIndex;
        webTitle=tabTitle;
        webUrl=tabUrl;
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
}
