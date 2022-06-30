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
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.onurkol.app.browser.R;
import com.onurkol.app.browser.controller.tabs.TabController;
import com.onurkol.app.browser.data.tabs.UserWebCollectionData;
import com.onurkol.app.browser.dialogs.tabs.DialogUserCollectionEditPage;
import com.onurkol.app.browser.libs.CharLimiter;

import java.util.ArrayList;

public class UserWebCollectionListAdapter extends ArrayAdapter<UserWebCollectionData> {
    private final LayoutInflater inflater;
    private final Context mContext;
    private final Fragment mFragment;
    private ViewHolder holder;
    private ArrayList<UserWebCollectionData> mWebCollectionData;

    TabController tabController;

    public UserWebCollectionListAdapter(@NonNull Context context, Fragment fragment, ArrayList<UserWebCollectionData> webCollectionData) {
        super(context, 0, webCollectionData);
        inflater=LayoutInflater.from(context);
        mContext=context;
        mFragment=fragment;
        mWebCollectionData=webCollectionData;
        tabController=TabController.getController(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView=inflater.inflate(R.layout.item_web_collection_data, null);
            holder=new ViewHolder();
            holder.editWebCollectionButton=convertView.findViewById(R.id.editWebCollectionButton);
            holder.webCollectionTitle=convertView.findViewById(R.id.webCollectionTitle);
            holder.webCollectionTitlePreview=convertView.findViewById(R.id.webCollectionTitlePreview);
            holder.openWebCollectionButton=convertView.findViewById(R.id.openWebCollectionButton);
            convertView.setTag(holder);
        }
        else{
            holder=(ViewHolder)convertView.getTag();
        }

        // Get Data
        UserWebCollectionData data=mWebCollectionData.get(position);

        holder.webCollectionTitle.setText(
                CharLimiter.Limit(data.getTitle(), 13));
        char titlePreview=data.getTitle().charAt(0);
        holder.webCollectionTitlePreview.setText(String.valueOf(titlePreview));

        holder.editWebCollectionButton.setOnClickListener(v ->
                DialogUserCollectionEditPage.getDialog(mFragment, position).show());

        holder.openWebCollectionButton.setOnClickListener(v ->
                tabController.getCurrentTab().onStartWeb(data.getUrl()));

        return convertView;
    }

    private static class ViewHolder {
        ImageButton editWebCollectionButton;
        TextView webCollectionTitle, webCollectionTitlePreview;
        CardView openWebCollectionButton;
    }
}
