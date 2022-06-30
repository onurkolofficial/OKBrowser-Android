package com.onurkol.app.browser.data.tabs;

public class RecentSearchData {
    private String tabSearchSentence;

    public RecentSearchData(String searchSentence){
        tabSearchSentence=searchSentence;
    }

    public void setSearchSentence(String searchSentence){
        tabSearchSentence=searchSentence;
    }
    public String getSearchSentence(){
        return tabSearchSentence;
    }
}
