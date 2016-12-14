package com.irfanandarafifsatrio.myquote.utils;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.irfanandarafifsatrio.myquote.preferences.PrefQuote;

/**
 * Created by irfanandarafifsatrio on 12/14/16.
 *
 */
public class AlarmReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String quote = PrefQuote.getRandomQuote(context);
        if (quote!=null)
            NotificationHelper.sendNotification(context,quote);
    }
}
