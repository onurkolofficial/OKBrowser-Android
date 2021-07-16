package com.onurkol.app.browser.lib.settings;

import android.content.Context;

import com.onurkol.app.browser.R;
import com.onurkol.app.browser.lib.ContextManager;

import java.util.ArrayList;
import java.util.Arrays;

public class SearchEngine {
    private static SearchEngine instance=null;
    private String searchEngineName, searchEngineQuery;
    private int searchEngineValue;
    Context context;

    private SearchEngine(){
        context=ContextManager.getManager().getContext();
    }

    public static synchronized SearchEngine getInstance() {
        if(instance==null)
            instance=new SearchEngine();
        return instance;
    }

    public void setSearchEngine(int Index){
        searchEngineValue=Index;
        searchEngineName=getSearchEngineNameList().get(Index);
        searchEngineQuery=getSearchEngineQueryList().get(Index);
    }

    public ArrayList<String> getSearchEngineNameList() {
        return new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.search_engine_names)));
    }

    public ArrayList<String> getSearchEngineQueryList() {
        return new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.search_engine_queries)));
    }

    public ArrayList<Integer> getSearchEngineValueList() {
        return returnIntegerListHandler(R.array.search_engine_values);
    }

    private ArrayList<Integer> returnIntegerListHandler(int arrayName){
        int[] list=context.getResources().getIntArray(arrayName);
        // Return
        ArrayList<Integer> intValue=new ArrayList<>();
        for (int index : list)
            intValue.add(index);

        return intValue;
    }

    public String getSearchEngineName() {
        return searchEngineName;
    }

    public String getSearchEngineQuery() {
        return searchEngineQuery;
    }

    public int getSearchEngineValue() {
        return searchEngineValue;
    }

}
