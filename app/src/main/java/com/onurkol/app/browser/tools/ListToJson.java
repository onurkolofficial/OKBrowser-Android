package com.onurkol.app.browser.tools;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.onurkol.app.browser.data.browser.tabs.TabData;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ListToJson {
    static final Gson gson=new Gson();

    public static String getJson(ArrayList list){
        int listSize=list.size();
        // Variables
        String newData;
        if(listSize>0) {
            // Added New Data
            StringBuilder processData = new StringBuilder();
            for (int i = 0; i < listSize; i++) {
                if (i == (listSize - 1))
                    processData.append(gson.toJson(list.get(i)));
                else
                    processData.append(gson.toJson(list.get(i))).append(",");
            }
            newData = "[" + processData + "]";
        }
        else
            // Clear All Tabs
            newData="";
        return newData;
    }

    public static ArrayList getArrayList(String json, Type dataType){
        // New Data
        ArrayList getSavedList=new ArrayList<>();
        // Get Data
        if(!json.equals(""))
            // Convert String to List
            getSavedList=gson.fromJson(json, dataType);
        return getSavedList;
    }

    public static Collection getCollection(String json, Type dataType){
        // New Data
        Collection getSavedList=new ArrayList<>();
        // Get Data
        if(!json.equals(""))
            // Convert String to List
            getSavedList=gson.fromJson(json, dataType);
        return getSavedList;
    }
}
