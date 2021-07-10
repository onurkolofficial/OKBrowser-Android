package com.onurkol.app.browser.data.tabs;

import android.graphics.Bitmap;

public class ClassesTabData {
    /**
     * Add another Object, Classes, Items etc...
     **/
    private Bitmap webPreview;

    public ClassesTabData(Bitmap tabPreview){
        webPreview=tabPreview;
    }

    public Bitmap getTabPreview(){
        return webPreview;
    }
}
