package com.onurkol.app.browser.data.tabs;

public class TabData {
    /**
     * WARNING! TabData is required only STRING Values
     * This Datas is Convert to JSON.
     * If add another data, going at 'ClassesTabData.java'
     **/
    private String webTitle;
    private String webUrl;

    public TabData(String tabTitle,String tabUrl){
        webTitle=tabTitle;
        webUrl=tabUrl;
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
}