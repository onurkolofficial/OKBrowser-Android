package com.onurkol.app.browser.controller;

import com.onurkol.app.browser.controller.browser.DownloadController;
import com.onurkol.app.browser.controller.browser.HistoryController;
import com.onurkol.app.browser.controller.tabs.RecentSearchController;
import com.onurkol.app.browser.controller.tabs.UserWebCollectionController;

public class ApplicationResetController {
    public static void clearAllData(){
        // Destroy All Data
        UserWebCollectionController.getController().deleteAllWebCollectionData();
        RecentSearchController.getController().deleteAllSearch();
        HistoryController.getController().deleteAllHistory();
        DownloadController.getController().deleteAllDownloads();
        PreferenceController.getController().clearAll();
    }
}
