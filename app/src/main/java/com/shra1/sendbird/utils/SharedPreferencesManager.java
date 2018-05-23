package com.shra1.sendbird.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Shrawan WABLE on 3/20/2018.
 */

public class SharedPreferencesManager {
    public static SharedPreferencesManager instance = null;
    Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public SharedPreferencesManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(
                context.getPackageName() + "." + getClass().getName(),
                Activity.MODE_PRIVATE);
    }

    public static SharedPreferencesManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPreferencesManager(context);
        }
        return instance;
    }

    public String getGuestId() {
        return sharedPreferences.getString("GuestId", "GuestId");
    }

    public void setGuestId(String GuestId) {
        editor = sharedPreferences.edit();
        editor.putString("GuestId", GuestId);
        editor.commit();
    }

    public String getGuestName() {
        return sharedPreferences.getString("GuestName", "GuestName");
    }

    public void setGuestName(String GuestName) {
        editor = sharedPreferences.edit();
        editor.putString("GuestName", GuestName);
        editor.commit();
    }
    public String getUsersList() {
        return sharedPreferences.getString("UsersList", "UsersList");
    }

    public void setUsersList(String UsersList) {
        editor = sharedPreferences.edit();
        editor.putString("UsersList", UsersList);
        editor.commit();
    }
}
