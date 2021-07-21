package com.onurkol.app.browser.adapters.installer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.onurkol.app.browser.R;
import com.onurkol.app.browser.data.BrowserDataManager;
import com.onurkol.app.browser.data.installer.InstallerDataInteger;
import com.onurkol.app.browser.lib.AppPreferenceManager;

import java.util.ArrayList;

public class InstallerDataItemAdapter extends ArrayAdapter<InstallerDataInteger> {
    private final LayoutInflater inflater;
    private ViewHolder holder;
    private static ArrayList<InstallerDataInteger> dataTypeList;
    private final ListView dataListView;

    // Change Settings and disabled all checkbox.
    ArrayList<CheckBox> checkBoxes=new ArrayList<>();

    // Variables
    String PREF_DATA_KEY=null;
    int preferenceData=0;
    // Classes
    AppPreferenceManager prefManager;

    public InstallerDataItemAdapter(Context context, ListView getDataListView, ArrayList<InstallerDataInteger> getDataList){
        super(context, 0, getDataList);
        dataTypeList=getDataList;
        dataListView=getDataListView;
        inflater=LayoutInflater.from(context);
        prefManager=AppPreferenceManager.getInstance();
    }

    @Override
    public int getCount() {
        return dataTypeList.size();
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
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // Get Classes
        BrowserDataManager dataManager=new BrowserDataManager();

        // Add Checkboxes List
        checkBoxes.add(holder.dataItemCheckbox);

        // Get Data
        InstallerDataInteger dataType=dataTypeList.get(position);

        String getXmlDataString=dataType.getStringData();
        int getXmlDataInteger=dataType.getIntegerData();

        // Write Data
        holder.dataItemNameView.setText(getXmlDataString);

        // Check Preference
        if(dataType.getIsPreferenceData()){
            // Get Preference Key
            PREF_DATA_KEY=dataType.getPreferenceKey();
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
        });
        return convertView;
    }

    private static class ViewHolder {
        TextView dataItemNameView;
        LinearLayout dataItemSelectLayoutButton;
        CheckBox dataItemCheckbox;
    }
}
