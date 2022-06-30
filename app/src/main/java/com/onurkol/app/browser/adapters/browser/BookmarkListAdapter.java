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
import com.onurkol.app.browser.activity.browser.BookmarkActivity;
import com.onurkol.app.browser.activity.browser.HistoryActivity;
import com.onurkol.app.browser.controller.ContextController;
import com.onurkol.app.browser.controller.browser.BookmarkController;
import com.onurkol.app.browser.data.browser.BookmarkData;
import com.onurkol.app.browser.libs.ActivityActionAnimator;
import com.onurkol.app.browser.libs.CharLimiter;

import java.util.ArrayList;

public class BookmarkListAdapter extends ArrayAdapter<BookmarkData> {
    private final LayoutInflater inflater;
    private final Context mContext;
    private ViewHolder holder;
    private ArrayList<BookmarkData> mBookmarkData;

    BookmarkController bookmarkController;

    ListView bookmarkListView;

    public BookmarkListAdapter(@NonNull Context context, ListView listView, ArrayList<BookmarkData> bookmarkData){
        super(context, 0, bookmarkData);
        inflater=LayoutInflater.from(context);
        mContext=context;
        mBookmarkData=bookmarkData;
        bookmarkListView=listView;
        bookmarkController=BookmarkController.getController();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_bookmark_data, null);
            holder = new ViewHolder();
            holder.bookmarkTitleText=convertView.findViewById(R.id.bookmarkTitleText);
            holder.bookmarkUrlText=convertView.findViewById(R.id.bookmarkUrlText);
            holder.openBookmarkLayoutButton=convertView.findViewById(R.id.openBookmarkLayoutButton);
            holder.deleteBookmarkButton=convertView.findViewById(R.id.deleteBookmarkButton);
            convertView.setTag(holder);
        }
        else{
            holder=(ViewHolder)convertView.getTag();
        }
        // Get Data
        BookmarkData data=mBookmarkData.get(position);

        String historyTitle=CharLimiter.Limit(data.getTitle(),34);
        String historyUrl=CharLimiter.Limit(data.getUrl(),38);
        holder.bookmarkTitleText.setText(historyTitle);
        holder.bookmarkUrlText.setText(historyUrl);

        holder.openBookmarkLayoutButton.setOnClickListener(v -> {
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

        holder.deleteBookmarkButton.setOnClickListener(v -> {
            // Delete Data
            bookmarkController.deleteBookmark(position);
            // Refresh List View
            bookmarkListView.invalidateViews();
            BookmarkActivity.updateView(mContext);
        });

        return convertView;
    }

    private static class ViewHolder {
        TextView bookmarkTitleText, bookmarkUrlText;
        LinearLayout openBookmarkLayoutButton;
        ImageButton deleteBookmarkButton;
    }
}
