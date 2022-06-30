package com.onurkol.app.browser.fragments.installer;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.onurkol.app.browser.R;
import com.onurkol.app.browser.controller.PreferenceController;
import com.onurkol.app.browser.interfaces.BrowserDataInterface;

public class InstallerGUIFragment extends Fragment implements BrowserDataInterface {
    PreferenceController preferenceController;

    ViewPager2 installerPager;
    Button pagerNextButton, pagerPreviousButton;
    LinearLayout simpleGuiLayoutButton, denseGuiLayoutButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.screen_installer_gui_mode, container, false);

        installerPager=requireActivity().findViewById(R.id.installerPager);
        pagerNextButton=view.findViewById(R.id.pagerNextButton);
        pagerPreviousButton=view.findViewById(R.id.pagerPreviousButton);
        simpleGuiLayoutButton=view.findViewById(R.id.simpleGuiLayoutButton);
        denseGuiLayoutButton=view.findViewById(R.id.denseGuiLayoutButton);

        preferenceController=PreferenceController.getController();

        pagerNextButton.setOnClickListener(v -> {
            int nextPage=installerPager.getCurrentItem() + 1;
            installerPager.setCurrentItem(nextPage);
        });
        pagerPreviousButton.setOnClickListener(v -> {
            int previousPage=installerPager.getCurrentItem() - 1;
            installerPager.setCurrentItem(previousPage);
        });

        // Check Values in 'res/browser/values/gui_modes.xml'
        simpleGuiLayoutButton.setOnClickListener(v -> {
            preferenceController.setPreference(BrowserDataInterface.KEY_GUI_MODE, GUI_MODE_SIMPLE);
            resetLayoutButtonsSelect();
        });
        denseGuiLayoutButton.setOnClickListener(v -> {
            preferenceController.setPreference(BrowserDataInterface.KEY_GUI_MODE, GUI_MODE_DENSE);
            resetLayoutButtonsSelect();
        });

        resetLayoutButtonsSelect();
        return view;
    }

    private void resetLayoutButtonsSelect(){
        TypedValue themedValue = new TypedValue();
        requireActivity().getTheme().resolveAttribute(androidx.appcompat.R.attr.selectableItemBackground, themedValue, true);
        simpleGuiLayoutButton.setBackgroundResource(themedValue.resourceId);
        denseGuiLayoutButton.setBackgroundResource(themedValue.resourceId);
        int defaultGui=preferenceController.getInt(BrowserDataInterface.KEY_GUI_MODE);
        if(defaultGui==GUI_MODE_SIMPLE)
            simpleGuiLayoutButton.setBackground(AppCompatResources.getDrawable(requireActivity(), R.drawable.layout_select_border));
        else if(defaultGui==GUI_MODE_DENSE)
            denseGuiLayoutButton.setBackground(AppCompatResources.getDrawable(requireActivity(), R.drawable.layout_select_border));
    }
}
