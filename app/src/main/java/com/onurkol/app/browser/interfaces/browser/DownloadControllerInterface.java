package com.onurkol.app.browser.interfaces.browser;

import android.app.DownloadManager;
import android.view.View;

import com.onurkol.app.browser.data.browser.DownloadData;

import java.util.ArrayList;

public interface DownloadControllerInterface {
    void newDownload(String downloadFileName, String downloadFolder);
    void deleteDownload(int position);
    void deleteAllDownloads();

    void syncDownloadData();

    void saveDownloadDataPreference();

    void downloadImage(View view, String downloadURL, String downloadPath, DownloadManager downloadManager);

    ArrayList<DownloadData> getDownloadList();
}
