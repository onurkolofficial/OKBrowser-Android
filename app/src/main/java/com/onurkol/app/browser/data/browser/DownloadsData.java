package com.onurkol.app.browser.data.browser;

public class DownloadsData {
    private String getDownloadFileName, getDownloadFolder, getDownloadDate;

    public DownloadsData(String downloadFileName, String downloadFolder, String downloadDate){
        getDownloadFileName=downloadFileName;
        getDownloadFolder=downloadFolder;
        getDownloadDate=downloadDate;
    }

    public String getFileName(){
        return getDownloadFileName;
    }
    public String getFolder(){
        return getDownloadFolder;
    }
    public String getDate() {
        return getDownloadDate;
    }

    public void setFileName(String newFileName){
        getDownloadFileName=newFileName;
    }
}
