package com.onurkol.app.browser.lib.core;

import android.content.res.Configuration;
import android.content.res.Resources;

import com.onurkol.app.browser.lib.ContextManager;

import java.util.Locale;

public class LanguageManager {
    private static LanguageManager instance=null;

    // Constructor
    private LanguageManager(){}

    public static synchronized LanguageManager getInstance(){
        if(instance==null)
            instance=new LanguageManager();
        return instance;
    }

    public void setAppLanguage(int Language){
        // Language ids for : res/browser/values/app_languages.xml
        String languageCode=null;
        String languageCountry=null;
        if(Language==0) {
            languageCode=Locale.getDefault().getDisplayLanguage();
            languageCountry=Locale.getDefault().getCountry();
        }
        else if(Language==1) {
            languageCode="en";
            languageCountry="US";
        }
        else if(Language==2) {
            languageCode = "tr";
            languageCountry="TR";
        }

        Locale locale = new Locale(languageCode,languageCountry);
        Locale.setDefault(locale);
        Resources resources = ContextManager.getManager().getContextActivity().getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }
}
