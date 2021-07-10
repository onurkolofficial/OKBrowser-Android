package com.onurkol.app.browser.adapters.installer;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.onurkol.app.browser.fragments.installer.InstallerCompletedFragment;
import com.onurkol.app.browser.fragments.installer.InstallerLanguageFragment;
import com.onurkol.app.browser.fragments.installer.InstallerThemeFragment;
import com.onurkol.app.browser.fragments.installer.InstallerWelcomeFragment;

public class InstallerPagerAdapter extends FragmentStateAdapter {
    public static int pageCount=4;

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
                return  new InstallerLanguageFragment();
            case 2:
                return new InstallerThemeFragment();
            case 3:
                return new InstallerCompletedFragment();
        }
    }

    @Override
    public int getItemCount() {
        return pageCount;
    }
}
