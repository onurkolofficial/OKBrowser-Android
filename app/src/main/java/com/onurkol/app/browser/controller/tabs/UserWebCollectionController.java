package com.onurkol.app.browser.controller.tabs;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.onurkol.app.browser.controller.PreferenceController;
import com.onurkol.app.browser.data.tabs.TabDataPreference;
import com.onurkol.app.browser.data.tabs.UserWebCollectionData;
import com.onurkol.app.browser.interfaces.BrowserDataInterface;
import com.onurkol.app.browser.interfaces.tabs.UserWebCollectionControllerInterface;
import com.onurkol.app.browser.libs.ListToJson;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class UserWebCollectionController implements UserWebCollectionControllerInterface, BrowserDataInterface {
    private static WeakReference<UserWebCollectionController> instance=null;
    PreferenceController preferenceController;

    Gson gson=new Gson();

    private UserWebCollectionController(){
        preferenceController=PreferenceController.getController();
    }

    public static synchronized UserWebCollectionController getController(){
        if(instance==null || instance.get()==null)
            instance=new WeakReference<>(new UserWebCollectionController());
        return instance.get();
    }

    @Override
    public void newWebCollection(@NonNull String webTitle, @NonNull String webUrl) {
        UserWebCollectionData data = new UserWebCollectionData(webTitle, webUrl);
        TAB_USER_WEB_COLLECTION.add(data);
        saveCollectionDataPreference();
    }

    @Override
    public void updateWebCollection(int position, UserWebCollectionData updatedWebCollectionData) {
        TAB_USER_WEB_COLLECTION.set(position, updatedWebCollectionData);
        saveCollectionDataPreference();
    }

    @Override
    public void deleteWebCollection(int position) {
        TAB_USER_WEB_COLLECTION.remove(position);
        saveCollectionDataPreference();
    }

    @Override
    public void deleteAllWebCollectionData() {
        TAB_USER_WEB_COLLECTION.clear();
        saveCollectionDataPreference();
    }

    @Override
    public void syncCollectionData() {
        String collectionPreferenceList=preferenceController.getString(KEY_TAB_USER_WEB_COLLECTION);
        if(collectionPreferenceList!=null && !collectionPreferenceList.equals(COLLECTION_NO_DATA)) {
            ArrayList<UserWebCollectionData> getPreferenceCollectionData = new ArrayList<>(
                    gson.fromJson(collectionPreferenceList, new TypeToken<ArrayList<UserWebCollectionData>>() {
                    }.getType()));
            TAB_USER_WEB_COLLECTION.clear();
            TAB_USER_WEB_COLLECTION.addAll(getPreferenceCollectionData);
        }
    }

    @Override
    public void saveCollectionDataPreference() {
        if(TAB_USER_WEB_COLLECTION.size()<=0) {
            preferenceController.setPreference(KEY_TAB_USER_WEB_COLLECTION, COLLECTION_NO_DATA);
        }
        else{
            String newData=ListToJson.getJson(TAB_USER_WEB_COLLECTION);
            preferenceController.setPreference(KEY_TAB_USER_WEB_COLLECTION, newData);
        }
    }

    @Override
    public ArrayList<UserWebCollectionData> getWebCollectionList() {
        return TAB_USER_WEB_COLLECTION;
    }
}
