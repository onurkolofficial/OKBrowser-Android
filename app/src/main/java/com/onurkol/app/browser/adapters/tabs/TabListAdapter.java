package com.onurkol.app.browser.adapters.tabs;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.viewpager2.widget.ViewPager2;

import com.onurkol.app.browser.R;
import com.onurkol.app.browser.activity.MainActivity;
import com.onurkol.app.browser.data.tabs.ClassesTabData;
import com.onurkol.app.browser.data.tabs.TabData;
import com.onurkol.app.browser.fragments.tabs.list.TabListFragment;
import com.onurkol.app.browser.lib.ContextManager;
import com.onurkol.app.browser.tools.ScreenManager;
import com.onurkol.app.browser.lib.tabs.TabBuilder;
import com.onurkol.app.browser.lib.tabs.core.ActivityTabSignal;
import com.onurkol.app.browser.tools.CharLimiter;

import java.util.ArrayList;

public class TabListAdapter extends ArrayAdapter<TabData> {
    private final LayoutInflater inflater;
    private ViewHolder holder;
    private static ArrayList<TabData> tabDataList;
    private static ArrayList<ClassesTabData> classesTabDataList;
    private final GridView tabGridView;

    // Classes
    Context context;
    Activity contextActivity;
    TabBuilder tabBuilder;
    ActivityTabSignal tabSignal;

    // Other
    View fragmentView;

    public TabListAdapter(Context context, GridView tabListView, ArrayList<TabData> mTabData, ArrayList<ClassesTabData> cTabData){
        super(context, 0, mTabData);
        tabDataList=mTabData;
        classesTabDataList=cTabData;
        tabGridView=tabListView;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return tabDataList.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_tab_data, null);
            holder = new ViewHolder();
            holder.tabUrlText = convertView.findViewById(R.id.tabUrlText);
            holder.tabCloseButton = convertView.findViewById(R.id.closeTabButton);
            holder.tabOpenButton = convertView.findViewById(R.id.openTabButton);
            holder.tabPreviewImage = convertView.findViewById(R.id.tabPreviewImage);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Get Classes
        context = ContextManager.getManager().getContext();
        contextActivity = ContextManager.getManager().getContextActivity();
        tabBuilder = TabBuilder.Build();
        tabSignal = new ActivityTabSignal();

        // Get Tab Data
        TabData data=tabDataList.get(position);
        ClassesTabData cData=classesTabDataList.get(position);

        // Check Url
        if(data.getUrl()==null || data.getUrl().equals("")) {
            // Set Page Title
            holder.tabUrlText.setText(context.getString(R.string.new_tab_text));
            // Get View
            fragmentView=cData.getTabFragment().getView();
            // Tab Preview
            holder.tabPreviewImage.setImageBitmap(ScreenManager.getScreenshot(fragmentView));
        }
        else {
            // Set Page Title
            holder.tabUrlText.setText(CharLimiter.Limit(data.getTitle(), 27));
            // Tab Preview
            if(cData.getTabPreview()!=null)
                holder.tabPreviewImage.setImageBitmap(ScreenManager.getScreenshot(cData.getTabFragment().getView()));
        }

        // Button Click Events
        // Open Select Tab Button
        holder.tabOpenButton.setOnClickListener(view -> {
            // Close This Activity
            contextActivity.finish();
            // Send Tab Signal
            sendTabSignal(position, ActivityTabSignal.TAB_ON_CHANGE, false);
        });

        // Close Tab Button
        holder.tabCloseButton.setOnClickListener(view -> {
            TabListFragment.isChanged=true;
            // Check Tab Count
            if((tabBuilder.getTabDataList().size()-1)<=0){
                TabListFragment.changedIndexList.add(0);
                // Clear Data List
                tabBuilder.getTabDataList().clear();
                tabBuilder.getClassesTabDataList().clear();
                // Check Incognito Tab Counts
                if(tabBuilder.getIncognitoTabDataList().size()<=0) {
                    tabBuilder.getTabFragmentList().clear();
                    // Close This Activity
                    contextActivity.finish();
                }
                else
                    // Change Tab Pager (Show Incognito Tabs)
                    ((ViewPager2)contextActivity.findViewById(R.id.tabListPager)).setCurrentItem(1);
            }
            else{
                TabListFragment.changedIndexList.add(position);
                // Remove Tab Data
                tabBuilder.getTabDataList().remove(position);
                tabBuilder.getClassesTabDataList().remove(position);
            }
            // Invalidate Views
            tabGridView.invalidateViews();
        });

        return convertView;
    }

    // Tab Signals for Listeners ( Normal Tabs )
    public void sendTabSignal(int tabPosition, int signalCode, boolean incognito){
        // Send Activity Status
        // Create Tab Signal
        ActivityTabSignal tabSignal=new ActivityTabSignal();
        ActivityTabSignal.TabSignalData signalData=new ActivityTabSignal.TabSignalData();
        // Set Status
        tabSignal.setSignalStatus(signalCode);
        // Set Incognito Mode
        tabSignal.setTabIsIncognito(incognito);
        // Set Data
        signalData.tab_position=tabPosition;
        signalData.tab_url=tabDataList.get(tabPosition).getUrl();
        // Send Data
        tabSignal.setSignalData(signalData);
        // Send Signal
        MainActivity.sendTabSignal(tabSignal);
    }

    //View Holder
    private static class ViewHolder {
        TextView tabUrlText;
        ImageButton tabCloseButton;
        CardView tabOpenButton;
        ImageView tabPreviewImage;
    }
}
