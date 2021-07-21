package com.onurkol.app.browser.lib.browser;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.onurkol.app.browser.data.browser.BookmarkData;
import com.onurkol.app.browser.data.browser.HistoryData;
import com.onurkol.app.browser.interfaces.browser.bookmarks.BookmarkManagers;
import com.onurkol.app.browser.interfaces.browser.bookmarks.BookmarkSettings;
import com.onurkol.app.browser.lib.AppPreferenceManager;
import com.onurkol.app.browser.tools.ListToJson;

import java.util.ArrayList;

public class BookmarkManager implements BookmarkSettings, BookmarkManagers {
    private static BookmarkManager instance;

    // Classes
    AppPreferenceManager prefManager=AppPreferenceManager.getInstance();
    Gson gson=new Gson();

    public static synchronized BookmarkManager getInstance(){
        if(instance==null)
            instance=new BookmarkManager();
        return instance;
    }

    @Override
    public void newBookmark(BookmarkData bookmarkData) {
        // Get Preference Data
        if(BROWSER_BOOKMARK_LIST.size()<=0)
            syncSavedBookmarkData();
        // Add Data
        BROWSER_BOOKMARK_LIST.add(0,bookmarkData);
        // Save Preferences
        saveBookmarkListPreference(BROWSER_BOOKMARK_LIST);
    }

    @Override
    public void saveBookmarkListPreference(ArrayList<BookmarkData> bookmarkData) {
        String listData;
        if(bookmarkData.size()<=0)
            listData="";
        else
            listData=ListToJson.getJson(bookmarkData);
        // Save Preference
        prefManager.setPreference(KEY_BOOKMARK_PREFERENCE,listData);
    }

    @Override
    public ArrayList<BookmarkData> getSavedBookmarkData() {
        if(prefManager==null)
            prefManager=AppPreferenceManager.getInstance();
        // New Data
        ArrayList<BookmarkData> savedBookmarksList=new ArrayList<>();
        // Get Preference
        String savedBookmarksString=prefManager.getString(KEY_BOOKMARK_PREFERENCE);
        if(savedBookmarksString!=null && !savedBookmarksString.equals(""))
            // Convert String to List
            savedBookmarksList=gson.fromJson(savedBookmarksString, new TypeToken<ArrayList<BookmarkData>>(){}.getType());
        return savedBookmarksList;
    }

    @Override
    public void syncSavedBookmarkData() {
        // Clear History Data
        BROWSER_BOOKMARK_LIST.clear();
        // Sync
        BROWSER_BOOKMARK_LIST.addAll(getSavedBookmarkData());
    }
}
