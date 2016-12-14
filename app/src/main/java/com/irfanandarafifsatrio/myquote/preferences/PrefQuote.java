package com.irfanandarafifsatrio.myquote.preferences;

import android.content.Context;

import com.irfanandarafifsatrio.myquote.model.ListQuote;
import com.irfanandarafifsatrio.myquote.model.Quote;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by irfanandarafifsatrio on 9/26/16.
 *
 */

public class PrefQuote {
    public static final String PREF_NAME = PrefQuote.class.getName()+"_pref";
    public static final String PREF_VALUE = PrefQuote.class.getName()+"_value";

    public static void save(ListQuote data, Context ctx ){
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, PREF_NAME,0);
        complexPreferences.putObject(PREF_VALUE, data);
        complexPreferences.commit();
    }

    public static ListQuote load(Context ctx){
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, PREF_NAME, 0);
        return complexPreferences.getObject(PREF_VALUE,ListQuote.class);
    }

    public static String getJSON(Context ctx){
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, PREF_NAME,0);
        return complexPreferences.getJSON(PREF_VALUE);
    }

    public static void clear(Context ctx){
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, PREF_NAME, 0);
        complexPreferences.clearObject();
        complexPreferences.commit();
    }

    public static void addQuote(Context context, String quote) {
        ListQuote listQuote = PrefQuote.load(context);
        if (listQuote==null){
            List<Quote> mList = new ArrayList<>();
            mList.add(new Quote(quote,System.currentTimeMillis()/1000));
            listQuote = new ListQuote(mList);
        }else{
            listQuote.getQuoteList().add(new Quote(quote,System.currentTimeMillis()/1000));
        }
        PrefQuote.save(listQuote,context);
    }

    public static void edituote(Context context, int pos, String quote) {
        ListQuote listQuote = PrefQuote.load(context);
        if (listQuote==null){
            List<Quote> mList = new ArrayList<>();
            mList.add(new Quote(quote,System.currentTimeMillis()/1000));
            listQuote = new ListQuote(mList);
        }else{
            listQuote.getQuoteList().set(pos,new Quote(quote,System.currentTimeMillis()/1000));
        }
        PrefQuote.save(listQuote,context);
    }

    public static String getRandomQuote(Context context) {
        ListQuote listQuote = PrefQuote.load(context);
        if (listQuote != null && listQuote.getQuoteList().size() != 0) {
            int pos = new Random().nextInt(listQuote.getQuoteList().size());
            return listQuote.getQuoteList().get(pos).getText();
        }
        return null;
    }
}
