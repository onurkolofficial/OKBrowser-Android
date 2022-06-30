package com.onurkol.app.browser.interfaces.browser;

import com.onurkol.app.browser.data.browser.BookmarkData;
import com.onurkol.app.browser.data.browser.DownloadData;

import java.util.ArrayList;

public interface BookmarkInterface {
    String KEY_BROWSER_BOOKMARK="BROWSER_BOOKMARK_DATA";

    String BOOKMARK_NO_DATA="NO_BOOKMARK";

    ArrayList<BookmarkData> BOOKMARK_LIST=new ArrayList<>();
}
