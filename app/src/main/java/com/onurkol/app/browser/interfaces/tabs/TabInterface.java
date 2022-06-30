package com.onurkol.app.browser.interfaces.tabs;


import androidx.annotation.NonNull;

import com.onurkol.app.browser.webview.OKWebView;

public interface TabInterface {
    int UI_SEARCH_STATE=20002,
            UI_DEFAULT_STATE=20000,
            UI_SEARCH_BREAK_STATE=20001,
            UI_ERROR_STATE=20003;

    int MENU_UI_CAN_BACK_STATE=16000,
            MENU_UI_CAN_BACK_NO_FORWARD_STATE=16001,
            MENU_UI_CAN_FORWARD_BACK_STATE=16002,
            MENU_UI_CAN_FORWARD_STATE=16003,
            MENU_UI_CAN_FORWARD_NO_BACK_STATE=16004,
            MENU_UI_DEFAULT_STATE=16005;

    String DESKTOP_USER_AGENT="Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.104 Safari/537.36";

    int CONTEXT_MENU_ANCHOR_MODE=16200,
            CONTEXT_MENU_IMAGE_MODE=16201,
            CONTEXT_MENU_ANCHOR_AND_IMAGE_MODE=16202;

    void onStartWeb(@NonNull String searchOrUrl);
    void goBack();
    void goForward();
    void goHome();
    int getBackForwardState(); // for toolbar/menu button.
    void setBackForwardState(int stateId);
    void setMenuUIStateUpdate();
    void restoreMenuUIState();
    void restoreMenuUIState(int getState);

    void setTabIndex(int newTabIndex);

    void setIncognito(boolean tabIncognito);
    boolean isIncognito();

    void setTabHome(boolean tabIsHomePage);
    boolean isTabHome();

    void setDesktopMode(boolean desktopMode);
    boolean isDesktopMode();

    void setTabError(boolean tabIsErrorPage);
    boolean isTabError();

    void setUIStateSearch();
    void setUIStateSearchBreak();
    void setUIStateDefault();
    void setUIStateError();
    int getUIState();

    @NonNull OKWebView getWebView();
}
