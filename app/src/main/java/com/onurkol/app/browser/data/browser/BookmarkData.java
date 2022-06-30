package com.onurkol.app.browser.data.browser;

public class BookmarkData {
    private String mBookmarkTitle;
    private String mBookmarkWebUrl;

    public BookmarkData(String bookmarkTitle, String bookmarkUrl) {
        mBookmarkTitle=bookmarkTitle;
        mBookmarkWebUrl=bookmarkUrl;
    }

    public void setTitle(String Title){
        mBookmarkTitle=Title;
    }
    public String getTitle(){
        return mBookmarkTitle;
    }

    public void setUrl(String Url){
        mBookmarkWebUrl=Url;
    }
    public String getUrl(){
        return mBookmarkWebUrl;
    }
}
