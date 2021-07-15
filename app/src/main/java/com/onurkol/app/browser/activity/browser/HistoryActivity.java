package com.onurkol.app.browser.activity.browser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.onurkol.app.browser.R;
import com.onurkol.app.browser.activity.browser.installer.InstallerActivity;
import com.onurkol.app.browser.adapters.browser.history.HistoryDateListAdapter;
import com.onurkol.app.browser.data.BrowserDataManager;
import com.onurkol.app.browser.data.browser.history.HistoryData;
import com.onurkol.app.browser.data.browser.history.HistoryDate_Data;
import com.onurkol.app.browser.interfaces.browser.HistorySettings;
import com.onurkol.app.browser.lib.AppPreferenceManager;
import com.onurkol.app.browser.lib.ContextManager;

public class HistoryActivity extends AppCompatActivity implements HistorySettings {

    // Elements
    ImageButton backButton;
    TextView settingName;
    Button deleteAllHistory;
    ListView historyListView;
    // Classes
    BrowserDataManager dataManager;
    AppPreferenceManager prefManager;
    // Intents
    Intent installerIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Set Current Activity Context
        ContextManager.Build(this);
        // Create View
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
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
        deleteAllHistory=findViewById(R.id.deleteAllHistory);
        historyListView=findViewById(R.id.historyListRecyclerView);

        // Set Toolbar Title
        settingName.setText(getString(R.string.history_text));

        // Button Click Events
        backButton.setOnClickListener(view -> finish());

        // Set Adapter
        historyListView.setNestedScrollingEnabled(false);
        ArrayAdapter<HistoryDate_Data> hladapt=new HistoryDateListAdapter(this, BROWSER_HISTORY_DATE_LIST, BROWSER_HISTORY_LIST);
        historyListView.setAdapter(hladapt);

        // Test Data
        // Dates
        BROWSER_HISTORY_DATE_LIST.add(new HistoryDate_Data("123"));
        BROWSER_HISTORY_DATE_LIST.add(new HistoryDate_Data("125"));
        BROWSER_HISTORY_DATE_LIST.add(new HistoryDate_Data("127"));

        // Data
        BROWSER_HISTORY_LIST.add(new HistoryData("title","url","123"));
        BROWSER_HISTORY_LIST.add(new HistoryData("title1","url1","123"));
        BROWSER_HISTORY_LIST.add(new HistoryData("title2","url1","123"));
        BROWSER_HISTORY_LIST.add(new HistoryData("title2","url1","123"));
        BROWSER_HISTORY_LIST.add(new HistoryData("title3","url2","125"));
        BROWSER_HISTORY_LIST.add(new HistoryData("title3","url2","125"));
        BROWSER_HISTORY_LIST.add(new HistoryData("title4","url2","125"));
        BROWSER_HISTORY_LIST.add(new HistoryData("title5","url2","127"));
        BROWSER_HISTORY_LIST.add(new HistoryData("title5","url2","127"));
        BROWSER_HISTORY_LIST.add(new HistoryData("title5","url2","127"));
        BROWSER_HISTORY_LIST.add(new HistoryData("title5","url2","127"));
        BROWSER_HISTORY_LIST.add(new HistoryData("title5","url2","127"));

    }
}