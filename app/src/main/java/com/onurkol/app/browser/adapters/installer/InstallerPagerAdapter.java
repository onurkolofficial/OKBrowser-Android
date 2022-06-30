package com.onurkol.app.browser.adapters.installer;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.onurkol.app.browser.fragments.installer.InstallerCompleteFragment;
import com.onurkol.app.browser.fragments.installer.InstallerDayNightFragment;
import com.onurkol.app.browser.fragments.installer.InstallerGUIFragment;
import com.onurkol.app.browser.fragments.installer.InstallerLanguageFragment;
import com.onurkol.app.browser.fragments.installer.InstallerPermissionsFragment;
import com.onurkol.app.browser.fragments.installer.InstallerWelcomeFragment;

public class InstallerPagerAdapter extends FragmentStateAdapter {
    private final int INSTALL_PAGE_COUNT=6;

    public InstallerPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
            default:
                return new InstallerWelcomeFragment(); // Default
            case 1:
                return new InstallerLanguageFragment();
            case 2:
                return new InstallerDayNightFragment();
            case 3:
                return new InstallerGUIFragment();
            case 4:
                return new InstallerPermissionsFragment();
            case 5:
                return new InstallerCompleteFragment();
        }
    }

    @Override
    public int getItemCount() {
        return INSTALL_PAGE_COUNT;
    }
}
