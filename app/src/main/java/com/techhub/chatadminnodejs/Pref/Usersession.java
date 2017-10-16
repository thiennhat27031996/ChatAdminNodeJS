package com.techhub.chatadminnodejs.Pref;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by thiennhat on 16/10/2017.
 */

public class Usersession {
    private static final String TAG=Usersession.class.getSimpleName();
    private static final String PREF_NAME="login";
    private static final String KEY_IS_LOGGED_IN="isloggedin";
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Context ctx;

    public Usersession(Context ctx) {
        this.ctx = ctx;
        prefs=ctx.getSharedPreferences(PREF_NAME,ctx.MODE_PRIVATE);
        editor=prefs.edit();
    }
    public void setLoggedin(boolean isLoggedin){
        editor.putBoolean(KEY_IS_LOGGED_IN,isLoggedin);
        editor.apply();
    }
    public boolean isUserLoggedin(){
        return prefs.getBoolean(KEY_IS_LOGGED_IN,false);
    }
}
