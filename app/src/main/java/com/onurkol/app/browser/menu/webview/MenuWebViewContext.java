package com.onurkol.app.browser.menu.webview;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.onurkol.app.browser.R;
import com.onurkol.app.browser.data.browser.DownloadsData;
import com.onurkol.app.browser.interfaces.BrowserDefaultSettings;
import com.onurkol.app.browser.lib.ContextManager;
import com.onurkol.app.browser.lib.browser.downloads.DownloadsHelper;
import com.onurkol.app.browser.lib.browser.downloads.DownloadsManager;
import com.onurkol.app.browser.lib.browser.tabs.TabBuilder;
import com.onurkol.app.browser.lib.browser.tabs.core.ToolbarTabCounter;
import com.onurkol.app.browser.lib.core.PermissionManager;
import com.onurkol.app.browser.tools.CharLimiter;
import com.onurkol.app.browser.tools.DateManager;
import com.onurkol.app.browser.tools.URLChecker;
import com.onurkol.app.browser.tools.JavascriptManager;
import com.onurkol.app.browser.webview.OKWebView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.WeakReference;

import static android.content.Context.DOWNLOAD_SERVICE;

public class MenuWebViewContext {
    public static String KEY_MENU_IMAGE="BROWSER_MENU_IMAGE",
            KEY_MENU_ANCHOR="BROWSER_MENU_ANCHOR",
            KEY_MENU_IMAGE_ANCHOR="BROWSER_MENU_IMAGE_ANCHOR";

    // Get Image Menu
    public static synchronized PopupWindow getImageContextMenu() {
        return WebContextMenuClass(KEY_MENU_IMAGE, null, "", "");
    }
    public static synchronized PopupWindow getImageContextMenu(String imageUrl) {
        return WebContextMenuClass(KEY_MENU_IMAGE, null, imageUrl, "");
    }
    public static synchronized PopupWindow getImageContextMenu(String imageUrl, String title) {
        return WebContextMenuClass(KEY_MENU_IMAGE, null, imageUrl, title);
    }

    // Get Anchor Context Menu
    public static synchronized PopupWindow getAnchorContextMenu() {
        return WebContextMenuClass(KEY_MENU_ANCHOR, "", null, "");
    }
    public static synchronized PopupWindow getAnchorContextMenu(String url) {
        return WebContextMenuClass(KEY_MENU_ANCHOR, url, null, "");
    }
    public static synchronized PopupWindow getAnchorContextMenu(String url, String title) {
        return WebContextMenuClass(KEY_MENU_ANCHOR, url, null, title);
    }

    // Get Image Anchor Context Menu
    public static synchronized PopupWindow getImageAnchorContextMenu() {
        return WebContextMenuClass(KEY_MENU_IMAGE_ANCHOR, "", null, "");
    }
    public static synchronized PopupWindow getImageAnchorContextMenu(String webUrl) {
        return WebContextMenuClass(KEY_MENU_IMAGE_ANCHOR, webUrl, null, "");
    }
    public static synchronized PopupWindow getImageAnchorContextMenu(String webUrl, String imageUrl) {
        return WebContextMenuClass(KEY_MENU_IMAGE_ANCHOR, webUrl, imageUrl, "");
    }
    public static synchronized PopupWindow getImageAnchorContextMenu(String webUrl, String imageUrl, String title) {
        return WebContextMenuClass(KEY_MENU_IMAGE_ANCHOR, webUrl, imageUrl, title);
    }

    // Classes
    static WeakReference<TabBuilder> tabBuilderStatic;
    // Services
    static ClipboardManager clipboard = (ClipboardManager) ContextManager.getManager().getContext().getSystemService(Context.CLIPBOARD_SERVICE);
    // Variables
    static String getLink, getImageLink, getLinkText;

    // Popup Window
    static PopupWindow popupWindow;

