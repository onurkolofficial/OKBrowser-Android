package com.onurkol.app.browser.fragments.installer;

import static com.onurkol.app.browser.data.settings.xml.DayNightXMLToList.getDayNightIconList;
import static com.onurkol.app.browser.data.settings.xml.DayNightXMLToList.getDayNightNameList;
import static com.onurkol.app.browser.data.settings.xml.DayNightXMLToList.getDayNightValueList;

import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.onurkol.app.browser.R;
import com.onurkol.app.browser.adapters.settings.SettingsPreferenceListWithIconAdapter;
import com.onurkol.app.browser.data.settings.SettingXMLDataWithIcon;
import com.onurkol.app.browser.interfaces.BrowserDataInterface;

import java.util.ArrayList;

public class InstallerDayNightFragment extends Fragment implements BrowserDataInterface {
    ViewPager2 installerPager;
    Button pagerNextButton,pagerPreviousButton;
    ListView dayNightList;

    ArrayList<SettingXMLDataWithIcon> DATA_LIST=new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.screen_installer_daynight_mode, container, false);

        installerPager=requireActivity().findViewById(R.id.installerPager);
        pagerNextButton=view.findViewById(R.id.pagerNextButton);
        pagerPreviousButton=view.findViewById(R.id.pagerPreviousButton);
        dayNightList=view.findViewById(R.id.dayNightList);

        // Set ListView Adapter
        dayNightList.setAdapter(new SettingsPreferenceListWithIconAdapter(requireActivity(), DATA_LIST, false));

        pagerNextButton.setOnClickListener(v -> {
            int nextPage=installerPager.getCurrentItem() + 1;
            installerPager.setCurrentItem(nextPage);
        });
        pagerPreviousButton.setOnClickListener(v -> {
            int previousPage=installerPager.getCurrentItem() - 1;
            installerPager.setCurrentItem(previousPage);
        });

        // Get Data
        ArrayList<String> xmlStringValue=getDayNightNameList(requireActivity());
        ArrayList<Integer> xmlIntegerValue=getDayNightValueList(requireActivity());
        TypedArray xmlDataIcons=getDayNightIconList(requireActivity());

        // Add Data
        for(int i=0; i<xmlStringValue.size(); i++) {
            // API 29 and and oldest versions not supported System Theme.
            // Not add this option for API 29 and oldest.
            if (xmlIntegerValue.get(i)==2){
                if(Build.VERSION.SDK_INT > Build.VERSION_CODES.Q)
                    DATA_LIST.add(new SettingXMLDataWithIcon(xmlStringValue.get(i), xmlIntegerValue.get(i), xmlDataIcons.getDrawable(i), KEY_DAY_NIGHT_MODE));
            }
            else
                DATA_LIST.add(new SettingXMLDataWithIcon(xmlStringValue.get(i), xmlIntegerValue.get(i), xmlDataIcons.getDrawable(i), KEY_DAY_NIGHT_MODE));
        }
        return view;
    }

    @Override
    public void onResume() {
        // Re-set Adapter
        dayNightList.setAdapter(new SettingsPreferenceListWithIconAdapter(requireActivity(), DATA_LIST, false));

        super.onResume();
    }
}
