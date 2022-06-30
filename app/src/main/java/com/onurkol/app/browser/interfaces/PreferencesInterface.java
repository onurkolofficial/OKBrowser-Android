package com.onurkol.app.browser.interfaces;

import android.content.SharedPreferences;

public interface PreferencesInterface {
    int INT_NULL=-1;

    SharedPreferences getSharedPreferences();

    void setPreference(String Name, String Value);
    void setPreference(String Name, int Value);
    void setPreference(String Name, boolean Value);

    String getString(String Name);
    int getInt(String Name);
    boolean getBoolean(String Name);

    boolean isExist(String Name);

    void clearAll();
}
