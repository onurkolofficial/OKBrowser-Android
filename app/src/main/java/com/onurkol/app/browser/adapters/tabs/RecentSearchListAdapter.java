package com.onurkol.app.browser.adapters.tabs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;

import com.onurkol.app.browser.R;
import com.onurkol.app.browser.controller.KeyboardController;
import com.onurkol.app.browser.controller.tabs.RecentSearchController;
import com.onurkol.app.browser.controller.tabs.TabController;
import com.onurkol.app.browser.data.tabs.RecentSearchData;
import com.onurkol.app.browser.libs.CharLimiter;

import java.util.ArrayList;

public class RecentSearchListAdapter extends ArrayAdapter<RecentSearchData> {
    private final LayoutInflater inflater;
    private final Context mContext;
    private final Fragment mFragment;
    private ViewHolder holder;
    private ArrayList<RecentSearchData> mRecentSearchData;

    TabController tabController;

    public RecentSearchListAdapter(@NonNull Context context, Fragment fragment, ArrayList<RecentSearchData> recentSearchData) {
        super(context, 0, recentSearchData);
        inflater=LayoutInflater.from(context);
        mContext=context;
        mFragment=fragment;
        mRecentSearchData=recentSearchData;
        tabController=TabController.getController(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView=inflater.inflate(R.layout.item_recent_search_data, null);
            holder=new ViewHolder();
            holder.deleteSearchButton=convertView.findViewById(R.id.deleteSearchButton);
            holder.openSearchLayoutButton=convertView.findViewById(R.id.openSearchLayoutButton);
            holder.recentSearchSentence=convertView.findViewById(R.id.recentSearchSentence);
            convertView.setTag(holder);
        }
        else{
            holder=(ViewHolder)convertView.getTag();
        }

        // Get Data
        RecentSearchData data=mRecentSearchData.get(position);

        holder.recentSearchSentence.setText(
                CharLimiter.Limit(data.getSearchSentence(),40));

        holder.deleteSearchButton.setOnClickListener(v -> {
            RecentSearchController.getController().deleteSearch(position);
            notifyDataSetChanged();
        });

        holder.openSearchLayoutButton.setOnClickListener(v -> {
            tabController.getCurrentTab().onStartWeb(data.getSearchSentence());
            KeyboardController.hideKeyboard(mContext, v);
        });

        return convertView;
    }

    private static class ViewHolder {
        ImageButton deleteSearchButton;
        LinearLayoutCompat openSearchLayoutButton;
        TextView recentSearchSentence;
    }
}
