package com.onurkol.app.browser.interfaces;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public interface FragmentControllerInterface {
    void setSupportFragmentManager(@NonNull FragmentManager fragmentManager);
    FragmentManager getSupportFragmentManager();

    void addFragment(int viewId, @NonNull Fragment fragment);
    void removeFragment(@NonNull Fragment fragment);
    void showFragment(@NonNull Fragment fragment);
    void hideFragment(@NonNull Fragment fragment);
    void attachFragment(@NonNull Fragment fragment);
    void detachFragment(@NonNull Fragment fragment);
}
