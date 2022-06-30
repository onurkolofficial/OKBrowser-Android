package com.onurkol.app.browser.interfaces.tabs;

import com.onurkol.app.browser.data.tabs.UserWebCollectionData;

import java.util.ArrayList;

public interface UserWebCollectionInterface {
    String KEY_TAB_USER_WEB_COLLECTION="BROWSER_TAB_USER_WEB_COLLECTION";

    String COLLECTION_NO_DATA="NO_COLLECTION";

    int COLLECTION_GRID_COUNT=3,
            COLLECTION_GRID_COUNT_LANDSCAPE=5;

    ArrayList<UserWebCollectionData> TAB_USER_WEB_COLLECTION=new ArrayList<>();
}
