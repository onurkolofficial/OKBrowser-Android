package com.onurkol.app.browser.webview.listeners;

import static android.content.Context.DOWNLOAD_SERVICE;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.onurkol.app.browser.R;
import com.onurkol.app.browser.controller.ContextController;
import com.onurkol.app.browser.controller.PermissionController;
import com.onurkol.app.browser.controller.browser.DownloadController;
import com.onurkol.app.browser.interfaces.BrowserDataInterface;

import java.io.File;

public class DownloadListener implements android.webkit.DownloadListener, BrowserDataInterface {
    Context mContext;

    public DownloadListener(Context context){
        mContext=context;
    }

    @Override
    public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
        PermissionController permissionController=PermissionController.getController();

        View snackbarRoot=((Activity)mContext).findViewById(R.id.browserCoordinatorLayout);

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q){
            // API 29 and above
            downloadHandler(mContext, url, snackbarRoot);
        }
        else{
            // API 28 and below
            if(permissionController.getStoragePermission(mContext))
                downloadHandler(mContext, url, snackbarRoot);
            else
                Snackbar.make(snackbarRoot, mContext.getString(R.string.require_file_permission_text),
                        Snackbar.LENGTH_SHORT)
                        .setAction(mContext.getString(R.string.ok_text),v -> {})
                        .show();
        }
    }

    private void downloadHandler(Context context, String url, View view){
        DownloadController downloadController=DownloadController.getController();
        DownloadManager downloadManager=(DownloadManager)context.getSystemService(DOWNLOAD_SERVICE);
        // Get File Info
        Uri fileUri=Uri.parse(url);
        File file=new File(String.valueOf(fileUri));
        // Set Download Manager Request
        DownloadManager.Request request = new DownloadManager.Request(fileUri);
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        // Get File Name
        String fileName=file.getName();

        String dataDownloadFolder="";
        // Set Folder
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.Q)
            dataDownloadFolder=BROWSER_DOWNLOAD_FOLDER_V30;
        else
            dataDownloadFolder=BROWSER_DOWNLOAD_FOLDER;

        request.setDestinationInExternalPublicDir(dataDownloadFolder, fileName);

        // Add Download Data (for download list)
        downloadController.newDownload(fileName, dataDownloadFolder);

        // Enqueue
        downloadManager.enqueue(request);

        Snackbar.make(view, context.getString(R.string.downloading_file_text),
                Snackbar.LENGTH_SHORT).show();
    }
}
