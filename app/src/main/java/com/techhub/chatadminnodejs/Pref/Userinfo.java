package com.techhub.chatadminnodejs.Pref;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by thiennhat on 16/10/2017.
 */

public class Userinfo {
    private static final String TAG=Usersession.class.getSimpleName();
    private static final String PREF_NAME="userinfo";
    private static final String KEY_USERID="userid";
    private static final String KEY_USERTOKEN="usertoken";
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Context ctx;

    public Userinfo(Context ctx) {
        this.ctx = ctx;
        prefs=ctx.getSharedPreferences(PREF_NAME,ctx.MODE_PRIVATE);
        editor=prefs.edit();
    }

    public void setUserid(String userid){
        editor.putString(KEY_USERID,userid);
        editor.apply();
    }
    public void setUsertoken(String usertoken){
        editor.putString(KEY_USERTOKEN,usertoken);
        editor.apply();
    }



    public void clearUserinfo(){
        editor.clear();
        editor.commit();
    }
    public String getKeyUserid(){
        return prefs.getString(KEY_USERID,"");
    }
    public String getKeyUsertoken(){
        return prefs.getString(KEY_USERTOKEN,"");
    }
}
