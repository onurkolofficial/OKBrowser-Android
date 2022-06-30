package com.onurkol.app.browser.interfaces.tabs;

import androidx.annotation.NonNull;

import com.onurkol.app.browser.data.tabs.RecentSearchData;

import java.util.ArrayList;

public interface RecentSearchControllerInterface {
    void newSearch(@NonNull String searchSentence);
    void deleteSearch(int position);
    void deleteAllSearch();

    void syncRecentSearchData();

    void saveRecentSearchDataPreference();

    ArrayList<RecentSearchData> getRecentSearchList();
}
