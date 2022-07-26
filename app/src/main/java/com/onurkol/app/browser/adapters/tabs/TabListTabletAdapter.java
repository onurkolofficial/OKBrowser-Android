package com.onurkol.app.browser.adapters.tabs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.onurkol.app.browser.R;
import com.onurkol.app.browser.activity.MainActivity;
import com.onurkol.app.browser.controller.tabs.TabController;
import com.onurkol.app.browser.data.tabs.TabData;
import com.onurkol.app.browser.interfaces.BrowserDataInterface;
import com.onurkol.app.browser.libs.CharLimiter;

import java.util.ArrayList;

public class TabListTabletAdapter extends RecyclerView.Adapter<TabListTabletAdapter.ViewHolder> implements BrowserDataInterface {
    private final LayoutInflater mInflater;
    final Context mContext;
    private final ArrayList<TabData> tabDataList;
    private ItemClickListener mClickListener;

    TabController tabController;

    // data is passed into the constructor
    public TabListTabletAdapter(Context context, ArrayList<TabData> data) {
        mInflater = LayoutInflater.from(context);
        mContext=context;
        tabDataList = data;
        tabController=TabController.getController(context);
    }

    // inflates the row layout from xml when needed
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_tab_list_data_tablet, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TabData data = tabDataList.get(position);

        // Check Url and Page Title
        if(data.getUrl()==null){
            if (data.getTabFragment()!=null && data.getTabFragment().isIncognito())
                holder.tabTitleText.setText(mContext.getText(R.string.new_incognito_tab_text));
            else
                holder.tabTitleText.setText(mContext.getText(R.string.new_tab_text));
        }
        else
            holder.tabTitleText.setText(
                    CharLimiter.Limit(data.getTitle(), 30));

        // Check Active Tab Background
        if(tabController.getCurrentTabData().getTabIndex()==position){
            if(!tabController.getCurrentTab().isIncognito() && data.getTabFragment().isIncognito()) {
                holder.tabStatusBackground.setBackground(null);
            }
            else if(tabController.getCurrentTab().isIncognito() && !data.getTabFragment().isIncognito()){
                holder.tabStatusBackground.setBackground(null);
            }
            else {
                // Active Tab Background
                holder.tabStatusBackground.setBackgroundColor(ContextCompat.getColor(mContext,
                        R.color.primary));
                // Active Tab Text Color
                holder.tabTitleText.setTextColor(ContextCompat.getColor(mContext,
                        R.color.text_icon_color_dark));
                // Close Tab Button Color
                holder.tabCloseButton.setColorFilter(ContextCompat.getColor(mContext,
                        R.color.text_icon_color_dark));
            }
        }

        // Change Tab
        holder.tabChangeButton.setOnClickListener(v -> {
            if(data.getTabFragment()!=null && data.getTabFragment().isIncognito()){
                MainActivity.isTabChangedIncognito=true;
                MainActivity.tabChangeIncognitoIndex=data.getTabIndex();
            }
            else {
                MainActivity.isTabChanged=true;
                MainActivity.tabChangeIndex=data.getTabIndex();
            }
            if(data.getTabFragment()!=null)
                data.getTabFragment().restoreMenuUIState(data.getTabFragment().getBackForwardState());
            ((MainActivity)mContext).onTabUpdateWithTablet();
        });

        // Close Tab
        holder.tabCloseButton.setOnClickListener(v -> {
            if(data.getTabFragment()!=null && data.getTabFragment().isIncognito()){
                MainActivity.isTabClosedIncognito=true;
                tabController.closeTab(data.getTabIndex(), true);
            }
            else {
                MainActivity.isTabClosed=true;
                tabController.closeTab(data.getTabIndex(), false);
            }
            removeAt(position);
            ((MainActivity)mContext).onTabUpdateWithTablet();
        });
    }

    public void removeAt(int position) {
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, tabDataList.size());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return tabDataList.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tabTitleText;
        ImageButton tabCloseButton;
        LinearLayout tabStatusBackground;
        CardView tabChangeButton;

        ViewHolder(View itemView) {
            super(itemView);
            tabTitleText = itemView.findViewById(R.id.tabTitleText);
            tabCloseButton = itemView.findViewById(R.id.tabCloseButton);
            tabStatusBackground = itemView.findViewById(R.id.tabStatusBackground);
            tabChangeButton = itemView.findViewById(R.id.tabChangeButton);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    TabData getItem(int id) {
        return tabDataList.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
