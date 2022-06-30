package com.onurkol.app.browser.adapters.settings;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.onurkol.app.browser.R;
import com.onurkol.app.browser.controller.settings.DayNightModeController;
import com.onurkol.app.browser.controller.settings.LanguageController;
import com.onurkol.app.browser.controller.PreferenceController;
import com.onurkol.app.browser.data.settings.SettingXMLData;
import com.onurkol.app.browser.interfaces.BrowserDataInterface;

import java.util.ArrayList;

public class SettingsPreferenceListAdapter extends ArrayAdapter<SettingXMLData> implements BrowserDataInterface {
    private final LayoutInflater inflater;
    private final Context getContext;
    private ViewHolder holder;
    private static ArrayList<SettingXMLData> dataTypeList;

    // Change Settings and disabled all checkbox.
    ArrayList<CheckBox> checkBoxes=new ArrayList<>();

    // Variables
    String PREF_DATA_KEY=null;
    int preferenceData=0;
    boolean mSettingsPreference;

    PreferenceController preferenceController;
    LanguageController languageController;
    DayNightModeController dayNightModeController;

    public SettingsPreferenceListAdapter(@NonNull Context context, ArrayList<SettingXMLData> getDataList, boolean isSettingsPreference) {
        super(context, 0, getDataList);
        dataTypeList=getDataList;
        inflater=LayoutInflater.from(context);
        preferenceController=PreferenceController.getController();
        languageController=LanguageController.getController();
        dayNightModeController=DayNightModeController.getController();
        getContext=context;
        mSettingsPreference=isSettingsPreference;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_checkbox_data, null);
            holder = new ViewHolder();
            holder.dataItemNameView = convertView.findViewById(R.id.dataItemNameView);
            holder.dataItemSelectLayoutButton = convertView.findViewById(R.id.dataItemSelectLayoutButton);
            holder.dataItemCheckbox = convertView.findViewById(R.id.dataItemCheckbox);
            convertView.setTag(holder);
        }
        else{
            holder=(ViewHolder)convertView.getTag();
        }

        // Add Checkboxes List
        checkBoxes.add(holder.dataItemCheckbox);

        // Get Data
        SettingXMLData dataType=dataTypeList.get(position);

        String getXmlDataString=dataType.getStringData();
        int getXmlDataInteger=dataType.getIntegerData();

        // Write Data
        holder.dataItemNameView.setText(getXmlDataString);

        // Check Preference
        if(dataType.getPreferenceKey()!=null){
            // Get Preference Key
            PREF_DATA_KEY=dataType.getPreferenceKey();
            // Get Preference Data (only Integer)
            preferenceData=preferenceController.getInt(PREF_DATA_KEY);

            // Check Checkbox
            if(preferenceData==getXmlDataInteger){
                if(!holder.dataItemCheckbox.isChecked())
                    holder.dataItemCheckbox.setChecked(true);
            }
            else
                holder.dataItemCheckbox.setChecked(false);
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
            if(PREF_DATA_KEY!=null) {
                preferenceController.setPreference(PREF_DATA_KEY, getXmlDataInteger);
                // Update Theme & Language
                if(!PREF_DATA_KEY.equals(KEY_SEARCH_ENGINE))
                    ((Activity)getContext).recreate();

                /*
                 *
                // Update Theme & Language (Alternative)
                switch (PREF_DATA_KEY) {
                    case KEY_DAY_NIGHT_MODE:
                        dayNightModeController.setDayNightMode(getXmlDataInteger);
                        ((Activity)getContext).recreate();
                        break;
                    case KEY_LANGUAGE:
                        languageController.setLanguage(getContext(), getXmlDataInteger);
                        ((Activity)getContext).recreate();
                        break;
                }
                 *
                 */
            }
        });
        return convertView;
    }

    @Override
    public int getCount() {
        return dataTypeList.size();
    }

    private static class ViewHolder {
        TextView dataItemNameView;
        LinearLayoutCompat dataItemSelectLayoutButton;
        CheckBox dataItemCheckbox;
    }
}
