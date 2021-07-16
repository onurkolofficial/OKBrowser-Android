package com.onurkol.app.browser.adapters.browser.tabs;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.onurkol.app.browser.fragments.browser.tabs.list.IncognitoTabListFragment;
import com.onurkol.app.browser.fragments.browser.tabs.list.TabListFragment;

public class TabListsPageAdapter extends FragmentStateAdapter {
    public static int pageCount=2;

    public TabListsPageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:
                return new IncognitoTabListFragment();
            case 0:
            default:
                return new TabListFragment(); // Default
        }
    }

    @Override
    public int getItemCount() {
        return pageCount;
    }

}
