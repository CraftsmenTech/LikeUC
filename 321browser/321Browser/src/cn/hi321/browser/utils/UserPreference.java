package cn.hi321.browser.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
 

public class UserPreference {
    private static SharedPreferences mUserPreferences;
    private static String USER_PREFERENCE = "user_preference";
 
    public static void clearData(){
    	if(mUserPreferences!=null) { 
    		Editor editor = mUserPreferences.edit();
    		if(editor != null)
    	    editor.clear();
    		mUserPreferences = null; 
    	}
    } 
    public static void ensureIntializePreference(Context context) {
        if (mUserPreferences != null) { 
            return;
        }
        mUserPreferences = context.getSharedPreferences(USER_PREFERENCE, 0);
    }

    public static void save(String key, String value) {
        Editor editor = mUserPreferences.edit();
        editor.putString(key, value);
        editor.commit(); 
    }
    
    public static void save(String key, int value) {
        Editor editor = mUserPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static void save(String key, boolean value) {
        Editor editor = mUserPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static void save(String key, long value) {
        Editor editor = mUserPreferences.edit();
        editor.putLong(key, value);
        editor.commit();
    }
    
    public static void save(String key, Float value) {
        Editor editor = mUserPreferences.edit();
        editor.putFloat(key, value);
        editor.commit();
    }
 
    public static String read(String key, String defaultvalue) {
        return mUserPreferences.getString(key, defaultvalue);
    }

    public static int read(String key, int defaultvalue) {
        return mUserPreferences.getInt(key, defaultvalue);
    }

    public static long read(String key, long defaultvalue) {
        return mUserPreferences.getLong(key, defaultvalue);
    }
    
    public static float read(String key, float defaultvalue) {
        return mUserPreferences.getFloat(key, defaultvalue);
    }


    public static boolean read(String key, boolean defaultvalue) {
        return mUserPreferences.getBoolean(key, defaultvalue);
    }
 

}
