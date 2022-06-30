package com.onurkol.app.browser.fragments.installer;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.onurkol.app.browser.R;
import com.onurkol.app.browser.activity.MainActivity;
import com.onurkol.app.browser.controller.browser.BrowserDataInitController;

public class InstallerCompleteFragment extends Fragment {
    Button installerCompleteButton;

    BrowserDataInitController browserDataController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.screen_installer_complete, container, false);

        browserDataController=BrowserDataInitController.getController();

        installerCompleteButton=view.findViewById(R.id.installerCompleteButton);

        installerCompleteButton.setOnClickListener(v -> {
            browserDataController.setInstallerCompleted(true);
            startActivity(new Intent(requireActivity(), MainActivity.class));
            requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            requireActivity().finish();
            requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        return view;
    }
}
