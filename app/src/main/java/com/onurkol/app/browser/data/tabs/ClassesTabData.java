package com.onurkol.app.browser.data.tabs;

import android.graphics.Bitmap;

import com.onurkol.app.browser.fragments.tabs.TabFragment;

public class ClassesTabData {
    /**
     * Add another Object, Classes, Items etc...
     **/
    private Bitmap webPreview;
    private TabFragment webTabFragment;

    public ClassesTabData(TabFragment tabFragment, Bitmap tabPreview){
        webPreview=tabPreview;
        webTabFragment=tabFragment;
    }

    public Bitmap getTabPreview(){
        return webPreview;
    }
    public TabFragment getTabFragment(){
        return webTabFragment;
    }
}
