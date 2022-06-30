package com.onurkol.app.browser.controller.settings;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import com.onurkol.app.browser.interfaces.BrowserDataInterface;

import java.lang.ref.WeakReference;
import java.util.Locale;

public class LanguageController implements BrowserDataInterface {
    private static WeakReference<LanguageController> instance=null;

    private String LanguageName;

    private LanguageController(){}

    public static synchronized LanguageController getController(){
        if(instance==null || instance.get()==null)
            instance=new WeakReference<>(new LanguageController());
        return instance.get();
    }

    public void setLanguage(Context context, int languageCode){
        String langCode=null;
        String langCountry=null;
        switch (languageCode){
            case LANGUAGE_AUTO:
            default:
                Locale systemLocale=Resources.getSystem().getConfiguration().locale;
                langCode=systemLocale.getLanguage();
                langCountry=systemLocale.getCountry();
                break;
            case LANGUAGE_EN_US:
                langCode="en";
                langCountry="US";
                break;
            case LANGUAGE_TR_TR:
                langCode="tr";
                langCountry="TR";
                break;
        }
        LanguageName=langCode+"_"+langCountry;

        Locale locale = new Locale(langCode,langCountry);
        Locale.setDefault(locale);
        Resources resources=context.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }

    public String getLanguage(){
        return LanguageName;
    }
}
