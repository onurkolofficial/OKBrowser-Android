package com.onurkol.app.browser.interfaces.browser.bookmarks;

import com.onurkol.app.browser.data.browser.BookmarkData;

import java.util.ArrayList;

public interface BookmarkSettings {
    // Preference Keys
    String KEY_BOOKMARK_PREFERENCE="BROWSER_BOOKMARK_PREFERENCE";

    // Bookmark List
    ArrayList<BookmarkData> BROWSER_BOOKMARK_LIST=new ArrayList<>();
}
