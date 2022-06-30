package com.onurkol.app.browser.controller.browser;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.onurkol.app.browser.controller.PreferenceController;
import com.onurkol.app.browser.data.browser.BookmarkData;
import com.onurkol.app.browser.interfaces.BrowserDataInterface;
import com.onurkol.app.browser.interfaces.browser.BookmarkControllerInterface;
import com.onurkol.app.browser.libs.ListToJson;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class BookmarkController implements BookmarkControllerInterface, BrowserDataInterface {
    private static WeakReference<BookmarkController> instance=null;
    PreferenceController preferenceController;

    Gson gson=new Gson();

    private BookmarkController(){
        preferenceController=PreferenceController.getController();
    }

    public static synchronized BookmarkController getController(){
        if(instance==null || instance.get()==null)
            instance=new WeakReference<>(new BookmarkController());
        return instance.get();
    }

    @Override
    public void newBookmark(String bookmarkTitle, String bookmarkUrl) {
        BookmarkData data=new BookmarkData(bookmarkTitle, bookmarkUrl);
        BOOKMARK_LIST.add(data);
        saveBookmarkDataPreference();
    }

    @Override
    public void deleteBookmark(int position) {
        BOOKMARK_LIST.remove(position);
        saveBookmarkDataPreference();
    }

    @Override
    public void deleteAllBookmarks() {
        BOOKMARK_LIST.clear();
        saveBookmarkDataPreference();
    }

    @Override
    public void updateBookmark(int position, BookmarkData bookmarkData) {
        BOOKMARK_LIST.set(position, bookmarkData);
        saveBookmarkDataPreference();
    }

    @Override
    public void syncBookmarkData() {
        String bookmarkPreferenceList=preferenceController.getString(KEY_BROWSER_BOOKMARK);
        if(bookmarkPreferenceList!=null && !bookmarkPreferenceList.equals(BOOKMARK_NO_DATA)) {
            ArrayList<BookmarkData> getBookmarkPreferenceData = new ArrayList<>(
                    gson.fromJson(bookmarkPreferenceList, new TypeToken<ArrayList<BookmarkData>>() {
                    }.getType()));
            BOOKMARK_LIST.clear();
            BOOKMARK_LIST.addAll(getBookmarkPreferenceData);
        }
    }

    @Override
    public void saveBookmarkDataPreference() {
        if(BOOKMARK_LIST.size()<=0) {
            preferenceController.setPreference(KEY_BROWSER_BOOKMARK, BOOKMARK_NO_DATA);
        }
        else{
            String newData=ListToJson.getJson(BOOKMARK_LIST);
            preferenceController.setPreference(KEY_BROWSER_BOOKMARK, newData);
        }
    }

    @Override
    public ArrayList<BookmarkData> getBookmarkList() {
        return BOOKMARK_LIST;
    }
}
