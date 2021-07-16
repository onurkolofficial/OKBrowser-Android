package com.onurkol.app.browser.fragments.installer;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.onurkol.app.browser.R;
import com.onurkol.app.browser.activity.browser.installer.InstallerActivity;
import com.onurkol.app.browser.adapters.installer.InstallerDataItemAdapter;
import com.onurkol.app.browser.data.installer.InstallerDataInteger;
import com.onurkol.app.browser.interfaces.BrowserDefaultSettings;
import com.onurkol.app.browser.lib.settings.AppTheme;

import java.util.ArrayList;

public class InstallerThemeFragment extends Fragment {
    // Elements
    ViewPager2 installerPager;
    ListView themeItemList;
    Button themeNextButton;

    ArrayList<InstallerDataInteger> THEME_DATA_LIST=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.page_installer_theme, container, false);

        // Get Elements
        installerPager=InstallerActivity.installerPagerStatic.get();
        themeItemList=view.findViewById(R.id.themeItemList);
        themeNextButton=view.findViewById(R.id.themeNextButton);

        // Set Adapter
        themeItemList.setAdapter(new InstallerDataItemAdapter(getActivity(), themeItemList, THEME_DATA_LIST));

        // Get Data
        ArrayList<String> xmlStringValue=AppTheme.getInstance().getThemeNameList();
        ArrayList<Integer> xmlIntegerValue=AppTheme.getInstance().getThemeValueList();

        // Add Data
        for(int i=0; i<xmlStringValue.size(); i++){
            // API 29 and and oldest versions not supported System Theme.
            // Not add this option for API 29 and oldest.
            if (xmlIntegerValue.get(i)==2){
                if(Build.VERSION.SDK_INT > Build.VERSION_CODES.Q)
                    THEME_DATA_LIST.add(new InstallerDataInteger(xmlStringValue.get(i), xmlIntegerValue.get(i), true, BrowserDefaultSettings.KEY_APP_THEME));
            }
            else
                THEME_DATA_LIST.add(new InstallerDataInteger(xmlStringValue.get(i), xmlIntegerValue.get(i), true, BrowserDefaultSettings.KEY_APP_THEME));
        }

        // Set Listeners
        themeNextButton.setOnClickListener(installerNextPageListener);

        return view;
    }

    @Override
    public void onResume() {
        // Re-Set Adapter
        themeItemList.setAdapter(new InstallerDataItemAdapter(getActivity(), themeItemList, THEME_DATA_LIST));

        super.onResume();
    }

    // Listeners
    View.OnClickListener installerNextPageListener=view -> {
        // Get Next Page
        int nextPage=installerPager.getCurrentItem() + 1;
        // Open Next Page
        installerPager.setCurrentItem(nextPage);
    };
}
