package com.onurkol.app.browser.controller;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.onurkol.app.browser.interfaces.FragmentControllerInterface;

import java.lang.ref.WeakReference;

public class FragmentController implements FragmentControllerInterface {
    private static WeakReference<FragmentController> instance=null;
    private FragmentManager supportFragmentManager;

    private FragmentController(){}

    public static synchronized FragmentController getController(){
        if(instance==null || instance.get()==null)
            instance=new WeakReference<>(new FragmentController());
        return instance.get();
    }

    @Override
    public void setSupportFragmentManager(@NonNull FragmentManager fragmentManager) {
        supportFragmentManager=fragmentManager;
    }

    @Override
    public FragmentManager getSupportFragmentManager() {
        return supportFragmentManager;
    }

    @Override
    public void addFragment(int viewId, @NonNull Fragment fragment) {
        supportFragmentManager.beginTransaction().add(viewId, fragment).commit();
    }

    @Override
    public void removeFragment(@NonNull Fragment fragment) {
        supportFragmentManager.beginTransaction().remove(fragment).commit();
    }

    @Override
    public void showFragment(@NonNull Fragment fragment) {
        supportFragmentManager.beginTransaction().show(fragment).commit();
    }

    @Override
    public void hideFragment(@NonNull Fragment fragment) {
        supportFragmentManager.beginTransaction().hide(fragment).commit();
    }

    @Override
    public void attachFragment(@NonNull Fragment fragment) {
        supportFragmentManager.beginTransaction().attach(fragment).commit();
    }

    @Override
    public void detachFragment(@NonNull Fragment fragment) {
        supportFragmentManager.beginTransaction().detach(fragment).commit();
    }
}
