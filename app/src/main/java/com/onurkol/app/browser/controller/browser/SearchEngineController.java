package com.onurkol.app.browser.controller.browser;

import android.content.Context;

import com.onurkol.app.browser.controller.PreferenceController;
import com.onurkol.app.browser.data.settings.xml.SearchEngineXMLToList;
import com.onurkol.app.browser.interfaces.BrowserDataInterface;

import java.lang.ref.WeakReference;

public class SearchEngineController implements BrowserDataInterface {
    private static WeakReference<SearchEngineController> instance=null;
    PreferenceController preferenceController;
    Context mContext;

    int CURRENT_SEARCH_ENGINE;

    private SearchEngineController(Context context){
        preferenceController=PreferenceController.getController();
        CURRENT_SEARCH_ENGINE=preferenceController.getInt(KEY_SEARCH_ENGINE);
        mContext=context;
    }

    public static synchronized SearchEngineController getController(Context context){
        if(instance==null || instance.get()==null)
            instance=new WeakReference<>(new SearchEngineController(context));
        return instance.get();
    }

    public void syncSearchEngineData(){
        CURRENT_SEARCH_ENGINE=preferenceController.getInt(KEY_SEARCH_ENGINE);
    }

    public String getCurrentSearchEngineName() {
        return SearchEngineXMLToList.getSearchEngineNameList(mContext).get(CURRENT_SEARCH_ENGINE);
    }

    public int getCurrentSearchEngineValue() {
        return SearchEngineXMLToList.getSearchEngineValueList(mContext).get(CURRENT_SEARCH_ENGINE);
    }

    public String getCurrentSearchEngineQuery() {
        return SearchEngineXMLToList.getSearchEngineQueryList(mContext).get(CURRENT_SEARCH_ENGINE);
    }
}
