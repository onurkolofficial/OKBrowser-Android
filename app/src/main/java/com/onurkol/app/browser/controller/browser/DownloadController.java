package com.onurkol.app.browser.controller.browser;

import android.app.DownloadManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.util.Base64;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.onurkol.app.browser.R;
import com.onurkol.app.browser.controller.PreferenceController;
import com.onurkol.app.browser.data.browser.DownloadData;
import com.onurkol.app.browser.data.browser.HistoryData;
import com.onurkol.app.browser.interfaces.BrowserDataInterface;
import com.onurkol.app.browser.interfaces.browser.DownloadControllerInterface;
import com.onurkol.app.browser.libs.DateManager;
import com.onurkol.app.browser.libs.ListToJson;
import com.onurkol.app.browser.libs.URLChecker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class DownloadController implements DownloadControllerInterface, BrowserDataInterface {
    private static WeakReference<DownloadController> instance=null;
    PreferenceController preferenceController;

    Gson gson=new Gson();

    private DownloadController(){
        preferenceController=PreferenceController.getController();
    }

    public static synchronized DownloadController getController(){
        if(instance==null || instance.get()==null)
            instance=new WeakReference<>(new DownloadController());
        return instance.get();
    }


    @Override
    public void newDownload(String downloadFileName, String downloadFolder) {
        // Get Date
        String downloadDate=DateManager.getDate();
        DownloadData data=new DownloadData(downloadFileName, downloadFolder, downloadDate);
        DOWNLOAD_LIST.add(0, data);
        saveDownloadDataPreference();
    }

    @Override
    public void deleteDownload(int position) {
        DOWNLOAD_LIST.remove(position);
        saveDownloadDataPreference();
    }

    @Override
    public void deleteAllDownloads() {
        DOWNLOAD_LIST.clear();
        saveDownloadDataPreference();
    }

    @Override
    public void syncDownloadData() {
        String downloadPreferenceList=preferenceController.getString(KEY_BROWSER_DOWNLOAD);
        if(downloadPreferenceList!=null && !downloadPreferenceList.equals(DOWNLOAD_NO_DATA)) {
            ArrayList<DownloadData> getDownloadPreferenceData = new ArrayList<>(
                    gson.fromJson(downloadPreferenceList, new TypeToken<ArrayList<DownloadData>>() {
                    }.getType()));
            DOWNLOAD_LIST.clear();
            DOWNLOAD_LIST.addAll(getDownloadPreferenceData);
        }
    }

    @Override
    public void saveDownloadDataPreference() {
        if(DOWNLOAD_LIST.size()<=0) {
            preferenceController.setPreference(KEY_BROWSER_DOWNLOAD, DOWNLOAD_NO_DATA);
        }
        else{
            String newData=ListToJson.getJson(DOWNLOAD_LIST);
            preferenceController.setPreference(KEY_BROWSER_DOWNLOAD, newData);
        }
    }

    @Override
    public void downloadImage(View view, String downloadURL, String downloadPath, DownloadManager downloadManager) {
        if(URLChecker.isDataImage(downloadURL)){
            // Save Image
            File path = new File(BROWSER_STORAGE_FOLDER+"/"+downloadPath);
            String filetype = downloadURL.substring(
                    downloadURL.indexOf("/")+1, downloadURL.indexOf(";"));
            String filename = System.currentTimeMillis() + "." + filetype;
            File file = new File(path, filename);
            try {
                if(!path.exists())
                    path.mkdirs();
                if(!file.exists())
                    file.createNewFile();

                String base64EncodedString = downloadURL.substring(downloadURL.indexOf(",") + 1);
                byte[] decodedBytes = Base64.decode(base64EncodedString, Base64.DEFAULT);
                OutputStream os = new FileOutputStream(file);
                os.write(decodedBytes);
                os.close();

                //Tell the media scanner about the new file so that it is immediately available to the user.
                MediaScannerConnection.scanFile(view.getContext(),
                        new String[]{file.toString()}, null,
                        (path1, uri) -> {});

                // Add Download Data (for download list)
                newDownload(filename, downloadPath);

                Snackbar.make(view, view.getContext().getString(R.string.download_completed_text), Snackbar.LENGTH_SHORT)
                        .setAction(view.getContext().getString(R.string.ok_text),v -> {})
                        .show();

            } catch (IOException e) {
                Snackbar.make(view, view.getContext().getString(R.string.download_failed_text), Snackbar.LENGTH_SHORT)
                        .setAction(view.getContext().getString(R.string.ok_text),v -> {})
                        .show();
            }
        }
        else{
            // Download Image
            Uri fileUri=Uri.parse(downloadURL);
            File file=new File(String.valueOf(fileUri));
            // Set Download Manager Request
            DownloadManager.Request request = new DownloadManager.Request(fileUri);
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

            // Get File Name
            String fileName=file.getName();
            // Set Folder
            request.setDestinationInExternalPublicDir(downloadPath, fileName);

            // Add Download Data (for download list)
            newDownload(fileName, downloadPath);

            // Enqueue
            downloadManager.enqueue(request);

            Snackbar.make(view, view.getContext().getString(R.string.downloading_image_text),
                    Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public ArrayList<DownloadData> getDownloadList() {
        return DOWNLOAD_LIST;
    }
}
