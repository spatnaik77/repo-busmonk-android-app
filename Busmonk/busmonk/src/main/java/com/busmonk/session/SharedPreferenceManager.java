package com.busmonk.session;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by sr250345 on 12/10/15.
 */
public class SharedPreferenceManager {

    private Context context;
    private SharedPreferences sharedPref;

    private static final String KEY_SHARED_PREF = "BUSMONK_USER";
    private static final int KEY_MODE_PRIVATE   = 0;
    private static final String KEY_AUTH_TOKEN  = "authToken";

    static SharedPreferenceManager singleton;

    public static SharedPreferenceManager getInstance(Context context)
    {
        if(singleton == null)
        {
            singleton = new SharedPreferenceManager(context);
        }
        return singleton;
    }

    private SharedPreferenceManager(Context context) {
        this.context = context;
        sharedPref = this.context.getSharedPreferences(KEY_SHARED_PREF, KEY_MODE_PRIVATE);
    }

    public void putAuthToken(String authToken)
    {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(KEY_AUTH_TOKEN, authToken);
        editor.commit();
    }

    public String getAuthToken()
    {
        return sharedPref.getString(KEY_AUTH_TOKEN, null);
    }

    public void removeAuthToken()
    {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(KEY_AUTH_TOKEN);
        editor.commit();
    }
}
