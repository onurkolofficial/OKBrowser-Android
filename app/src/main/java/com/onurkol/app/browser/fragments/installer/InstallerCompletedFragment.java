package com.onurkol.app.browser.fragments.installer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.onurkol.app.browser.R;
import com.onurkol.app.browser.activity.MainActivity;
import com.onurkol.app.browser.activity.browser.installer.InstallerActivity;
import com.onurkol.app.browser.data.BrowserDataManager;
import com.onurkol.app.browser.interfaces.BrowserActionKeys;
import com.onurkol.app.browser.lib.ContextManager;

public class InstallerCompletedFragment extends Fragment implements BrowserActionKeys {
    // Elements
    ViewPager2 installerPager;
    Button installerCompleteButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.page_installer_complete, container, false);

        // Get Elements
        installerPager=InstallerActivity.installerPagerStatic.get();
        installerCompleteButton=view.findViewById(R.id.installerCompleteButton);

        // Click Events
        installerCompleteButton.setOnClickListener(completeInstallerListener);

        // Reset MainActivity
        MainActivity.isCreated=false;

        return view;
    }

    // Listeners
    View.OnClickListener completeInstallerListener=view -> {
        // Completed Installer Activity
        Context context=ContextManager.getManager().getContext();
        // Get Data Manager
        BrowserDataManager bdManager=new BrowserDataManager();
        // init
        bdManager.initBrowserPreferenceSettings();
        // load
        bdManager.successDataLoad();
        // Intent
        Intent mainActivityIntent=new Intent(context, MainActivity.class);
        // Set Intent Data
        // Create new Bundle
        Bundle bundle = new Bundle();
        bundle.putString(ACTION_NAME, KEY_ACTION_BROWSER_START_INSTALLER);
        mainActivityIntent.putExtras(bundle);
        // Start Browser & Close Installer Activity
        context.startActivity(mainActivityIntent);
        ContextManager.getManager().getContextActivity().finish();
    };
}
