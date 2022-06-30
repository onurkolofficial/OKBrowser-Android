package com.onurkol.app.browser.libs;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateManager {
    public static String getDate(){
        // Get Date
        Date currentDate = Calendar.getInstance().getTime();
        // Convert Format
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return df.format(currentDate);
    }
}
