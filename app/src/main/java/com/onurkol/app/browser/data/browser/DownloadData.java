package com.onurkol.app.browser.data.browser;

public class DownloadData {
    private String getDownloadFileName;
    private String getDownloadFolder;
    private String getDownloadDate;

    public DownloadData(String downloadFileName, String downloadFolder, String downloadDate){
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
