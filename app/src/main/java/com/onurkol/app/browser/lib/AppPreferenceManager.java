package com.onurkol.app.browser.lib;

import android.content.Context;
import android.content.SharedPreferences;

import com.onurkol.app.browser.interfaces.AppPreferenceSettings;

public class AppPreferenceManager implements AppPreferenceSettings {
    private static AppPreferenceManager instance=null;
    private static SharedPreferences preferences;
    Context context;

    // Variables
    public static int INTEGER_NULL=-0x9999;
    // Constructor
    private AppPreferenceManager(){
        // Get Context
        context=ContextManager.getManager().getContext();
        // Get Preferences
        preferences=context.getSharedPreferences(defaultPreferenceName, Context.MODE_PRIVATE);
    }

    public static synchronized AppPreferenceManager getInstance(){
        if(instance==null)
            instance=new AppPreferenceManager();
        return instance;
    }

    @Override
    public SharedPreferences getPreferences() {
        return preferences;
    }
    // Set
    @Override
    public void setPreference(String Name, String Value) {
        preferences.edit().putString(Name,Value).apply();
    }
    @Override
    public void setPreference(String Name, int Value) {
        preferences.edit().putInt(Name,Value).apply();
    }
    @Override
    public void setPreference(String Name, boolean Value) {
        preferences.edit().putBoolean(Name,Value).apply();
    }
    // Get
    @Override
    public String getString(String Name) {
        return preferences.getString(Name,null);
    }
    @Override
    public int getInt(String Name) {
        return preferences.getInt(Name,INTEGER_NULL);
    }
    @Override
    public boolean getBoolean(String Name) {
        return preferences.getBoolean(Name,false);
    }
    // Clear
    @Override
    public void clearPreferences() {
        preferences.edit().clear().apply();
    }
}
