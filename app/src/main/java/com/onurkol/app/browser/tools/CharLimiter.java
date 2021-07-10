package com.onurkol.app.browser.tools;

public class CharLimiter {
    public static String Limit(String string, int Limit){
        return ((string!=null) ? ((string.length() > Limit) ? string.substring(0,Limit)+"..." : string) : "");
    }
    public static String Limit(String string, int Start, int Limit){
        return ((string!=null) ? ((string.length() > (Limit-Start)) ? string.substring(Start,Limit)+"..." : string) : "");
    }
}