    // Main Class
    static PopupWindow WebContextMenuClass(String type, String url, String imageUrl, String linkText) {
        Context context = ContextManager.getManager().getContext();
        // Set Popup Window
        popupWindow = new PopupWindow(context);
        // Layout Inflater
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        TextView showWebUrl;
        LinearLayout openNewTabLayoutButton = null,
                openIncognitoTabLayoutButton = null,
                imageOpenLayoutButton = null,
                imageOpenNewTabLayoutButton = null,
                imageOpenIncognitoLayoutButton = null,
                copyLinkLayoutButton = null,
                copyImageLinkLayoutButton = null,
                copyLinkTextLayoutButton = null,
                imageDownloadLayoutButton = null;

        // Get Classes
        tabBuilderStatic=new WeakReference<>(TabBuilder.Build());

        // Set Values
        getLink = url;
        getLinkText = linkText;
        getImageLink = imageUrl;

        // Convert URL
        String convertLink = CharLimiter.Limit(url, 55);
        String convertImageLink = CharLimiter.Limit(imageUrl, 55);

        // Inflating Menu Layouts
        if(type.equals(KEY_MENU_IMAGE)){
            // Image Context Menu
            View view = inflater.inflate(R.layout.menu_webview_image, null);
            // Set Popup View
            popupWindow.setContentView(view);

            // Get Elements
            showWebUrl=view.findViewById(R.id.showWebUrl);
            imageOpenLayoutButton=view.findViewById(R.id.imageOpenLayoutButton);
            imageOpenNewTabLayoutButton=view.findViewById(R.id.imageOpenNewTabLayoutButton);
            imageOpenIncognitoLayoutButton=view.findViewById(R.id.imageOpenIncognitoLayoutButton);
            imageDownloadLayoutButton=view.findViewById(R.id.imageDownloadLayoutButton);
            copyImageLinkLayoutButton=view.findViewById(R.id.imageCopyLinkLayoutButton);

            // Set Text
            showWebUrl.setText(convertImageLink);
        }
        else if(type.equals(KEY_MENU_ANCHOR)){
            // Image Context Menu
            View view = inflater.inflate(R.layout.menu_webview_anchor, null);
            // Set Popup View
            popupWindow.setContentView(view);

            // Get Elements
            showWebUrl=view.findViewById(R.id.showWebUrl);
            openNewTabLayoutButton=view.findViewById(R.id.anchorOpenNewTabLayoutButton);
            openIncognitoTabLayoutButton=view.findViewById(R.id.anchorOpenIncognitoTabLayoutButton);
            copyLinkLayoutButton=view.findViewById(R.id.anchorCopyLinkLayoutButton);
            copyLinkTextLayoutButton=view.findViewById(R.id.anchorCopyLinkTextLayoutButton);

            // Set Text
            showWebUrl.setText(convertLink);
        }
        else if(type.equals(KEY_MENU_IMAGE_ANCHOR)){
            // Image Context Menu
            View view = inflater.inflate(R.layout.menu_webview_image_anchor, null);
            // Set Popup View
            popupWindow.setContentView(view);

            // Get Elements
            showWebUrl=view.findViewById(R.id.showWebUrl);
            openNewTabLayoutButton=view.findViewById(R.id.imageAnchorOpenNewTabLayoutButton);
            openIncognitoTabLayoutButton=view.findViewById(R.id.imageAnchorOpenIncognitoTabLayoutButton);
            imageOpenLayoutButton=view.findViewById(R.id.imageAnchorOpenImageLayoutButton);
            imageDownloadLayoutButton=view.findViewById(R.id.imageAnchorDownloadLayoutButton);
            copyLinkLayoutButton=view.findViewById(R.id.imageAnchorCopyLinkLayoutButton);
            copyLinkTextLayoutButton=view.findViewById(R.id.imageAnchorCopyLinkTextLayoutButton);

            // Set Text
            showWebUrl.setText(convertLink);
        }

        // Set Listeners
        if(imageOpenLayoutButton!=null)
            imageOpenLayoutButton.setOnClickListener(openImageListener);
        if(imageOpenNewTabLayoutButton!=null)
            imageOpenNewTabLayoutButton.setOnClickListener(openNewTabImageListener);
        if(imageOpenIncognitoLayoutButton!=null)
            imageOpenIncognitoLayoutButton.setOnClickListener(openIncognitoTabImageListener);
        if(imageDownloadLayoutButton!=null)
            imageDownloadLayoutButton.setOnClickListener(downloadImageListener);
        if(openNewTabLayoutButton!=null)
            openNewTabLayoutButton.setOnClickListener(openNewTabListener);
        if(openIncognitoTabLayoutButton!=null)
            openIncognitoTabLayoutButton.setOnClickListener(openNewIncognitoTabListener);
        if(copyImageLinkLayoutButton!=null)
            copyImageLinkLayoutButton.setOnClickListener(copyImageLinkListener);
        if(copyLinkLayoutButton!=null)
            copyLinkLayoutButton.setOnClickListener(copyLinkListener);
        if(copyLinkTextLayoutButton!=null)
            copyLinkTextLayoutButton.setOnClickListener(copyLinkTextListener);

        // Set Popup Window Settings
        popupWindow.setFocusable(true);
        popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setElevation(20);
        popupWindow.setOnDismissListener(dismissListenerContextMenu);

        return popupWindow;
    }

