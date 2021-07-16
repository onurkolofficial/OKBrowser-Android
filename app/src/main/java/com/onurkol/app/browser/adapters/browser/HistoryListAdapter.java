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
import com.onurkol.app.browser.lib.ContextManager;
import com.onurkol.app.browser.lib.browser.tabs.TabBuilder;
import com.onurkol.app.browser.lib.browser.tabs.core.ActivityTabSignal;
import com.onurkol.app.browser.tools.CharLimiter;
import com.onurkol.app.browser.tools.DateManager;

import java.util.List;

public class HistoryListAdapter extends ArrayAdapter<HistoryData> implements BrowserActionKeys {
    private final LayoutInflater inflater;
    private ViewHolder holder;
    private final List<HistoryData> getHistoryList;
    private final ListView getListView;

    // Classes
    ContextManager contextManager;
    TabBuilder tabBuilder;
    Context getContext;

    // Variables
    boolean isNewDate;

    public HistoryListAdapter(Context context, ListView historyListView, List<HistoryData> historyList) {
        super(context, 0, historyList);
        getHistoryList=historyList;
        getContext=context;
        getListView=historyListView;
        inflater=LayoutInflater.from(context);
        contextManager=ContextManager.getManager();
        tabBuilder=TabBuilder.Build();
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

        // Check Show Date
        if(position==0 || isNewDate) {
            Log.e("HitoryListAdapter","84: Data: "+data.getDate()+" Update: "+isNewDate);
            // Check Today-Yesterday-Days Ago
            String dateText;
            String dataDate=data.getDate(),
                    currentDate=DateManager.getDate();
            if(currentDate.equals(dataDate))
                dateText=getContext.getString(R.string.today_text)+" - "+dataDate;
            else {
                dateText=dataDate;
                /*
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

                 */
            }
            holder.historyDateText.setText(dateText);
            holder.historyDateText.setVisibility(View.VISIBLE);
        }
        else
            holder.historyDateText.setVisibility(View.GONE);

        String historyTitle=CharLimiter.Limit(data.getTitle(),36);
        String historyUrl=CharLimiter.Limit(data.getUrl(),36);
        // Set Texts
        holder.historyTitleText.setText(historyTitle);
        holder.historyUrlText.setText(historyUrl);

        // Button Click Events
        holder.openHistoryLayoutButton.setOnClickListener(view -> {
            // Get Intent
            Intent mainActivityIntent;
            boolean isCreateNewTab;

            Log.e("HistoryListAdapter","108: Check Values = "+tabBuilder.getTabFragmentList().size()
                    + " x " + tabBuilder.getIncognitoTabFragmentList().size()
                    + " x " + contextManager.getContextActivity().isTaskRoot());

            /*
            // Check is New Tab
            if(tabBuilder.getTabFragmentList().size()<=0 || tabBuilder.getIncognitoTabFragmentList().size()<=0)
                isCreateNewTab=true;
            else
                isCreateNewTab=false;
            // Check Context
            if(contextManager.getContextActivity().getParent()!=null)
                mainActivityIntent=contextManager.getContextActivity().getParent().getIntent();
            else
                mainActivityIntent=new Intent(getContext, MainActivity.class);

            // Check Incognito Web
            boolean isIncognito;
            if(tabBuilder.getActiveTabFragment()!=null)
                isIncognito=false;
            else
                isIncognito=tabBuilder.getActiveIncognitoFragment()!=null;

            // Create new Bundle
            Bundle bundle = new Bundle();
            // Set bundle Data
            if(isCreateNewTab)
                bundle.putString(ACTION_NAME, KEY_ACTION_TAB_ON_CREATE);
            else
                if (isIncognito)
                    bundle.putString(ACTION_NAME, KEY_ACTION_INCOGNITO_ON_START);
                else
                    bundle.putString(ACTION_NAME, KEY_ACTION_TAB_ON_START);
            bundle.putString(ACTION_VALUE, data.getUrl());
            // Set extras
            mainActivityIntent.putExtras(bundle);

            if(isCreateNewTab)
                ((Activity)getContext).startActivity(mainActivityIntent);
            else
                MainActivity.updatedIntent=mainActivityIntent;
            // Close Current Activity
            ((Activity)getContext).finish();
             */
        });

        // Check isNewDate
        if(position!=(getHistoryList.size()-1)){
            isNewDate=!getHistoryList.get(position+1).getDate().equals(getHistoryList.get(position).getDate());
        }

        return convertView;
    }

    // Tab Signal
    public void sendTabSignal(int position, int signalCode, boolean incognito){
        // Send Activity Status
        // Create Tab Signal
        ActivityTabSignal tabSignal=new ActivityTabSignal();
        ActivityTabSignal.TabSignalData signalData=new ActivityTabSignal.TabSignalData();
        // Set Status
        tabSignal.setSignalStatus(signalCode);
        // Set Incognito Mode
        tabSignal.setTabIsIncognito(incognito);
        // Set Data
        signalData.tab_url=getHistoryList.get(position).getUrl();
        // Send Data
        tabSignal.setSignalData(signalData);
        // Send Signal
        MainActivity.sendTabSignal(tabSignal);
    }

    //View Holder
    private static class ViewHolder {
        TextView historyDateText,historyTitleText,historyUrlText;
        ImageButton deleteHistoryButton;
        LinearLayout openHistoryLayoutButton;
    }
}
