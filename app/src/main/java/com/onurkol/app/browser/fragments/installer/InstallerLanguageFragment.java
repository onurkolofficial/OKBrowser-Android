package com.onurkol.app.browser.fragments.installer;

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
import com.onurkol.app.browser.data.installer.InstallerDataType;
import com.onurkol.app.browser.interfaces.BrowserDefaultSettings;
import com.onurkol.app.browser.lib.browser.AppLanguage;

import java.util.ArrayList;

public class InstallerLanguageFragment extends Fragment {
    // Elements
    ViewPager2 installerPager;
    Button languageNextButton;
    ListView languageItemList;

    ArrayList<InstallerDataType> LANGUAGE_DATA_LIST=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.page_installer_language, container, false);

        // Get Elements
        installerPager=InstallerActivity.installerPagerStatic.get();
        languageItemList=view.findViewById(R.id.languageItemList);
        languageNextButton=view.findViewById(R.id.languageNextButton);

        // Set Adapter
        languageItemList.setAdapter(new InstallerDataItemAdapter(getActivity(), languageItemList, LANGUAGE_DATA_LIST));

        // Get Data
        ArrayList<String> xmlStringValue=AppLanguage.getInstance().getLanguageNameList();
        ArrayList<Integer> xmlIntegerValue=AppLanguage.getInstance().getLanguageValueList();

        // Add Data
        for(int i=0; i<xmlStringValue.size(); i++)
            LANGUAGE_DATA_LIST.add(new InstallerDataType(xmlStringValue.get(i),xmlIntegerValue.get(i), true, BrowserDefaultSettings.KEY_APP_LANGUAGE));

        // Set Listeners
        languageNextButton.setOnClickListener(installerNextPageListener);

        return view;
    }

    @Override
    public void onResume() {
        // <BUG> Re-Set Adapter
        languageItemList.setAdapter(new InstallerDataItemAdapter(getActivity(), languageItemList, LANGUAGE_DATA_LIST));

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
