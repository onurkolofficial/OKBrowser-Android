package com.onurkol.app.browser.interfaces.browser.bookmarks;

import com.onurkol.app.browser.data.browser.BookmarkData;

import java.util.ArrayList;

public interface BookmarkManagers {
    void newBookmark(BookmarkData bookmarkData);
    void saveBookmarkListPreference(ArrayList<BookmarkData> bookmarkData);
    ArrayList<BookmarkData> getSavedBookmarkData();
    void syncSavedBookmarkData();
}
