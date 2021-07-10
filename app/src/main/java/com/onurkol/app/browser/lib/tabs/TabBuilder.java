package com.onurkol.app.browser.lib.tabs;

public class TabBuilder extends TabManager {
    private static TabBuilder instance;

    private TabBuilder(){}

    public static synchronized TabBuilder Build(){
        if(instance==null)
            instance=new TabBuilder();
        return instance;
    }
}
