package com.onurkol.app.browser.controller;

import android.content.Context;
import android.content.SharedPreferences;

import com.onurkol.app.browser.interfaces.BrowserDataInterface;
import com.onurkol.app.browser.interfaces.PreferencesInterface;

import java.lang.ref.WeakReference;

public class PreferenceController implements BrowserDataInterface, PreferencesInterface {
    private static WeakReference<PreferenceController> instance=null;
    private static SharedPreferences preferences;
    Context context;

    private PreferenceController(){
        context=ContextController.getController().getContext();
        preferences=context.getSharedPreferences(BROWSER_PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized PreferenceController getController(){
        if(instance==null || instance.get()==null)
            instance=new WeakReference<>(new PreferenceController());
        return instance.get();
    }

    // Getter,Setter
    @Override
    public SharedPreferences getSharedPreferences(){
        return preferences;
    }

    @Override
    public void setPreference(String Name, String Value) {
        preferences.edit().putString(Name, Value).apply();
    }
    @Override
    public void setPreference(String Name, int Value) {
        preferences.edit().putInt(Name, Value).apply();
    }
    @Override
    public void setPreference(String Name, boolean Value) {
        preferences.edit().putBoolean(Name, Value).apply();
    }

    @Override
    public String getString(String Name) {
        return preferences.getString(Name, null);
    }
    @Override
    public int getInt(String Name) {
        return preferences.getInt(Name, INT_NULL);
    }
    @Override
    public boolean getBoolean(String Name) {
        return preferences.getBoolean(Name, false);
    }

    @Override
    public boolean isExist(String Name) {
        return preferences.contains(Name);
    }

    @Override
    public void clearAll() {
        preferences.edit().clear().apply();
    }
}
