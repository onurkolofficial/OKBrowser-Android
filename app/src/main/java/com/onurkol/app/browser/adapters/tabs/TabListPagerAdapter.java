package com.onurkol.app.browser.adapters.tabs;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.onurkol.app.browser.fragments.tabs.list.TabIncognitoListPagerFragment;
import com.onurkol.app.browser.fragments.tabs.list.TabListPagerFragment;

public class TabListPagerAdapter extends FragmentStateAdapter {
    private final int PAGE_COUNT=2; // Tabs, Incognito Tabs

    public TabListPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
            default:
                return new TabListPagerFragment();
            case 1:
                return new TabIncognitoListPagerFragment();
        }
    }

    @Override
    public int getItemCount() {
        return PAGE_COUNT;
    }
}
