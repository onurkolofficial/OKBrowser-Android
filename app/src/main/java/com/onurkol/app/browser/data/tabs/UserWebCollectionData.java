package com.onurkol.app.browser.data.tabs;

public class UserWebCollectionData {
    private String webCollectionTitle;
    private String webCollectionUrl;

    public UserWebCollectionData(String webTitle, String webUrl) {
        webCollectionTitle=webTitle;
        webCollectionUrl=webUrl;
    }

    public void setTitle(String Title){
        webCollectionTitle=Title;
    }
    public String getTitle(){
        return webCollectionTitle;
    }

    public void setUrl(String Url){
        webCollectionUrl=Url;
    }
    public String getUrl(){
        return webCollectionUrl;
    }
}
