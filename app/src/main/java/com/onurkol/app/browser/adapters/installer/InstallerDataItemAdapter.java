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
import com.onurkol.app.browser.data.installer.InstallerDataType;
import com.onurkol.app.browser.interfaces.BrowserDefaultSettings;
import com.onurkol.app.browser.lib.AppPreferenceManager;
import com.onurkol.app.browser.lib.ContextManager;
import com.onurkol.app.browser.lib.core.LanguageManager;
import com.onurkol.app.browser.lib.core.ThemeManager;

import java.util.ArrayList;

public class InstallerDataItemAdapter extends ArrayAdapter<InstallerDataType> {
    private final LayoutInflater inflater;
    private ViewHolder holder;
    private static ArrayList<InstallerDataType> dataTypeList;
    private final ListView dataListView;

    // TEST
    ArrayList<CheckBox> checkBoxes=new ArrayList<>();

    // Variables
    String PREF_DATA_KEY=null;
    int preferenceData=0;
    // Classes
    AppPreferenceManager prefManager;

    public InstallerDataItemAdapter(Context context, ListView getDataListView, ArrayList<InstallerDataType> getDataList){
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
            convertView = inflater.inflate(R.layout.item_installer_data_list, null);
            holder = new ViewHolder();
            holder.dataItemNameView = convertView.findViewById(R.id.dataItemNameView);
            holder.dataItemSelectLayoutButton = convertView.findViewById(R.id.dataItemSelectLayoutButton);
            holder.dataItemCheckbox = convertView.findViewById(R.id.dataItemCheckbox);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Add Checkboxes List
        checkBoxes.add(holder.dataItemCheckbox);

        // Get Data
        InstallerDataType dataType=dataTypeList.get(position);

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
            if(PREF_DATA_KEY.equals(BrowserDefaultSettings.KEY_APP_THEME))
                // Update Theme
                ThemeManager.getInstance().setAppTheme(getXmlDataInteger);
            else if(PREF_DATA_KEY.equals(BrowserDefaultSettings.KEY_APP_LANGUAGE)) {
                // Update Language
                LanguageManager.getInstance().setAppLanguage(getXmlDataInteger);
                // Refresh
                ContextManager.getManager().getContextActivity().recreate();
            }
        });
        return convertView;
    }

    private static class ViewHolder {
        TextView dataItemNameView;
        LinearLayout dataItemSelectLayoutButton;
        CheckBox dataItemCheckbox;
    }
}
