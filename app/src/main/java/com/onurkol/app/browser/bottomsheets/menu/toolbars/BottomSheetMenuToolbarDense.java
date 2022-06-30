package com.onurkol.app.browser.bottomsheets.menu.toolbars;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.onurkol.app.browser.R;
import com.onurkol.app.browser.controller.gui.CommonMenuMainController;

public class BottomSheetMenuToolbarDense {
    static LayoutInflater inflater;

    public static BottomSheetDialog getMenuBottomSheet(Context context){
        BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(context);

        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.bottomsheet_toolbar_main_dense, null);

        bottomSheetDialog.setContentView(view);

        CommonMenuMainController.loadMenuController(context, view, bottomSheetDialog);

        return bottomSheetDialog;
    }
}
