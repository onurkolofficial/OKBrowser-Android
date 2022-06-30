package com.onurkol.app.browser.dialogs.tabs;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.onurkol.app.browser.R;
import com.onurkol.app.browser.controller.tabs.UserWebCollectionController;
import com.onurkol.app.browser.data.tabs.UserWebCollectionData;
import com.onurkol.app.browser.libs.URLChecker;

public class DialogUserCollectionEditPage {
    public static Dialog getDialog(Fragment fragment, int position) {
        TextView dialogAddEditCollectionWebTitle;
        Button dialogAddEditButton, dialogCancelButton, dialogDeleteButton;
        EditText dialogWebTitle, dialogWebURL;

        UserWebCollectionController webCollectionController=UserWebCollectionController.getController();

        Context context=fragment.getContext();

        final Dialog collectionEditPageDialog = new Dialog(context);
        collectionEditPageDialog.setContentView(R.layout.screen_tab_web_collection_new_edit);
        collectionEditPageDialog.getWindow().setLayout(MATCH_PARENT, WRAP_CONTENT);

        dialogAddEditCollectionWebTitle=collectionEditPageDialog.findViewById(R.id.dialogAddEditCollectionWebTitle);
        dialogAddEditButton=collectionEditPageDialog.findViewById(R.id.dialogAddEditButton);
        dialogCancelButton=collectionEditPageDialog.findViewById(R.id.dialogCancelButton);
        dialogWebTitle=collectionEditPageDialog.findViewById(R.id.dialogWebTitle);
        dialogWebURL=collectionEditPageDialog.findViewById(R.id.dialogWebURL);
        dialogDeleteButton=collectionEditPageDialog.findViewById(R.id.dialogDeleteButton);

        dialogAddEditCollectionWebTitle.setText(context.getString(R.string.edit_page_text));
        dialogAddEditButton.setText(context.getString(R.string.edit_text));

        // Get Data
        UserWebCollectionData data=webCollectionController.getWebCollectionList().get(position);

        dialogWebTitle.setText(data.getTitle());
        dialogWebURL.setText(data.getUrl());

        dialogCancelButton.setOnClickListener(v -> collectionEditPageDialog.dismiss());
        dialogAddEditButton.setOnClickListener(v -> {
            String title=dialogWebTitle.getText().toString();
            String url=dialogWebURL.getText().toString();
            if(title.equals("") || url.equals("")){
                Snackbar.make(fragment.getView().getRootView().findViewById(R.id.browserCoordinatorLayout),
                                context.getString(R.string.not_enter_page_url_collection_data_text),
                                Snackbar.LENGTH_LONG)
                        .setAction(context.getString(R.string.ok_text),sv -> {})
                        .show();
                collectionEditPageDialog.dismiss();
            }
            else{
                if(URLChecker.isURL(url)){
                    data.setTitle(title);
                    data.setUrl(url);
                    webCollectionController.updateWebCollection(position, data);
                }
                else{
                    Snackbar.make(fragment.getView().getRootView().findViewById(R.id.browserCoordinatorLayout),
                                    context.getString(R.string.invalid_url_text),
                                    Snackbar.LENGTH_LONG)
                            .setAction(context.getString(R.string.ok_text),sv -> {})
                            .show();
                }
                collectionEditPageDialog.dismiss();
                // Update GridView
                GridView gw=fragment.getView().findViewById(R.id.webCollectionGridView);
                gw.invalidateViews();
            }
        });
        dialogDeleteButton.setOnClickListener(v -> {
            webCollectionController.deleteWebCollection(position);
            collectionEditPageDialog.dismiss();
            // Update GridView
            GridView gw=fragment.getView().findViewById(R.id.webCollectionGridView);
            gw.invalidateViews();
        });

        return collectionEditPageDialog;
    }
}
