package com.onurkol.app.browser.adapters.browser;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
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
import com.onurkol.app.browser.data.browser.DownloadsData;
import com.onurkol.app.browser.interfaces.browser.downloads.DownloadsSettings;
import com.onurkol.app.browser.lib.ContextManager;
import com.onurkol.app.browser.lib.browser.downloads.DownloadsManager;
import com.onurkol.app.browser.lib.browser.tabs.TabBuilder;
import com.onurkol.app.browser.tools.CharLimiter;

import java.io.File;
import java.util.ArrayList;

public class DownloadListAdapter extends ArrayAdapter<DownloadsData> implements DownloadsSettings {
    private final LayoutInflater inflater;
    private ViewHolder holder;
    private final ArrayList<DownloadsData> getDownloadsList;
    private final ListView getListView;

    // Classes
    ContextManager contextManager;
    TabBuilder tabBuilder;
    DownloadsManager downloadsManager;
    Context getContext;

    public DownloadListAdapter(Context context, ListView downloadsListView, ArrayList<DownloadsData> downloadsList) {
        super(context, 0, downloadsList);
        getDownloadsList=downloadsList;
        getContext=context;
        getListView=downloadsListView;
        inflater=LayoutInflater.from(context);
        contextManager=ContextManager.getManager();
        tabBuilder=TabBuilder.Build();
        downloadsManager=DownloadsManager.getInstance();
    }

    @Override
    public int getCount() {
        return getDownloadsList.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_download_data, null);
            holder = new ViewHolder();
            holder.downloadFileNameText = convertView.findViewById(R.id.downloadFileNameText);
            holder.downloadFolderText = convertView.findViewById(R.id.downloadFolderText);
            holder.deleteDownloadFileButton = convertView.findViewById(R.id.deleteDownloadFileButton);
            holder.openFileLayoutButton = convertView.findViewById(R.id.openFileLayoutButton);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Get Data
        DownloadsData data=getDownloadsList.get(position);

        String downloadFile=CharLimiter.Limit(data.getFileName(),34);
        String downloadFolder=CharLimiter.Limit(data.getFolder(),38);
        // Set Texts
        holder.downloadFileNameText.setText(downloadFile);
        holder.downloadFolderText.setText(downloadFolder);

        // Button Click Events
        holder.openFileLayoutButton.setOnClickListener(view -> {
            // Open File
            String file=Environment.getExternalStorageDirectory().getPath()+"/"+data.getFolder()+"/"+data.getFileName();
            // Check File
            Uri uri=Uri.parse(file);
            // Set Intent
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

            getContext.startActivity(Intent.createChooser(intent,getContext.getString(R.string.open_file_text)));
        });

        holder.deleteDownloadFileButton.setOnClickListener(view -> {
            // Get Delete File
            String file=Environment.getExternalStorageDirectory().getPath()+"/"+data.getFolder()+"/"+data.getFileName();
            // Deleting File
            File fileData = new File(file);
            fileData.delete();
            // Delete Preference Data
            // Remove Item (Current)
            BROWSER_DOWNLOAD_LIST.remove(position);
            // Save Current List
            downloadsManager.saveDownloadsListPreference(BROWSER_DOWNLOAD_LIST);
            // Refresh List View
            getListView.invalidateViews();
        });

        return convertView;
    }

    //View Holder
    private static class ViewHolder {
        TextView downloadFileNameText,downloadFolderText;
        ImageButton deleteDownloadFileButton;
        LinearLayout openFileLayoutButton;
    }
}
