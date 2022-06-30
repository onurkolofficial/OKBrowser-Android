package com.onurkol.app.browser.dialogs.tabs;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.snackbar.Snackbar;
import com.onurkol.app.browser.R;
import com.onurkol.app.browser.controller.tabs.UserWebCollectionController;
import com.onurkol.app.browser.libs.URLChecker;

public class DialogUserCollectionNewPage {
    public static Dialog getDialog(Fragment fragment) {
        TextView dialogAddEditCollectionWebTitle;
        Button dialogAddEditButton, dialogCancelButton, dialogDeleteButton;
        EditText dialogWebTitle, dialogWebURL;

        Context context=fragment.getContext();

        final Dialog collectionNewPageDialog = new Dialog(context);
        collectionNewPageDialog.setContentView(R.layout.screen_tab_web_collection_new_edit);
        collectionNewPageDialog.getWindow().setLayout(MATCH_PARENT, WRAP_CONTENT);

        dialogAddEditCollectionWebTitle=collectionNewPageDialog.findViewById(R.id.dialogAddEditCollectionWebTitle);
        dialogAddEditButton=collectionNewPageDialog.findViewById(R.id.dialogAddEditButton);
        dialogCancelButton=collectionNewPageDialog.findViewById(R.id.dialogCancelButton);
        dialogWebTitle=collectionNewPageDialog.findViewById(R.id.dialogWebTitle);
        dialogWebURL=collectionNewPageDialog.findViewById(R.id.dialogWebURL);
        dialogDeleteButton=collectionNewPageDialog.findViewById(R.id.dialogDeleteButton);

        dialogAddEditCollectionWebTitle.setText(context.getString(R.string.add_page_text));
        dialogAddEditButton.setText(context.getString(R.string.add_text));

        dialogDeleteButton.setVisibility(View.GONE);

        dialogCancelButton.setOnClickListener(v -> collectionNewPageDialog.dismiss());
        dialogAddEditButton.setOnClickListener(v -> {
            String title=dialogWebTitle.getText().toString();
            String url=dialogWebURL.getText().toString();
            if(title.equals("") || url.equals("")){
                Snackbar.make(fragment.getView().getRootView().findViewById(R.id.browserCoordinatorLayout),
                        context.getString(R.string.not_enter_page_url_collection_data_text),
                        Snackbar.LENGTH_LONG)
                        .setAction(context.getString(R.string.ok_text),sv -> {})
                        .show();
            }
            else {
                if(URLChecker.isURL(url))
                    UserWebCollectionController.getController().newWebCollection(title, url);
                else{
                    Snackbar.make(fragment.getView().getRootView().findViewById(R.id.browserCoordinatorLayout),
                                    context.getString(R.string.invalid_url_text),
                                    Snackbar.LENGTH_LONG)
                            .setAction(context.getString(R.string.ok_text),sv -> {})
                            .show();
                }
            }
            collectionNewPageDialog.dismiss();
            // Update GridView
            GridView gw=fragment.getView().findViewById(R.id.webCollectionGridView);
            gw.invalidateViews();
        });

        return collectionNewPageDialog;
    }
}
