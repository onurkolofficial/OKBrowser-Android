package com.onurkol.app.browser.interfaces.tabs;

import com.onurkol.app.browser.data.tabs.RecentSearchData;

import java.util.ArrayList;

public interface RecentSearchInterface {
    String KEY_TAB_RECENT_SEARCH="BROWSER_TAB_RECENT_SEARCH";

    String RECENT_SEARCH_NO_DATA="NO_RECENT_SEARCH";

    ArrayList<RecentSearchData> RECENT_SEARCHES=new ArrayList<>();
}
