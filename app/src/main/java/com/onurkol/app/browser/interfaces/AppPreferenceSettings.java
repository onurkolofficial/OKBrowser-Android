package com.onurkol.app.browser.interfaces;

import android.content.SharedPreferences;

public interface AppPreferenceSettings {
    String defaultPreferenceName="app.preference.data";

    SharedPreferences getPreferences();

    // Set/Get
    void setPreference(String Name, String Value);
    void setPreference(String Name, int Value);
    void setPreference(String Name, boolean Value);

    String getString(String Name);
    int getInt(String Name);
    boolean getBoolean(String Name);

    void clearPreferences();
}
