package com.onurkol.app.browser.fragments.installer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.onurkol.app.browser.R;

public class InstallerWelcomeFragment extends Fragment {
    ViewPager2 installerPager;
    Button pagerNextButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.screen_installer_welcome, container, false);

        installerPager=requireActivity().findViewById(R.id.installerPager);
        pagerNextButton=view.findViewById(R.id.pagerNextButton);

        pagerNextButton.setOnClickListener(v -> {
            int nextPage=installerPager.getCurrentItem() + 1;
            installerPager.setCurrentItem(nextPage);
        });

        return view;
    }
}
