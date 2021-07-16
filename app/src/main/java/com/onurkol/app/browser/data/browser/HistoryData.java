package com.onurkol.app.browser.data.browser;

public class HistoryData {
    private String getHistoryDate, getHistoryTitle, getHistoryUrl;

    public HistoryData(String historyTitle,String historyUrl, String historyDate){
        getHistoryDate=historyDate;
        getHistoryTitle=historyTitle;
        getHistoryUrl=historyUrl;
    }

    public String getTitle(){
        return getHistoryTitle;
    }
    public String getUrl(){
        return getHistoryUrl;
    }
    public String getDate() {
        return getHistoryDate;
    }
}