    // Listeners
    static PopupWindow.OnDismissListener dismissListenerContextMenu=() -> {
        // Reset Background
        JavascriptManager.getManager().getWebView().getRootView().setAlpha(1);
    };
    private final static View.OnClickListener openNewTabListener=view -> {
        // Create New Tab
        tabBuilderStatic.get().createNewTab(getLink);
        // Get Activity
        Activity activity=ContextManager.getManager().getContextActivity();
        // Hide Incognito Icon
        ((ImageView)activity.findViewById(R.id.incognitoIcon)).setVisibility(View.GONE);
        // Update Tab Count
        ((ImageButton)activity.findViewById(R.id.browserTabListButton)).setImageDrawable(new ToolbarTabCounter().getTabCountDrawable());

        // Recreate Index
        tabBuilderStatic.get().recreateTabIndex();
        // Dismiss Popup
        popupWindow.dismiss();
    };
    private final static View.OnClickListener openNewIncognitoTabListener=view -> {
        // Create New Tab
        tabBuilderStatic.get().createNewIncognitoTab(getLink);
        // Get Activity
        Activity activity=ContextManager.getManager().getContextActivity();
        // Show Incognito Icon
        ((ImageView)activity.findViewById(R.id.incognitoIcon)).setVisibility(View.VISIBLE);
        // Update Tab Count
        ((ImageButton)activity.findViewById(R.id.browserTabListButton)).setImageDrawable(new ToolbarTabCounter().getIncognitoTabCountDrawable());

        // Recreate Index
        tabBuilderStatic.get().recreateIncognitoTabIndex();
        // Dismiss Popup
        popupWindow.dismiss();
    };
    private final static View.OnClickListener copyLinkListener=view -> {
        // Copy Data
        ClipData clip = ClipData.newPlainText("copy_link", getLink);
        clipboard.setPrimaryClip(clip);
        // Dismiss Popup
        popupWindow.dismiss();
    };
    private final static View.OnClickListener copyImageLinkListener=view -> {
        // Copy Data
        ClipData clip = ClipData.newPlainText("copy_link", getImageLink);
        clipboard.setPrimaryClip(clip);
        // Dismiss Popup
        popupWindow.dismiss();
    };
    private final static View.OnClickListener copyLinkTextListener=view -> {
        // Copy Data
        ClipData clip = ClipData.newPlainText("copy_link_text", getLinkText);
        clipboard.setPrimaryClip(clip);
        // Dismiss Popup
        popupWindow.dismiss();
    };
    private final static View.OnClickListener openImageListener=view -> {
        // Get Classes
        TabBuilder tabBuilder=tabBuilderStatic.get();
        // Get WebView
        OKWebView webView;
        if(tabBuilder.getActiveTabFragment()!=null)
            webView=tabBuilder.getActiveTabFragment().getWebView();
        else
            webView=tabBuilder.getActiveIncognitoFragment().getWebView();

        // Load Url
        webView.loadUrl(getImageLink);
        // Dismiss Popup
        popupWindow.dismiss();
    };
    private final static View.OnClickListener openNewTabImageListener=view -> {
        // Create New Tab
        tabBuilderStatic.get().createNewTab(getImageLink);
        // Get Activity
        Activity activity=ContextManager.getManager().getContextActivity();
        // Hide Incognito Icon
        ((ImageView)activity.findViewById(R.id.incognitoIcon)).setVisibility(View.GONE);
        // Update Tab Count
        ((ImageButton)activity.findViewById(R.id.browserTabListButton)).setImageDrawable(new ToolbarTabCounter().getTabCountDrawable());

        // Recreate Index
        tabBuilderStatic.get().recreateTabIndex();
        // Dismiss Popup
        popupWindow.dismiss();
    };
    private final static View.OnClickListener openIncognitoTabImageListener=view -> {
        // Create New Tab
        tabBuilderStatic.get().createNewIncognitoTab(getImageLink);
        // Get Activity
        Activity activity=ContextManager.getManager().getContextActivity();
        // Show Incognito Icon
        ((ImageView)activity.findViewById(R.id.incognitoIcon)).setVisibility(View.VISIBLE);
        // Update Tab Count
        ((ImageButton)activity.findViewById(R.id.browserTabListButton)).setImageDrawable(new ToolbarTabCounter().getIncognitoTabCountDrawable());

        // Recreate Index
        tabBuilderStatic.get().recreateIncognitoTabIndex();
        // Dismiss Popup
        popupWindow.dismiss();
    };

