package com.onurkol.app.browser.adapters.browser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.onurkol.app.browser.activity.MainActivity;
import com.onurkol.app.browser.data.browser.BookmarkData;
import com.onurkol.app.browser.interfaces.BrowserActionKeys;
import com.onurkol.app.browser.interfaces.browser.bookmarks.BookmarkSettings;
import com.onurkol.app.browser.lib.ContextManager;
import com.onurkol.app.browser.lib.browser.BookmarkManager;
import com.onurkol.app.browser.lib.browser.tabs.TabBuilder;
import com.onurkol.app.browser.tools.CharLimiter;

import java.util.ArrayList;

public class BookmarkListAdapter extends ArrayAdapter<BookmarkData> implements BrowserActionKeys, BookmarkSettings {
    private final LayoutInflater inflater;
    private ViewHolder holder;
    private final ArrayList<BookmarkData> getBookmarkList;
    private final ListView getListView;

    // Classes
    ContextManager contextManager;
    TabBuilder tabBuilder;
    BookmarkManager bookmarkManager;
    Context getContext;

    public BookmarkListAdapter(Context context, ListView historyListView, ArrayList<BookmarkData> bookmarkList) {
        super(context, 0, bookmarkList);
        getBookmarkList=bookmarkList;
        getContext=context;
        getListView=historyListView;
        inflater=LayoutInflater.from(context);
        contextManager=ContextManager.getManager();
        tabBuilder=TabBuilder.Build();
        bookmarkManager=BookmarkManager.getInstance();
    }

    @Override
    public int getCount() {
        return getBookmarkList.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_bookmark_data, null);
            holder = new ViewHolder();
            holder.bookmarkTitleText = convertView.findViewById(R.id.bookmarkTitleText);
            holder.bookmarkUrlText = convertView.findViewById(R.id.bookmarkUrlText);
            holder.deleteBookmarkButton = convertView.findViewById(R.id.deleteBookmarkButton);
            holder.openBookmarkLayoutButton = convertView.findViewById(R.id.openBookmarkLayoutButton);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        // Get Data
        BookmarkData data=getBookmarkList.get(position);

        String historyTitle=CharLimiter.Limit(data.getTitle(),34);
        String historyUrl=CharLimiter.Limit(data.getUrl(),38);
        // Set Texts
        holder.bookmarkTitleText.setText(historyTitle);
        holder.bookmarkUrlText.setText(historyUrl);

        // Button Click Events
        holder.openBookmarkLayoutButton.setOnClickListener(view -> {
            // Get Intent
            Intent mainActivityIntent;
            boolean isCreateNewActivity;

            // Check Context
            if(contextManager.getContextActivity().isTaskRoot()) {
                isCreateNewActivity=true;
                mainActivityIntent = new Intent(getContext, MainActivity.class);
            }
            else {
                isCreateNewActivity=false;
                if(contextManager.getContextActivity().getParentActivityIntent()!=null)
                    mainActivityIntent = contextManager.getContextActivity().getParentActivityIntent();
                else
                    mainActivityIntent = new Intent(getContext, MainActivity.class);
            }
            // Variables
            String action_name;
            // Create new Bundle
            Bundle bundle = new Bundle();
            // Check Action
            if(contextManager.getContextActivity().isTaskRoot()){
                action_name=KEY_ACTION_TAB_ON_CREATE;
            }
            else{
                if(tabBuilder.getTabFragmentList().size()<=0 && tabBuilder.getIncognitoTabFragmentList().size()<=0){
                    action_name=KEY_ACTION_TAB_ON_CREATE;
                }
                else{
                    if(tabBuilder.getActiveTabFragment()!=null){
                        action_name=KEY_ACTION_TAB_ON_START;
                    }
                    else{
                        action_name=KEY_ACTION_INCOGNITO_ON_START;
                    }
                }
            }
            bundle.putString(ACTION_NAME, action_name);
            bundle.putString(ACTION_VALUE, data.getUrl());
            // Set extras
            mainActivityIntent.putExtras(bundle);

            // Check Activity
            if(isCreateNewActivity)
                getContext.startActivity(mainActivityIntent);
            else
                MainActivity.updatedIntent=mainActivityIntent;
            // Close Current Activity
            ((Activity)getContext).finish();
        });

        holder.deleteBookmarkButton.setOnClickListener(view -> {
            // Remove Item (Current)
            BROWSER_BOOKMARK_LIST.remove(position);
            // Save Current List
            bookmarkManager.saveBookmarkListPreference(BROWSER_BOOKMARK_LIST);
            // Refresh List View
            getListView.invalidateViews();
        });

        return convertView;
    }

    //View Holder
    private static class ViewHolder {
        TextView bookmarkTitleText,bookmarkUrlText;
        ImageButton deleteBookmarkButton;
        LinearLayout openBookmarkLayoutButton;
    }
}
