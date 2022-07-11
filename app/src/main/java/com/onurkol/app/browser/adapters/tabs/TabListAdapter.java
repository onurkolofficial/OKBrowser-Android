package com.onurkol.app.browser.adapters.tabs;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import androidx.viewpager2.widget.ViewPager2;

import com.onurkol.app.browser.R;
import com.onurkol.app.browser.activity.MainActivity;
import com.onurkol.app.browser.controller.tabs.TabController;
import com.onurkol.app.browser.data.tabs.TabData;
import com.onurkol.app.browser.interfaces.BrowserDataInterface;
import com.onurkol.app.browser.libs.ActivityActionAnimator;
import com.onurkol.app.browser.libs.CharLimiter;
import com.onurkol.app.browser.libs.ScreenManager;

import java.util.ArrayList;

public class TabListAdapter extends ArrayAdapter<TabData> implements BrowserDataInterface {
    private final LayoutInflater inflater;
    private final Context mContext;
    private ViewHolder holder;
    private final ArrayList<TabData> mTabData;

    TabController tabController;

    public TabListAdapter(@NonNull Context context, ArrayList<TabData> tabData) {
        super(context, 0, tabData);
        inflater=LayoutInflater.from(context);
        mContext=context;
        mTabData=tabData;
        tabController=TabController.getController(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_tab_list_data, null);
            holder = new ViewHolder();
            holder.tabChangeButton=convertView.findViewById(R.id.tabChangeButton);
            holder.tabTitleText=convertView.findViewById(R.id.tabTitleText);
            holder.tabCloseButton=convertView.findViewById(R.id.tabCloseButton);
            holder.tabPreviewImage=convertView.findViewById(R.id.tabPreviewImage);
            holder.tabStatusBackground=convertView.findViewById(R.id.tabStatusBackground);
            convertView.setTag(holder);
        }
        else{
            holder=(ViewHolder)convertView.getTag();
        }

        // Get Data
        TabData data=mTabData.get(position);

        // Check Url and Page Title
        if(data.getUrl()==null){
            if (data.getTabFragment()!=null && data.getTabFragment().isIncognito())
                holder.tabTitleText.setText(mContext.getText(R.string.new_incognito_tab_text));
            else
                holder.tabTitleText.setText(mContext.getText(R.string.new_tab_text));
        }
        else
            holder.tabTitleText.setText(
                    CharLimiter.Limit(data.getTitle(), 15));

        // Check Active Tab Border
        if(tabController.getCurrentTabData().getTabIndex()==position){
            if(!tabController.getCurrentTab().isIncognito() && data.getTabFragment().isIncognito()) {
                holder.tabStatusBackground.setBackground(null);
            }
            else if(tabController.getCurrentTab().isIncognito() && !data.getTabFragment().isIncognito()){
                holder.tabStatusBackground.setBackground(null);
            }
            else
                holder.tabStatusBackground.setBackground(AppCompatResources.getDrawable(mContext,
                        R.drawable.layout_select_border_corner));
        }


        // Check Preview Image
        Bitmap getBitmap;
        if(data.getTabFragment()!=null)
            getBitmap=ScreenManager.getScreenshot(mContext, data.getTabFragment().getView());
        else
            getBitmap=data.getPreviewBitmap();
        holder.tabPreviewImage.setImageBitmap(getBitmap);

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
            ActivityActionAnimator.finish(mContext);
        });

        // Close Tab
        holder.tabCloseButton.setOnClickListener(v -> {
            if(data.getTabFragment()!=null && data.getTabFragment().isIncognito()){
                MainActivity.isTabClosedIncognito=true;
                MainActivity.tabCloseIncognitoIndexList.add(data.getTabIndex());
                if(INCOGNITO_TAB_DATA_LIST.size()<=1){
                    if(TAB_DATA_LIST.size()>0) {
                        ViewPager2 pager=((Activity)mContext).findViewById(R.id.tabListPager);
                        pager.setCurrentItem(0);
                        INCOGNITO_TAB_DATA_LIST.remove(data.getTabIndex());
                        notifyDataSetChanged();
                    }
                    else
                        ActivityActionAnimator.finish(mContext);
                }
                else{
                    INCOGNITO_TAB_DATA_LIST.remove(data.getTabIndex());
                    tabController.recreateTabIndex(true);
                    notifyDataSetChanged();
                }
            }
            else {
                MainActivity.isTabClosed=true;
                MainActivity.tabCloseIndexList.add(data.getTabIndex());
                if (TAB_DATA_LIST.size()<=1){
                    if(INCOGNITO_TAB_DATA_LIST.size()>0){
                        ViewPager2 pager=((Activity)mContext).findViewById(R.id.tabListPager);
                        pager.setCurrentItem(1);
                        TAB_DATA_LIST.remove(data.getTabIndex());
                        notifyDataSetChanged();
                    }
                    else
                        ActivityActionAnimator.finish(mContext);
                }
                else {
                    TAB_DATA_LIST.remove(data.getTabIndex());
                    tabController.recreateTabIndex(false);
                    notifyDataSetChanged();
                }
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        CardView tabChangeButton;
        TextView tabTitleText;
        ImageButton tabCloseButton;
        ImageView tabPreviewImage;
        LinearLayout tabStatusBackground;
    }
}
