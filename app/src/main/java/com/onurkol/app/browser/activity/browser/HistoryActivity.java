package com.onurkol.app.browser.activity.browser;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.onurkol.app.browser.R;
import com.onurkol.app.browser.activity.installer.InstallerActivity;
import com.onurkol.app.browser.adapters.browser.HistoryListAdapter;
import com.onurkol.app.browser.controller.ContextController;
import com.onurkol.app.browser.controller.PreferenceController;
import com.onurkol.app.browser.controller.browser.BrowserDataInitController;
import com.onurkol.app.browser.controller.browser.DownloadController;
import com.onurkol.app.browser.controller.browser.HistoryController;
import com.onurkol.app.browser.controller.settings.DayNightModeController;
import com.onurkol.app.browser.controller.settings.LanguageController;
import com.onurkol.app.browser.interfaces.BrowserDataInterface;
import com.onurkol.app.browser.libs.ActivityActionAnimator;

public class HistoryActivity extends AppCompatActivity implements BrowserDataInterface {
    BrowserDataInitController browserDataController;
    PreferenceController preferenceController;
    DayNightModeController dayNightController;
    LanguageController languageController;

    HistoryController historyController;

    public static boolean isCreated;

    ImageButton backButton;
    TextView settingName;
    ListView historyListView;
    Button deleteAllHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ContextController.setContext(this);

        preferenceController=PreferenceController.getController();
        browserDataController=BrowserDataInitController.getController();
        browserDataController.init();

        historyController=HistoryController.getController();
        dayNightController=DayNightModeController.getController();
        languageController=LanguageController.getController();

        if(!browserDataController.isInstallerCompleted()){
            startActivity(new Intent(this, InstallerActivity.class));
            finish();
        }

        // Set Theme|Language
        dayNightController.setDayNightMode(this, preferenceController.getInt(KEY_DAY_NIGHT_MODE));
        languageController.setLanguage(this, preferenceController.getInt(KEY_LANGUAGE));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        backButton=findViewById(R.id.settingsBackButton);
        settingName=findViewById(R.id.settingsTitle);
        historyListView=findViewById(R.id.historyListView);
        deleteAllHistory=findViewById(R.id.deleteAllHistory);

        settingName.setText(getString(R.string.history_text));

        backButton.setOnClickListener(view -> ActivityActionAnimator.finish(this));
        deleteAllHistory.setOnClickListener(view -> {
            // Dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(HistoryActivity.this);
            builder.setMessage(getString(R.string.question_delete_all_histories_text))
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(getString(R.string.yes_text),(dialog, which) -> {
                        historyController.deleteAllHistory();
                        historyListView.invalidateViews();
                        updateView(this);
                    })
                    .setNegativeButton(getString(R.string.no_text), null)
                    .show();
        });

        // Set List Adapter
        historyListView.setAdapter(new HistoryListAdapter(this, historyListView, HISTORY_LIST));
        historyController.syncHistoryData();

        updateView(this);
        isCreated=true;
    }

    public static void updateView(Context context){
        if(HistoryController.getController().getHistoryList().size()>0)
            ((Activity)context).findViewById(R.id.noHistoryLayout).setVisibility(View.GONE);
        else
            ((Activity)context).findViewById(R.id.noHistoryLayout).setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        ActivityActionAnimator.finish(this);
    }

    @Override
    protected void onDestroy() {
        isCreated=false;
        super.onDestroy();
    }
}