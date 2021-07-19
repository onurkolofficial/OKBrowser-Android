package com.onurkol.app.browser.activity.browser;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.onurkol.app.browser.R;
import com.onurkol.app.browser.activity.MainActivity;
import com.onurkol.app.browser.activity.browser.installer.InstallerActivity;
import com.onurkol.app.browser.adapters.browser.HistoryListAdapter;
import com.onurkol.app.browser.data.BrowserDataManager;
import com.onurkol.app.browser.interfaces.browser.history.HistorySettings;
import com.onurkol.app.browser.lib.AppPreferenceManager;
import com.onurkol.app.browser.lib.ContextManager;
import com.onurkol.app.browser.lib.browser.HistoryManager;
import com.onurkol.app.browser.tools.ProcessDelay;

public class HistoryActivity extends AppCompatActivity implements HistorySettings {

    // Elements
    ImageButton backButton;
    TextView settingName;
    Button deleteAllHistory;
    ListView historyListView;
    LinearLayout noHistoryLayout;
    // Classes
    BrowserDataManager dataManager;
    AppPreferenceManager prefManager;
    // Intents
    Intent installerIntent;
    // Variables
    public static boolean isCreated=false,isCreatedView=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Set Current Activity Context
        ContextManager.Build(this);
        // Get Classes
        dataManager=new BrowserDataManager();
        prefManager=AppPreferenceManager.getInstance();
        // Create View
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        // Get Intents
        installerIntent=new Intent(this, InstallerActivity.class);

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
        noHistoryLayout=findViewById(R.id.noHistoryLayout);

        // Set Toolbar Title
        settingName.setText(getString(R.string.history_text));

        // Button Click Events
        backButton.setOnClickListener(view -> finish());
        deleteAllHistory.setOnClickListener(view -> {
            // Dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(HistoryActivity.this);
            builder.setMessage(getString(R.string.question_delete_all_histories_text))
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(getString(R.string.yes_text),(dialog, which) -> {
                        // Clear Arrays
                        BROWSER_HISTORY_LIST.clear();
                        // Save Preferences
                        HistoryManager.getInstance().saveHistoryListPreference(BROWSER_HISTORY_LIST);
                        // Remove Views
                        historyListView.invalidateViews();
                        // Show No History Layout
                        noHistoryLayout.setVisibility(View.VISIBLE);
                        historyListView.setVisibility(View.GONE);
                    })
                    .setNegativeButton(getString(R.string.no_text), null)
                    .show();
        });

        // Check is Created (for Theme bug)
        if(!isCreated) {
            // Set Adapter
            historyListView.setAdapter(new HistoryListAdapter(this, historyListView, BROWSER_HISTORY_LIST));

            // Get Saved Data
            HistoryManager.getInstance().syncSavedHistoryData();

            if(BROWSER_HISTORY_LIST.size()<=0){
                // Show No History Layout
                noHistoryLayout.setVisibility(View.VISIBLE);
                historyListView.setVisibility(View.GONE);
            }
            else{
                // Hide No History Layout
                noHistoryLayout.setVisibility(View.GONE);
                historyListView.setVisibility(View.VISIBLE);
            }
        }
        isCreated = true;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        // Init Browser Data ( Applying View Settings )
        if(!dataManager.startInstallerActivity){
            dataManager.initBrowserPreferenceSettings();
            /*
            if(!isCreatedView) {
                //...
            }
             */
            isCreatedView=true;
        }
        return super.onCreateView(name, context, attrs);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isCreated=false;
    }
}