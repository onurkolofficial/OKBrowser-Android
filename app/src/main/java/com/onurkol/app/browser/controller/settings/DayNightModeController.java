package com.onurkol.app.browser.controller.settings;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;

import androidx.appcompat.app.AppCompatDelegate;

import com.onurkol.app.browser.R;
import com.onurkol.app.browser.controller.PreferenceController;
import com.onurkol.app.browser.interfaces.BrowserDataInterface;

import java.lang.ref.WeakReference;

public class DayNightModeController implements BrowserDataInterface {
    private static WeakReference<DayNightModeController> instance=null;

    PreferenceController preferenceController;

    private DayNightModeController(){
        preferenceController=PreferenceController.getController();
    }

    public static synchronized DayNightModeController getController(){
        if(instance==null || instance.get()==null)
            instance=new WeakReference<>(new DayNightModeController());
        return instance.get();
    }

    // API 29 and lower 'AppCompatDelegate' freezing on night mode change.
    // So used 'setTheme'.
    @Deprecated
    public void setDayNightMode(int mode){
        switch (mode) {
            case DAY_NIGHT_MODE_AUTO:
            default:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
            case DAY_NIGHT_MODE_DAY:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case DAY_NIGHT_MODE_NIGHT:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
        }
    }

    public void setDayNightMode(Context context, int mode){
        switch (mode) {
            case DAY_NIGHT_MODE_AUTO:
            default:
                configChangeClass(context, false);
                break;
            case DAY_NIGHT_MODE_DAY:
                context.setTheme(R.style.Theme_OKBrowser);
                break;
            case DAY_NIGHT_MODE_NIGHT:
                context.setTheme(R.style.Theme_OKBrowser_Night);
                break;
        }
    }

    public void onConfigChanges(Context context){
        configChangeClass(context, true);
    }

    private void configChangeClass(Context context, boolean isRecreate){
        if(preferenceController.getInt(KEY_DAY_NIGHT_MODE)==DAY_NIGHT_MODE_AUTO){
            int flag = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
            if (flag == Configuration.UI_MODE_NIGHT_YES)
                context.setTheme(R.style.Theme_OKBrowser_Night);
            else if (flag == Configuration.UI_MODE_NIGHT_NO)
                context.setTheme(R.style.Theme_OKBrowser);
            if(isRecreate)
                ((Activity)context).recreate();
        }
    }

    public boolean isNightMode() {
        return (preferenceController.getInt(KEY_DAY_NIGHT_MODE)==DAY_NIGHT_MODE_NIGHT);
    }
    public boolean isDayMode() {
        return (preferenceController.getInt(KEY_DAY_NIGHT_MODE)==DAY_NIGHT_MODE_DAY);
    }
    public boolean isAutoMode() {
        return (preferenceController.getInt(KEY_DAY_NIGHT_MODE)==DAY_NIGHT_MODE_AUTO);
    }
}
