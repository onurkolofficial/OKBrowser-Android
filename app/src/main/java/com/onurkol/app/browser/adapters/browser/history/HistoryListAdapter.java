package com.onurkol.app.browser.adapters.browser.history;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.onurkol.app.browser.R;
import com.onurkol.app.browser.data.browser.history.HistoryData;

import java.util.ArrayList;
import java.util.List;

public class HistoryListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context getContext;
    private final LayoutInflater inflater;
    private List<HistoryData> getHistoryList;
    private String getCheckDate;

    public HistoryListAdapter(Context context, List<HistoryData> historyList, String checkDate) {
        getHistoryList=historyList;
        getContext=context;
        getCheckDate=checkDate;
        inflater=LayoutInflater.from(context);
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        private TextView historyTitleText,historyUrlText;
        private ImageButton deleteHistoryButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            historyTitleText=itemView.findViewById(R.id.historyTitleText);
            historyUrlText=itemView.findViewById(R.id.historyUrlText);
            deleteHistoryButton=itemView.findViewById(R.id.deleteHistoryButton);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=inflater.inflate(R.layout.item_history_data,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ViewHolder holder=(ViewHolder) viewHolder;

        holder.historyTitleText.setText(getHistoryList.get(i).getTitle());
        holder.historyUrlText.setText(getHistoryList.get(i).getUrl());

    }

    @Override
    public int getItemCount() {
        return getHistoryList.size();
    }
}
