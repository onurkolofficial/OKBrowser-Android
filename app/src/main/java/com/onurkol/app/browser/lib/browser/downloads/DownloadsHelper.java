package com.onurkol.app.browser.lib.browser.downloads;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import com.onurkol.app.browser.data.browser.DownloadsData;
import com.onurkol.app.browser.lib.ContextManager;

import static android.content.Context.DOWNLOAD_SERVICE;

public class DownloadsHelper {
    // Get Download Manager
    static DownloadManager downloadManager = (DownloadManager)ContextManager.getManager().getContext().getSystemService(DOWNLOAD_SERVICE);

    // Data Variable
    public static DownloadsData downloadsData;

    // Receivers
    public static BroadcastReceiver fileDownloadReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)){
                Bundle extras = intent.getExtras();
                DownloadManager.Query q = new DownloadManager.Query();
                q.setFilterById(extras.getLong(DownloadManager.EXTRA_DOWNLOAD_ID));
                Cursor c = downloadManager.query(q);

                if (c.moveToFirst()) {
                    int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
                    if (status == DownloadManager.STATUS_SUCCESSFUL) {
                        // process download
                        String downloadFileName=c.getString(c.getColumnIndex(DownloadManager.COLUMN_TITLE));
                        // get other required data by changing the constant passed to getColumnIndex
                        // Check Download File Name
                        if(!downloadsData.getFileName().equals(downloadFileName))
                            downloadsData.setFileName(downloadFileName);

                        // Add Download Data
                        DownloadsManager.getInstance().newDownload(downloadsData);
                    }
                }
            }
        }
    };
}
