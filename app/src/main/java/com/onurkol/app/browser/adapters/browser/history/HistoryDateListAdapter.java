package com.onurkol.app.browser.adapters.browser.history;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.onurkol.app.browser.R;
import com.onurkol.app.browser.data.browser.history.HistoryData;
import com.onurkol.app.browser.data.browser.history.HistoryDate_Data;

import java.util.ArrayList;

public class HistoryDateListAdapter extends ArrayAdapter<HistoryDate_Data> {
    private Context getContext;
    private final LayoutInflater inflater;
    private ViewHolder holder;
    private ArrayList<HistoryDate_Data> getDateList;
    private ArrayList<HistoryData> getHistoryList;

    public HistoryDateListAdapter(Context context, ArrayList<HistoryDate_Data> dateList, ArrayList<HistoryData> historyList) {
        super(context, 0, dateList);
        getContext=context;
        getDateList=dateList;
        getHistoryList=historyList;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return getDateList.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_history_date_data, null);
            holder = new ViewHolder();
            holder.historyDateText = convertView.findViewById(R.id.historyDateText);
            holder.historyRecycleView = convertView.findViewById(R.id.historyDataView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Get Data
        String historyDate=getDateList.get(position).getDate();

        holder.historyDateText.setText(historyDate);

        // Set Recycler Layout Manager & Adapter
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext, LinearLayoutManager.VERTICAL, false);
        holder.historyRecycleView.setLayoutManager(layoutManager);

        // Create new List Data from MainList
        ArrayList<HistoryData> newAdapterData=new ArrayList<>();

        for(int in=0; in<getHistoryList.size(); in++){
            if(getHistoryList.get(in).getDate().equals(getDateList.get(position).getDate())){ // 123 is change date
                newAdapterData.add(getHistoryList.get(in));
            }
        }

        HistoryListAdapter hlAdapter=new HistoryListAdapter(getContext, newAdapterData, historyDate);
        holder.historyRecycleView.setAdapter(hlAdapter);

        return convertView;
    }

    //View Holder
    private static class ViewHolder {
        TextView historyDateText;
        RecyclerView historyRecycleView;
    }
}
