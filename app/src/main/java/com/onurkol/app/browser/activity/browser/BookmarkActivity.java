package com.onurkol.app.browser.activity.browser;

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
import com.onurkol.app.browser.interfaces.browser.bookmarks.BookmarkSettings;
import com.onurkol.app.browser.lib.AppPreferenceManager;
import com.onurkol.app.browser.lib.ContextManager;

public class BookmarkActivity extends AppCompatActivity implements BookmarkSettings {

    // Elements
    ImageButton backButton;
    TextView settingName;
    ListView bookmarkListView;
    LinearLayout noBookmarkLayout;
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
        setContentView(R.layout.activity_bookmark);
        // Get Classes
        dataManager=new BrowserDataManager();
        prefManager= AppPreferenceManager.getInstance();
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
        bookmarkListView=findViewById(R.id.bookmarkListView);
        noBookmarkLayout=findViewById(R.id.noBookmarkLayout);

        // Set Toolbar Title
        settingName.setText(getString(R.string.bookmarks_text));

        // Button Click Events
        backButton.setOnClickListener(view -> finish());

        // Check is Created (for Theme bug)
        if(!isCreated) {
            // Set Adapter


            if(BROWSER_BOOKMARK_LIST.size()<=0){
                // Show No Bookmark Layout
                noBookmarkLayout.setVisibility(View.VISIBLE);
                bookmarkListView.setVisibility(View.GONE);
            }
            else{
                // Hide No Bookmark Layout
                noBookmarkLayout.setVisibility(View.GONE);
                bookmarkListView.setVisibility(View.VISIBLE);
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