package com.onurkol.app.browser.fragments.installer;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.onurkol.app.browser.R;
import com.onurkol.app.browser.controller.PermissionController;

public class InstallerPermissionsFragment extends Fragment {
    ViewPager2 installerPager;
    Button pagerNextButton,pagerPreviousButton;
    LinearLayout permInternetLayoutButton,permStorageLayoutButton;
    CheckBox permInternetCheckbox,permStorageCheckbox;

    PermissionController permissionController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.screen_installer_permissions, container, false);

        permissionController=PermissionController.getController();

        installerPager=requireActivity().findViewById(R.id.installerPager);
        pagerNextButton=view.findViewById(R.id.pagerNextButton);
        pagerPreviousButton=view.findViewById(R.id.pagerPreviousButton);
        permInternetLayoutButton=view.findViewById(R.id.permInternetLayoutButton);
        permInternetCheckbox=view.findViewById(R.id.permInternetCheckbox);
        permStorageLayoutButton=view.findViewById(R.id.permStorageLayoutButton);
        permStorageCheckbox=view.findViewById(R.id.permStorageCheckbox);

        pagerNextButton.setOnClickListener(v -> {
            int nextPage=installerPager.getCurrentItem() + 1;
            installerPager.setCurrentItem(nextPage);
        });
        pagerPreviousButton.setOnClickListener(v -> {
            int previousPage=installerPager.getCurrentItem() - 1;
            installerPager.setCurrentItem(previousPage);
        });

        permInternetLayoutButton.setOnClickListener(v -> permissionController.setInternetPermission(requireActivity()));
        permStorageLayoutButton.setOnClickListener(v -> permissionController.setStoragePermission(requireActivity()));

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            permStorageLayoutButton.setVisibility(View.GONE);

        return view;
    }

    @Override
    public void onResume() {
        updateCheckboxes();
        super.onResume();
    }

    public void updateCheckboxes(){
        // Checkboxes
        permInternetCheckbox.setChecked(permissionController.getInternetPermission(requireActivity()));
        permStorageCheckbox.setChecked(permissionController.getStoragePermission(requireActivity()));
    }
}
