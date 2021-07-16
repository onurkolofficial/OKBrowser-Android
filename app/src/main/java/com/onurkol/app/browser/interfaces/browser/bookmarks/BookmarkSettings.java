package com.onurkol.app.browser.interfaces.browser.bookmarks;

import com.onurkol.app.browser.data.browser.BookmarkData;

import java.util.ArrayList;
import java.util.List;

public interface BookmarkSettings {
    // Preference Keys
    String KEY_BOOKMARK_PREFERENCE="BROWSER_BOOKMARK_PREFERENCE";

    // Bookmark List
    List<BookmarkData> BROWSER_BOOKMARK_LIST=new ArrayList<>();
}
