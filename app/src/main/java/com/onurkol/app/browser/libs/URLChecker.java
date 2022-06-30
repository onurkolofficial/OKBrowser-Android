package com.onurkol.app.browser.libs;

import java.util.Locale;
import java.util.regex.Pattern;

public class URLChecker {
    private static final String urlRegex = "^((ftp|http|https|intent|file)?://)" // support scheme
            + "?(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?" // ftp的user@
            + "(([0-9]{1,3}\\.){3}[0-9]{1,3}"                            // IP形式的URL -> 199.194.52.184
            + "|"                                                        // 允许IP和DOMAIN（域名）
            + "(.)*"                                                     // 域名 -> www.
            + "([0-9a-z_!~*'()-]+\\.)*"                                  // 域名 -> www.
            + "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\\."                    // 二级域名
            + "[a-z]{2,6})"                                              // first level domain -> .com or .museum
            + "(:[0-9]{1,4})?"                                           // Port -> :80
            + "((/?)|"                                                   // a slash isn't required if there is no file name
            + "(/[0-9a-z_!~*'().;?:@&=+$,%#-]+)+/?)$";

    // Check URL Keywords
    public static final String URL_SCHEME_MAIL_TO = "mailto:";
    public static final String URL_SCHEME_VIEW_SOURCE = "view-source:";

    public static boolean isURL(String url) {
        // 1
        if (url == null)
            return false;
        // 2
        url = url.toLowerCase(Locale.getDefault());
        if (url.startsWith(URL_SCHEME_MAIL_TO) ||
                url.startsWith(URL_SCHEME_VIEW_SOURCE)) {
            return true;
        }
        // 3
        Pattern pattern = Pattern.compile(urlRegex);
        return pattern.matcher(url).matches();
    }

    public static boolean isDataImage(String url){
        return url.startsWith("data:");
    }

    public static String convertHttpUrl(String url) {
        String outUrl;
        if (url == null)
            return "";
        if(!url.contains("http") || !url.contains("https"))
            outUrl="https://"+url;
        else
            outUrl=url;

        return outUrl;
    }

    public static String convertViewSourceUrl(String url){
        String outUrl, convert=url.replace(URL_SCHEME_VIEW_SOURCE,"");
        if(!url.contains("http") || !url.contains("https"))
            outUrl="https://"+convert;
        else
            outUrl=convert;
        return URL_SCHEME_VIEW_SOURCE+outUrl;
    }
}