    // Get Download Manager
    private final static View.OnClickListener downloadImageListener=view -> {
        // Get Context
        Context context=ContextManager.getManager().getContext();
        // Get Classes
        PermissionManager permissionManager=PermissionManager.getInstance();
        DownloadManager downloadManager = (DownloadManager)ContextManager.getManager().getContext().getSystemService(DOWNLOAD_SERVICE);

        if(permissionManager.getStoragePermission()){
            // Get Download Folder
            String downloadFolder= BrowserDefaultSettings.BROWSER_DOWNLOAD_FOLDER;
            String downloadFolderApi30Up= BrowserDefaultSettings.BROWSER_DOWNLOAD_FOLDER_API30_UP;
            String dataDownloadFolder="";
            String storageFolder=BrowserDefaultSettings.BROWSER_STORAGE_FOLDER;
            // Get Download Date
            String downloadDate=DateManager.getDate();

            // Check Folder
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.Q)
                dataDownloadFolder=downloadFolderApi30Up;
            else
                dataDownloadFolder=downloadFolder;

            // Check Image Url
            if(URLChecker.isDataImage(getImageLink)){
                // Save Image
                File path = new File(storageFolder+"/"+dataDownloadFolder);
                String filetype = getImageLink.substring(getImageLink.indexOf("/") + 1, getImageLink.indexOf(";"));
                String filename = System.currentTimeMillis() + "." + filetype;
                File file = new File(path, filename);
                try {
                    if(!path.exists())
                        path.mkdirs();
                    if(!file.exists())
                        file.createNewFile();

                    String base64EncodedString = getImageLink.substring(getImageLink.indexOf(",") + 1);
                    byte[] decodedBytes = Base64.decode(base64EncodedString, Base64.DEFAULT);
                    OutputStream os = new FileOutputStream(file);
                    os.write(decodedBytes);
                    os.close();

                    //Tell the media scanner about the new file so that it is immediately available to the user.
                    MediaScannerConnection.scanFile(context,
                            new String[]{file.toString()}, null,
                            (path1, uri) -> {});

                    // Add Download Data (Because not enqueue download manager)
                    DownloadsManager.getInstance().newDownload(new DownloadsData(file.getName(), dataDownloadFolder, downloadDate));
                    /*
                    //Set notification after download complete and add "click to view" action to that
                    String mimetype = getImageLink.substring(getImageLink.indexOf(":") + 1, getImageLink.indexOf("/"));
                    Intent intent = new Intent();
                    intent.setAction(android.content.Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(file), (mimetype + "/*"));
                    PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, 0);

                    Notification notification = new Notification.Builder(context)
                            .setSmallIcon(android.R.drawable.ic_menu_save)
                            .setContentText(context.getString(R.string.download_image_text))
                            .setContentTitle(filename)
                            .setContentIntent(pIntent)
                            .build();

                    notification.flags |= Notification.FLAG_AUTO_CANCEL;
                    int notificationId = 85851;
                    NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(notificationId, notification);
                     */
                } catch (IOException e) {}
            }
            else{
                // Download Image
                Uri fileUri=Uri.parse(getImageLink);
                File file=new File(String.valueOf(fileUri));
                // Set Download Manager Request
                DownloadManager.Request request = new DownloadManager.Request(fileUri);
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                // Get File Name
                String fileName=file.getName();
                // Set Folder
                request.setDestinationInExternalPublicDir(dataDownloadFolder,fileName);

                // Create Data
                DownloadsHelper.downloadsData=new DownloadsData(fileName, downloadFolder, downloadDate);
                // Enqueue
                downloadManager.enqueue(request);
            }
            // Show Message
            Toast.makeText(context,context.getString(R.string.downloading_image_text), Toast.LENGTH_LONG).show();
        }
        else{
            // Request Permission
            permissionManager.setStoragePermission();
        }

        // Dismiss Popup
        popupWindow.dismiss();
    };
}
