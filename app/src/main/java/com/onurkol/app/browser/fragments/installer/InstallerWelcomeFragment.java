package com.onurkol.app.browser.fragments.installer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.onurkol.app.browser.R;
import com.onurkol.app.browser.activity.browser.installer.InstallerActivity;

public class InstallerWelcomeFragment extends Fragment {
    // Elements
    ViewPager2 installerPager;
    Button welcomeNextButton;
    TextView welcomeTitle,welcomeText,welcomeNotice;
    ImageView welcomeAppLogo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.page_installer_welcome, container, false);

        // Get Elements
        installerPager=InstallerActivity.installerPagerStatic.get();
        welcomeNextButton=view.findViewById(R.id.welcomeNextButton);
        welcomeTitle=view.findViewById(R.id.welcomeTitle);
        welcomeAppLogo=view.findViewById(R.id.welcomeAppLogo);
        welcomeText=view.findViewById(R.id.welcomeText);
        welcomeNotice=view.findViewById(R.id.welcomeNotice);

        // Set Listeners
        welcomeNextButton.setOnClickListener(installerNextPageListener);

        // Start Animations
        initAnimationVariables();
        startWindowAnimations();

        return view;
    }

    private void initAnimationVariables(){
        // Set Default Variables
        welcomeAppLogo.setAlpha(0.0f);
        welcomeAppLogo.setTranslationY(welcomeAppLogo.getHeight()+200);
        welcomeTitle.setAlpha(0.0f);
        welcomeTitle.setTranslationY(welcomeTitle.getHeight()+200);
        welcomeText.setAlpha(0.0f);
        welcomeText.setTranslationY(welcomeText.getHeight()+200);
        welcomeNotice.setAlpha(0.0f);
        welcomeNotice.setTranslationY(welcomeNotice.getHeight()+200);
        welcomeNextButton.setAlpha(0.0f);
        welcomeNextButton.setTranslationY(welcomeNextButton.getHeight()+200);
    }
    private void startWindowAnimations(){
        // Set Animations Variables
        int duration=900;

        welcomeAppLogo.animate()
                .translationY(welcomeAppLogo.getHeight())
                .alpha(1.0f)
                .setDuration(duration);
        welcomeTitle.animate()
                .translationY(welcomeTitle.getHeight())
                .alpha(1.0f)
                .setDuration(duration).setStartDelay(1000);
        welcomeText.animate()
                .translationY(welcomeText.getHeight())
                .alpha(1.0f)
                .setDuration(duration).setStartDelay(1800);
        welcomeNotice.animate()
                .translationY(welcomeNotice.getHeight())
                .alpha(1.0f)
                .setDuration(duration).setStartDelay(3000);
        welcomeNextButton.animate()
                .translationY(welcomeNextButton.getHeight())
                .alpha(1.0f)
                .setDuration(duration).setStartDelay(3000);
    }

    View.OnClickListener installerNextPageListener= view -> {
        // Get Next Page
        int nextPage=installerPager.getCurrentItem() + 1;
        // Open Next Page
        installerPager.setCurrentItem(nextPage);
    };
}
