package com.onurkol.app.browser.adapters.browser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.onurkol.app.browser.R;
import com.onurkol.app.browser.activity.MainActivity;
import com.onurkol.app.browser.data.browser.HistoryData;
import com.onurkol.app.browser.interfaces.BrowserActionKeys;
import com.onurkol.app.browser.interfaces.browser.history.HistorySettings;
import com.onurkol.app.browser.lib.ContextManager;
import com.onurkol.app.browser.lib.browser.HistoryManager;
import com.onurkol.app.browser.lib.browser.tabs.TabBuilder;
import com.onurkol.app.browser.lib.browser.tabs.core.ActivityTabSignal;
import com.onurkol.app.browser.tools.CharLimiter;
import com.onurkol.app.browser.tools.DateManager;

import java.util.List;

public class HistoryListAdapter extends ArrayAdapter<HistoryData> implements BrowserActionKeys, HistorySettings {
    private final LayoutInflater inflater;
    private ViewHolder holder;
    private final List<HistoryData> getHistoryList;
    private final ListView getListView;

    // Classes
    ContextManager contextManager;
    TabBuilder tabBuilder;
    HistoryManager historyManager;
    Context getContext;

    public HistoryListAdapter(Context context, ListView historyListView, List<HistoryData> historyList) {
        super(context, 0, historyList);
        getHistoryList=historyList;
        getContext=context;
        getListView=historyListView;
        inflater=LayoutInflater.from(context);
        contextManager=ContextManager.getManager();
        tabBuilder=TabBuilder.Build();
        historyManager=HistoryManager.getInstance();
    }

    @Override
    public int getCount() {
        return getHistoryList.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_history_data, null);
            holder = new ViewHolder();
            holder.historyDateText = convertView.findViewById(R.id.historyDateText);
            holder.historyTitleText = convertView.findViewById(R.id.historyTitleText);
            holder.historyUrlText = convertView.findViewById(R.id.historyUrlText);
            holder.deleteHistoryButton = convertView.findViewById(R.id.deleteHistoryButton);
            holder.openHistoryLayoutButton = convertView.findViewById(R.id.openHistoryLayoutButton);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Get Data
        HistoryData data=getHistoryList.get(position);
        // Check Today-Yesterday-Days Ago
        String dateText;
        String dataDate = data.getDate(),
                currentDate = DateManager.getDate();
        if (currentDate.equals(dataDate))
            dateText = getContext.getString(R.string.today_text);
        else {
            // Check Yesterday
            int currentDateDays=Integer.parseInt(currentDate.split("/")[0].trim());
            int dataDateDays=Integer.parseInt(dataDate.split("/")[0].trim());
            int daysAgo=currentDateDays-dataDateDays;
            if(daysAgo<0)
                daysAgo*=-1;

            if(daysAgo==1)
                dateText = getContext.getString(R.string.yesterday_text);
            else
                dateText = daysAgo+" "+getContext.getString(R.string.days_ago_text);
        }
        holder.historyDateText.setText(dateText);

        String historyTitle=CharLimiter.Limit(data.getTitle(),30);
        String historyUrl=CharLimiter.Limit(data.getUrl(),34);
        // Set Texts
        holder.historyTitleText.setText(historyTitle);
        holder.historyUrlText.setText(historyUrl);

        // Button Click Events
        holder.openHistoryLayoutButton.setOnClickListener(view -> {
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
                ((Activity)getContext).startActivity(mainActivityIntent);
            else
                MainActivity.updatedIntent=mainActivityIntent;
            // Close Current Activity
            ((Activity)getContext).finish();
        });

        holder.deleteHistoryButton.setOnClickListener(view -> {
            // Remove Item (Current)
            BROWSER_HISTORY_LIST.remove(position);
            // Save Current List
            historyManager.saveHistoryListPreference(BROWSER_HISTORY_LIST);
            // Refresh List View
            getListView.invalidateViews();
        });

        return convertView;
    }

    //View Holder
    private static class ViewHolder {
        TextView historyDateText,historyTitleText,historyUrlText;
        ImageButton deleteHistoryButton;
        LinearLayout openHistoryLayoutButton;
    }
}
