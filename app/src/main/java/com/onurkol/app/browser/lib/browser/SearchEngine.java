package com.onurkol.app.browser.lib.browser;

import android.content.Context;

import com.onurkol.app.browser.R;
import com.onurkol.app.browser.lib.ContextManager;

import java.util.Arrays;
import java.util.List;

public class SearchEngine {
    private static SearchEngine instance=null;
    private String searchEngineName, searchEngineQuery;
    private int searchEngineIndex;
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
        searchEngineIndex=Index;
        searchEngineName=getSearchEngineNameList().get(Index);
        searchEngineQuery=getSearchEngineQueryList().get(Index);
    }

    public List<String> getSearchEngineNameList() {
        return Arrays.asList(context.getResources().getStringArray(R.array.search_engine_names));
    }

    public List<String> getSearchEngineQueryList() {
        return Arrays.asList(context.getResources().getStringArray(R.array.search_engine_queries));
    }

    public List<int[]> getListSearchEngineIndex() {
        return Arrays.asList(context.getResources().getIntArray(R.array.search_engine_index));
    }

    public String getSearchEngineName() {
        return searchEngineName;
    }

    public String getSearchEngineQuery() {
        return searchEngineQuery;
    }

    public int getSearchEngineIndex() {
        return searchEngineIndex;
    }
}
