package com.irfanandarafifsatrio.myquote.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

/**
 * Created by skday on 9/26/16.
 */

public class ComplexPreferences {
    private static final String TAG = "ComplexPreferences";
    private static Gson GSON = new Gson();
    private static ComplexPreferences complexPreferences;
    //    Type typeOfObject = new TypeToken<Object>() {}.getType();
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private ComplexPreferences(Context context, String namePreferences, int mode) {
        if (namePreferences == null || namePreferences.equals("")) {
            namePreferences = "complex_preferences";
        }
        if (context == null) {
            Log.i(TAG, "context is null");
        } else {
            preferences = context.getSharedPreferences(namePreferences, mode);
            editor = preferences.edit();
        }
    }

    public static ComplexPreferences getComplexPreferences(Context context, String namePreferences, int mode) {

//        if (complexPreferences == null) {
        complexPreferences = new ComplexPreferences(context, namePreferences, mode);
//        }

        return complexPreferences;
    }

    public void putObject(String key, Object object) {
        if (object == null) {
            throw new IllegalArgumentException("object is null");
        }

        if (key.equals("")) {
            throw new IllegalArgumentException("key is empty or null");
        }

        editor.putString(key, GSON.toJson(object));
    }

    public void commit() {
        editor.commit();
    }

    public void clearObject() {
        editor.clear();
    }

    public <T> T getObject(String key, Class<T> a) {

        String gson = preferences.getString(key, null);
        if (gson == null) {
            return null;
        } else {
            try {
                return GSON.fromJson(gson, a);
            } catch (Exception e) {
                throw new IllegalArgumentException("Object storaged with key " + key + " is instanceof other class");
            }
        }
    }

    public String getJSON(String key) {

        String gson = preferences.getString(key, null);
        if (gson == null) {
            return null;
        } else {
            return gson;
        }
    }
}