package com.onurkol.app.browser.activity.browser.core;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.onurkol.app.browser.R;
import com.onurkol.app.browser.activity.browser.installer.InstallerActivity;
import com.onurkol.app.browser.data.BrowserDataManager;
import com.onurkol.app.browser.interfaces.browser.downloads.DownloadSettings;
import com.onurkol.app.browser.lib.AppPreferenceManager;
import com.onurkol.app.browser.lib.ContextManager;

public class DownloadsActivity extends AppCompatActivity implements DownloadSettings {

    // Elements
    ImageButton backButton;
    TextView settingName;
    ListView downloadsListView;
    LinearLayout noDownloadLayout;
    // Classes
    BrowserDataManager dataManager;
    AppPreferenceManager prefManager;
    // Intents
    Intent installerIntent;
    // Variables
    public static boolean isCreated=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Set Current Activity Context
        ContextManager.Build(this);
        // Create View
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloads);
        // Get Classes
        dataManager=new BrowserDataManager();
        prefManager=AppPreferenceManager.getInstance();
        // Get Intents
        installerIntent=new Intent(this, InstallerActivity.class);
        // Check Get Shortcut
        if(isTaskRoot())
            dataManager.initBrowserPreferenceSettings();

        // Check Installer Activity
        if(dataManager.startInstallerActivity){
            // Start Welcome Activity
            startActivity(installerIntent);
            // Finish Current Activity
            finish();
        }
        // Get Elements
        backButton=findViewById(R.id.backSettingsButton);
        settingName=findViewById(R.id.settingName);
        downloadsListView=findViewById(R.id.downloadsListView);
        noDownloadLayout=findViewById(R.id.noDownloadLayout);

        // Set Toolbar Title
        settingName.setText(getString(R.string.downloads_text));

        // Button Click Events
        backButton.setOnClickListener(view -> finish());

        // Check is Created (for Theme bug)
        if(!isCreated) {
            // Set Adapter


            if(BROWSER_DOWNLOAD_LIST.size()<=0){
                // Show No Downloads Layout
                noDownloadLayout.setVisibility(View.VISIBLE);
                downloadsListView.setVisibility(View.GONE);
            }
            else{
                // Hide No Downloads Layout
                noDownloadLayout.setVisibility(View.GONE);
                downloadsListView.setVisibility(View.VISIBLE);
            }
        }

        isCreated = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isCreated=false;
    }
}