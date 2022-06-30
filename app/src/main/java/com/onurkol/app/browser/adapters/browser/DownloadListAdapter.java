package com.onurkol.app.browser.adapters.browser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.onurkol.app.browser.R;
import com.onurkol.app.browser.activity.browser.DownloadsActivity;
import com.onurkol.app.browser.controller.browser.DownloadController;
import com.onurkol.app.browser.data.browser.DownloadData;
import com.onurkol.app.browser.interfaces.BrowserDataInterface;
import com.onurkol.app.browser.libs.CharLimiter;

import java.io.File;
import java.util.ArrayList;

public class DownloadListAdapter extends ArrayAdapter<DownloadData> implements BrowserDataInterface {
    private final LayoutInflater inflater;
    private final Context mContext;
    private ViewHolder holder;
    private final ArrayList<DownloadData> mDownloadData;

    DownloadController downloadController;

    ListView downloadListView;

    public DownloadListAdapter(@NonNull Context context, ListView listView, ArrayList<DownloadData> downloadData){
        super(context, 0, downloadData);
        inflater=LayoutInflater.from(context);
        mContext=context;
        downloadListView=listView;
        mDownloadData=downloadData;
        downloadController=DownloadController.getController();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_download_data, null);
            holder = new ViewHolder();
            holder.downloadFileNameText=convertView.findViewById(R.id.downloadFileNameText);
            holder.downloadFolderText=convertView.findViewById(R.id.downloadFolderText);
            holder.openFileLayoutButton=convertView.findViewById(R.id.openFileLayoutButton);
            holder.deleteDownloadFileButton=convertView.findViewById(R.id.deleteDownloadFileButton);
            convertView.setTag(holder);
        }
        else{
            holder=(ViewHolder)convertView.getTag();
        }
        // Get Data
        DownloadData data=mDownloadData.get(position);

        String downloadFile=CharLimiter.Limit(data.getFileName(),34);
        String downloadFolder="/"+CharLimiter.Limit(data.getFolder(),38);
        holder.downloadFileNameText.setText(downloadFile);
        holder.downloadFolderText.setText(downloadFolder);

        holder.openFileLayoutButton.setOnClickListener(v -> {
            // Get File
            String file=BROWSER_STORAGE_FOLDER+"/"+data.getFolder()+"/"+data.getFileName();
            Uri uri=Uri.parse(file);
            // Open File Intent
            Intent intent = new Intent(Intent.ACTION_VIEW);
            // Check File Type
            // Images
            if(file.endsWith(".png") ||
                    file.endsWith(".jpg") ||
                    file.endsWith(".jpeg") ||
                    file.endsWith(".svg") ||
                    file.endsWith(".gif") ||
                    file.endsWith(".bmp"))
                intent.setType("image/*");
            else if(file.endsWith(".txt") ||
                    file.endsWith(".xml"))
                intent.setType("text/*");
            else
                intent.setType("application/*");
            intent.putExtra(Intent.ACTION_VIEW,uri);

            mContext.startActivity(Intent.createChooser(intent,mContext.getString(R.string.open_file_text)));
        });

        holder.deleteDownloadFileButton.setOnClickListener(v -> {
            // Get File
            String file=BROWSER_STORAGE_FOLDER+"/"+data.getFolder()+"/"+data.getFileName();
            // Delete File
            File fileData = new File(file);
            if(fileData.exists())
                fileData.delete();
            // Delete Data
            downloadController.deleteDownload(position);
            // Refresh List View
            downloadListView.invalidateViews();
            DownloadsActivity.updateView(mContext);
        });

        return convertView;
    }

    private static class ViewHolder {
        TextView downloadFileNameText, downloadFolderText;
        LinearLayout openFileLayoutButton;
        ImageButton deleteDownloadFileButton;
    }
}
