package com.onurkol.app.browser.data.browser;

public class BookmarkData {
    private String getBookmarkTitle, getBookmarkUrl;

    public BookmarkData(String bookmarkTitle,String bookmarkUrl){
        getBookmarkTitle=bookmarkTitle;
        getBookmarkUrl=bookmarkUrl;
    }

    public String getTitle(){
        return getBookmarkTitle;
    }
    public String getUrl(){
        return getBookmarkUrl;
    }
}
