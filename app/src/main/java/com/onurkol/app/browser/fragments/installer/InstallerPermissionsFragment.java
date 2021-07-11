package com.onurkol.app.browser.fragments.installer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.onurkol.app.browser.R;
import com.onurkol.app.browser.activity.browser.installer.InstallerActivity;
import com.onurkol.app.browser.lib.core.PermissionManager;

public class InstallerPermissionsFragment extends Fragment {
    // Elements
    ViewPager2 installerPager;
    Button permissionsNextButton;
    LinearLayout permInternetLayoutButton,permStorageLayoutButton;
    CheckBox permInternetCheckbox,permStorageCheckbox;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.page_installer_permissions, container, false);

        // Get Elements
        installerPager=InstallerActivity.installerPagerStatic.get();
        permissionsNextButton=view.findViewById(R.id.permissionsNextButton);
        permInternetLayoutButton=view.findViewById(R.id.permInternetLayoutButton);
        permInternetCheckbox=view.findViewById(R.id.permInternetCheckbox);
        permStorageLayoutButton=view.findViewById(R.id.permStorageLayoutButton);
        permStorageCheckbox=view.findViewById(R.id.permStorageCheckbox);

        // Set Listeners
        permissionsNextButton.setOnClickListener(installerNextPageListener);
        permInternetLayoutButton.setOnClickListener(internetPermissionListener);
        permStorageLayoutButton.setOnClickListener(storagePermissionListener);;

        // Check Permissions Checkbox
        updateCheckboxes();

        return view;
    }

    @Override
    public void onResume() {
        updateCheckboxes();
        super.onResume();
    }

    public void updateCheckboxes(){
        // Permission Manager
        PermissionManager permManager=PermissionManager.getInstance();
        // Checkboxes
        permInternetCheckbox.setChecked(permManager.getInternetPermission());
        permStorageCheckbox.setChecked(permManager.getStoragePermission());
    }

    // Listeners
    View.OnClickListener installerNextPageListener=view -> {
        // Get Next Page
        int nextPage=installerPager.getCurrentItem() + 1;
        // Open Next Page
        installerPager.setCurrentItem(nextPage);
    };
    View.OnClickListener internetPermissionListener=view -> {
        // Permission Manager
        PermissionManager permManager=PermissionManager.getInstance();
        // Permission Dialog
        permManager.setInternetPermission();
    };
    View.OnClickListener storagePermissionListener=view -> {
        // Permission Manager
        PermissionManager permManager=PermissionManager.getInstance();
        // Permission Dialog
        permManager.setStoragePermission();
    };
}
