package com.irfanandarafifsatrio.myquote.utils;

import android.util.Log;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by irfanandarafifsatrio on 11/10/16.
 *
 */

public class StringHelper {

    public static String getDate(long date) {
        Date updatedate = new Date(date * 1000);
        SimpleDateFormat format = new SimpleDateFormat("dd MMM");
        return format.format(updatedate);
    }

    public static String getTime(long date) {
        Date updatedate = new Date(date * 1000);
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(updatedate);
    }
}
