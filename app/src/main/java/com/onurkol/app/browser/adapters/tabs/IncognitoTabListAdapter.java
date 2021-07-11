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
import com.onurkol.app.browser.data.tabs.IncognitoTabData;
import com.onurkol.app.browser.fragments.tabs.list.IncognitoTabListFragment;
import com.onurkol.app.browser.fragments.tabs.list.TabListFragment;
import com.onurkol.app.browser.lib.ContextManager;
import com.onurkol.app.browser.lib.tabs.TabBuilder;
import com.onurkol.app.browser.lib.tabs.core.ActivityTabSignal;
import com.onurkol.app.browser.tools.CharLimiter;
import com.onurkol.app.browser.tools.ScreenManager;

import java.util.ArrayList;

public class IncognitoTabListAdapter extends ArrayAdapter<IncognitoTabData> {
    private final LayoutInflater inflater;
    private ViewHolder holder;
    private static ArrayList<IncognitoTabData> tabDataList;
    private final GridView incognitoTabGridView;

    // Classes
    Context context;
    Activity contextActivity;
    TabBuilder tabBuilder;
    ActivityTabSignal tabSignal;

    // Other
    View fragmentView;

    public IncognitoTabListAdapter(Context context, GridView tabListView, ArrayList<IncognitoTabData> mTabData){
        super(context, 0, mTabData);
        tabDataList=mTabData;
        incognitoTabGridView=tabListView;
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
            convertView = inflater.inflate(R.layout.item_tab_list_incognito, null);
            holder = new ViewHolder();
            holder.incognitoTabUrlText = convertView.findViewById(R.id.incognitoTabUrlText);
            holder.incognitoTabCloseButton = convertView.findViewById(R.id.closeIncognitoTabButton);
            holder.incognitoTabPreviewImage = convertView.findViewById(R.id.incognitoTabPreviewImage);
            holder.incognitoTabOpenButton = convertView.findViewById(R.id.openTabButton);
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
        IncognitoTabData data=tabDataList.get(position);

        // Check Url
        if(data.getUrl().equals("")) {
            // Set Page Title
            holder.incognitoTabUrlText.setText(context.getString(R.string.incognito_tab_title));
            // Get View
            fragmentView=tabBuilder.getIncognitoTabFragmentList().get(position).getView();
            // Tab Preview
            holder.incognitoTabPreviewImage.setImageBitmap(ScreenManager.getScreenshot(fragmentView));
        }
        else {
            // Set Page Title
            holder.incognitoTabUrlText.setText(CharLimiter.Limit(data.getTitle(), 28));
            // Tab Preview
            if(data.getTabPreview()!=null)
                holder.incognitoTabPreviewImage.setImageBitmap(data.getTabPreview());
        }

        // Button Click Events
        // Open Select Tab Button
        holder.incognitoTabOpenButton.setOnClickListener(view -> {
            // Close This Activity
            contextActivity.finish();
            // Send Tab Signal
            TabListAdapter.sendTabSignal(position, ActivityTabSignal.INCOGNITO_ON_CHANGE, true);
        });

        // Close Tab Button
        holder.incognitoTabCloseButton.setOnClickListener(view -> {
            IncognitoTabListFragment.isChanged=true;
            if(((tabBuilder.getIncognitoTabDataList().size()-1)<=0)){
                IncognitoTabListFragment.changedIndexList.add(0); // Fixed Remove View
                // Clear Data List
                tabBuilder.getIncognitoTabDataList().clear();
                // Check Normal Tab Counts
                if(tabBuilder.getTabDataList().size()<=0)
                    // Close This Activity
                    contextActivity.finish();
                else
                    // Change Tab Pager (Show Normal Tabs)
                    ((ViewPager2)contextActivity.findViewById(R.id.tabListPager)).setCurrentItem(0);
            }
            else {
                IncognitoTabListFragment.changedIndexList.add(position);
                // Remove Tab Data
                tabBuilder.getIncognitoTabDataList().remove(position);
            }
            // Invalidate Views
            incognitoTabGridView.invalidateViews();
        });

        return convertView;
    }

    //View Holder
    private static class ViewHolder {
        TextView incognitoTabUrlText;
        ImageButton incognitoTabCloseButton;
        CardView incognitoTabOpenButton;
        ImageView incognitoTabPreviewImage;
    }
}
