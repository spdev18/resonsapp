package com.resons.app;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "UserData";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String LOGIN_ID = "LOGIN";

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public void setLoginId(String id) {
        editor.putString(LOGIN_ID, id);
        editor.commit();
    }

    public String getLoginId() {
        return pref.getString(LOGIN_ID, "0");
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }


    public void setvalue(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    public boolean getvalue(String key, Boolean defaultvalue) {
        return pref.getBoolean(key, defaultvalue);
    }


    public void setvalue(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }

    public int getvalue(String key, int defaultvalue) {
        return pref.getInt(key, defaultvalue);
    }
    public void setvalue(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public String getvalue(String key) {
        return pref.getString(key, "0");
    }

    public String getStringValue(String key) {
        return pref.getString(key, "");
    }

    public void Clear() {
        pref.edit().clear().commit();
    }


}
