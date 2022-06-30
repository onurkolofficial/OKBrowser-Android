package com.onurkol.app.browser.data.browser;

public class HistoryData {
    private String mHistoryWebTitle;
    private String mHistoryWebUrl;
    private String mHistoryDate;

    public HistoryData(String historyTitle, String historyUrl, String historyDate) {
        mHistoryWebTitle=historyTitle;
        mHistoryWebUrl=historyUrl;
        mHistoryDate=historyDate;
    }

    public void setTitle(String Title){
        mHistoryWebTitle=Title;
    }
    public String getTitle(){
        return mHistoryWebTitle;
    }

    public void setUrl(String Url){
        mHistoryWebUrl=Url;
    }
    public String getUrl(){
        return mHistoryWebUrl;
    }

    public String getDate(){
        return mHistoryDate;
    }
}
