package com.onurkol.app.browser.adapters.browser;

import static com.onurkol.app.browser.libs.ActivityActionAnimator.finishAndStartActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.onurkol.app.browser.activity.browser.HistoryActivity;
import com.onurkol.app.browser.controller.ContextController;
import com.onurkol.app.browser.controller.browser.HistoryController;
import com.onurkol.app.browser.data.browser.HistoryData;
import com.onurkol.app.browser.libs.ActivityActionAnimator;
import com.onurkol.app.browser.libs.CharLimiter;
import com.onurkol.app.browser.libs.DateManager;

import java.util.ArrayList;

public class HistoryListAdapter extends ArrayAdapter<HistoryData> {
    private final LayoutInflater inflater;
    private final Context mContext;
    private ViewHolder holder;
    private final ArrayList<HistoryData> mHistoryData;

    HistoryController historyController;

    ListView historyListView;

    public HistoryListAdapter(@NonNull Context context, ListView listView, ArrayList<HistoryData> historyData){
        super(context, 0, historyData);
        inflater=LayoutInflater.from(context);
        mContext=context;
        mHistoryData=historyData;
        historyListView=listView;
        historyController=HistoryController.getController();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_history_data, null);
            holder = new ViewHolder();
            holder.historyTitleText=convertView.findViewById(R.id.historyTitleText);
            holder.historyUrlText=convertView.findViewById(R.id.historyUrlText);
            holder.historyDateText=convertView.findViewById(R.id.historyDateText);
            holder.openHistoryLayoutButton=convertView.findViewById(R.id.openHistoryLayoutButton);
            holder.deleteHistoryButton=convertView.findViewById(R.id.deleteHistoryButton);
            convertView.setTag(holder);
        }
        else{
            holder=(ViewHolder)convertView.getTag();
        }
        // Get Data
        HistoryData data=mHistoryData.get(position);

        // Check Today-Yesterday-Days Ago
        String dateText="";
        String dataDate = data.getDate(),
                currentDate = DateManager.getDate();
        if (currentDate.equals(dataDate))
            dateText = mContext.getString(R.string.today_text);
        else {
            // Check Yesterday
            int currentDateDays=Integer.parseInt(currentDate.split("/")[0].trim());
            int dataDateDays=Integer.parseInt(dataDate.split("/")[0].trim());
            int daysAgo=currentDateDays-dataDateDays;
            if(daysAgo<0)
                daysAgo*=-1;

            if(daysAgo==1)
                dateText = mContext.getString(R.string.yesterday_text);
            else
                dateText = daysAgo+" "+mContext.getString(R.string.days_ago_text);
        }
        holder.historyDateText.setText(dateText);

        String historyTitle=CharLimiter.Limit(data.getTitle(),30);
        String historyUrl= CharLimiter.Limit(data.getUrl(),34);
        holder.historyTitleText.setText(historyTitle);
        holder.historyUrlText.setText(historyUrl);

        holder.openHistoryLayoutButton.setOnClickListener(v -> {
            if(ContextController.getController()==null)
                ContextController.setContext(mContext);
            if(ContextController.getController().getBaseContext()==null){
                Intent mainActivityIntent=new Intent(mContext, MainActivity.class);
                mainActivityIntent.setData(Uri.parse(data.getUrl()));
                finishAndStartActivity(mContext, mainActivityIntent);
            }
            else{
                ContextController.getController().getBaseContextActivity().getIntent()
                        .setData(Uri.parse(data.getUrl()));
                ActivityActionAnimator.finish(mContext);
            }
        });

        holder.deleteHistoryButton.setOnClickListener(v -> {
            // Delete Data
            historyController.deleteHistory(position);
            // Refresh List View
            historyListView.invalidateViews();
            HistoryActivity.updateView(mContext);
        });

        return convertView;
    }

    private static class ViewHolder {
        TextView historyTitleText, historyUrlText, historyDateText;
        LinearLayout openHistoryLayoutButton;
        ImageButton deleteHistoryButton;
    }
}
