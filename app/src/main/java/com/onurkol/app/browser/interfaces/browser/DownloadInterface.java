package com.onurkol.app.browser.interfaces.browser;

import com.onurkol.app.browser.data.browser.DownloadData;

import java.util.ArrayList;

public interface DownloadInterface {
    String KEY_BROWSER_DOWNLOAD="BROWSER_DOWNLOAD_DATA";

    String DOWNLOAD_NO_DATA="NO_DOWNLOAD";

    ArrayList<DownloadData> DOWNLOAD_LIST=new ArrayList<>();
}
