package cn.edu.university.zfcms.util;

import android.content.Context;
import android.content.SharedPreferences;

import cn.edu.university.zfcms.model.User;

/**
 * Created by hjw on 16/4/15.
 */
public class SpUtil {
    private static final String XMUTSP = "xmut_assit_sp";
    private static final String STATE_HEADER_KEY = "__VIEWSTATE";
    private static final String LOGIN_USER_ID_KEY = "LOGIN_USER_ID";
    private static final String LOGIN_USER_PSWD_KEY = "LOGIN_USER_PSWD";
    private static final String LOGIN_USER_NAME_KEY = "LOGIN_USER_NAME";

    private static final String STATE_HEADER_DEF_VAL = "dDwyODE2NTM0OTg7Oz6PUWaWJVz623LrXp10j4dGgzlnbw==";
    private static Context mContext;

    public static void init(Context context){
        mContext = context;
    }

    public static void saveLoginUser(User user){
        SharedPreferences.Editor editor = mContext
                .getSharedPreferences(XMUTSP,Context.MODE_PRIVATE).edit();
        editor.putString(LOGIN_USER_ID_KEY,user.userId);
        editor.putString(LOGIN_USER_NAME_KEY,user.userName);
        editor.putString(LOGIN_USER_PSWD_KEY,user.userPswd);
        editor.apply();
    }

    public static User getLoginUser(){
        User user = new User();
        SharedPreferences sp = mContext.getSharedPreferences(XMUTSP,Context.MODE_PRIVATE);
        user.userId = sp.getString(LOGIN_USER_ID_KEY,"");
        user.userName = sp.getString(LOGIN_USER_NAME_KEY,"");
        user.userPswd = sp.getString(LOGIN_USER_PSWD_KEY,"");
        return user;
    }

    public static void clearLoginUser(){
        SharedPreferences.Editor editor = mContext.getSharedPreferences(XMUTSP,Context.MODE_PRIVATE).edit();
        editor.remove(LOGIN_USER_NAME_KEY);
        editor.remove(LOGIN_USER_PSWD_KEY);
        editor.remove(LOGIN_USER_ID_KEY);
        editor.apply();
    }

    public static void saveNewerStateHeader(String newerHeaderVal){
        SharedPreferences.Editor editor = mContext
                .getSharedPreferences(XMUTSP,Context.MODE_PRIVATE).edit();
        editor.putString(STATE_HEADER_KEY,newerHeaderVal);
        editor.apply();
    }

    public static String getNewerStateHeaderVal(){
        String spText = mContext.getSharedPreferences(XMUTSP,Context.MODE_PRIVATE)
                .getString(STATE_HEADER_KEY, STATE_HEADER_DEF_VAL);
        return spText.isEmpty() ? STATE_HEADER_DEF_VAL : spText;
    }
}
