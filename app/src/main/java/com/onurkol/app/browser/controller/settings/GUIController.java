package com.onurkol.app.browser.controller.settings;

import com.onurkol.app.browser.controller.PreferenceController;
import com.onurkol.app.browser.interfaces.BrowserDataInterface;

import java.lang.ref.WeakReference;

public class GUIController implements BrowserDataInterface {
    private static WeakReference<GUIController> instance=null;

    PreferenceController preferenceController;

    private GUIController(){
        preferenceController=PreferenceController.getController();
    }

    public static synchronized GUIController getController(){
        if(instance==null || instance.get()==null)
            instance=new WeakReference<>(new GUIController());
        return instance.get();
    }

    public boolean isSimpleMode(){
        return (preferenceController.getInt(KEY_GUI_MODE)==GUI_MODE_SIMPLE);
    }

    public boolean isDenseMode(){
        return (preferenceController.getInt(KEY_GUI_MODE)==GUI_MODE_DENSE);
    }

}
