package com.onurkol.app.browser.activity.browser;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.onurkol.app.browser.R;
import com.onurkol.app.browser.activity.installer.InstallerActivity;
import com.onurkol.app.browser.adapters.browser.BookmarkListAdapter;
import com.onurkol.app.browser.controller.ContextController;
import com.onurkol.app.browser.controller.PreferenceController;
import com.onurkol.app.browser.controller.browser.BookmarkController;
import com.onurkol.app.browser.controller.browser.BrowserDataInitController;
import com.onurkol.app.browser.controller.browser.HistoryController;
import com.onurkol.app.browser.controller.settings.DayNightModeController;
import com.onurkol.app.browser.controller.settings.LanguageController;
import com.onurkol.app.browser.interfaces.BrowserDataInterface;
import com.onurkol.app.browser.libs.ActivityActionAnimator;

public class BookmarkActivity extends AppCompatActivity implements BrowserDataInterface {
    BrowserDataInitController browserDataController;
    PreferenceController preferenceController;
    DayNightModeController dayNightController;
    LanguageController languageController;

    BookmarkController bookmarkController;

    public static boolean isCreated;

    ImageButton backButton;
    TextView settingName;
    ListView bookmarkListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ContextController.setContext(this);

        preferenceController=PreferenceController.getController();
        browserDataController=BrowserDataInitController.getController();
        browserDataController.init();

        bookmarkController=BookmarkController.getController();
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
        setContentView(R.layout.activity_bookmark);

        backButton=findViewById(R.id.settingsBackButton);
        settingName=findViewById(R.id.settingsTitle);
        bookmarkListView=findViewById(R.id.bookmarkListView);

        settingName.setText(getString(R.string.bookmarks_text));

        backButton.setOnClickListener(view -> ActivityActionAnimator.finish(this));

        // Set List Adapter
        bookmarkListView.setAdapter(new BookmarkListAdapter(this, bookmarkListView, BOOKMARK_LIST));
        bookmarkController.syncBookmarkData();

        updateView(this);
        isCreated=true;
    }

    public static void updateView(Context context){
        if(BookmarkController.getController().getBookmarkList().size()>0)
            ((Activity)context).findViewById(R.id.noBookmarkLayout).setVisibility(View.GONE);
        else
            ((Activity)context).findViewById(R.id.noBookmarkLayout).setVisibility(View.VISIBLE);
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