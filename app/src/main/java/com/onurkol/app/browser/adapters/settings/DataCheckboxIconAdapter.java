package com.onurkol.app.browser.adapters.settings;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.onurkol.app.browser.R;
import com.onurkol.app.browser.activity.SettingsActivity;
import com.onurkol.app.browser.data.BrowserDataManager;
import com.onurkol.app.browser.data.settings.SettingsPreferenceIconDataInteger;
import com.onurkol.app.browser.interfaces.BrowserDefaultSettings;
import com.onurkol.app.browser.lib.AppPreferenceManager;
import com.onurkol.app.browser.lib.ContextManager;

import java.util.ArrayList;

public class DataCheckboxIconAdapter extends ArrayAdapter<SettingsPreferenceIconDataInteger> {
    private final LayoutInflater inflater;
    private ViewHolder holder;
    private static ArrayList<SettingsPreferenceIconDataInteger> dataList;
    private final ListView dataListView;

    // Change Settings and disabled all checkbox.
    ArrayList<CheckBox> checkBoxes=new ArrayList<>();

    // Variables
    String PREF_DATA_KEY=null;
    int preferenceData=0;
    // Classes
    AppPreferenceManager prefManager;

    public DataCheckboxIconAdapter(Context context, ListView getDataListView, ArrayList<SettingsPreferenceIconDataInteger> getDataList){
        super(context, 0, getDataList);
        dataList=getDataList;
        dataListView=getDataListView;
        inflater=LayoutInflater.from(context);
        prefManager=AppPreferenceManager.getInstance();
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_preferences_checkbox_icon_data, null);
            holder = new ViewHolder();
            holder.dataItemNameView = convertView.findViewById(R.id.dataItemNameView);
            holder.dataItemSelectLayoutButton = convertView.findViewById(R.id.dataItemSelectLayoutButton);
            holder.dataItemIcon = convertView.findViewById(R.id.dataItemIcon);
            holder.dataItemCheckbox = convertView.findViewById(R.id.dataItemCheckbox);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        // Get Classes
        ContextManager contextManager=ContextManager.getManager();
        BrowserDataManager dataManager=new BrowserDataManager();

        // Add Checkboxes List
        checkBoxes.add(holder.dataItemCheckbox);

        // Get Data
        SettingsPreferenceIconDataInteger data=dataList.get(position);

        String getXmlDataString=data.getStringData();
        int getXmlDataInteger=data.getIntegerData();
        Drawable getDataIcon=data.getDataIcon();

        // Check Preference
        if(data.getIsPreferenceData()){
            // Get Preference Key
            PREF_DATA_KEY=data.getPreferenceKey();
            // Get Preference Data (only Integer)
            preferenceData=prefManager.getInt(PREF_DATA_KEY);

            // Check Checkbox
            if(preferenceData==getXmlDataInteger){
                if(!holder.dataItemCheckbox.isChecked())
                    holder.dataItemCheckbox.setChecked(true);
            }
            else
                holder.dataItemCheckbox.setChecked(false);
        }

        // Write Data
        holder.dataItemNameView.setText(getXmlDataString);
        // Load Icon
        if(getDataIcon!=null) {
            getDataIcon.applyTheme(contextManager.getContext().getTheme());
            holder.dataItemIcon.setImageDrawable(getDataIcon);
        }

        // Change Settings
        holder.dataItemSelectLayoutButton.setOnClickListener(view -> {
            // Unselect Checked Checkboxes
            for(int i=0; i<checkBoxes.size(); i++){
                if(checkBoxes.get(i).isChecked()) {
                    checkBoxes.get(i).setChecked(false);
                    break;
                }
            }
            // Select Current Checkbox
            ((CheckBox)view.findViewById(R.id.dataItemCheckbox)).setChecked(true);
            // Check Data, Save Preference
            if(PREF_DATA_KEY!=null)
                prefManager.setPreference(PREF_DATA_KEY,getXmlDataInteger);

            // Update Theme & Language
            dataManager.setApplicationSettings(PREF_DATA_KEY,getXmlDataInteger);

            // Set on Config Changes (not Theme)
            SettingsActivity.isConfigChanged = true;

            if(!PREF_DATA_KEY.equals(BrowserDefaultSettings.KEY_APP_THEME) &&
                    !PREF_DATA_KEY.equals(BrowserDefaultSettings.KEY_SEARCH_ENGINE)) {
                SettingsActivity.changedConfigName = PREF_DATA_KEY;
                SettingsActivity.changedConfigValue = getXmlDataInteger;
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        TextView dataItemNameView;
        LinearLayout dataItemSelectLayoutButton;
        ImageView dataItemIcon;
        CheckBox dataItemCheckbox;
    }
}
