package cn.edu.university.zfcms.util;

import android.content.Context;
import android.content.SharedPreferences;

import cn.bmob.v3.BmobUser;
import cn.edu.university.zfcms.base.func.Config;
import cn.edu.university.zfcms.data.login.local.BassLoginDataSource;
import cn.edu.university.zfcms.model.User;

/**
 * Created by hjw on 16/4/15.
 */
public class PreferenceUtil {

    private static final String KEY_LOGIN_VIEWSTATE = "LOGIN_VIEWSTATE";
    private static final String KEY_LOGIN_USER_ID = "LOGIN_USER_ID";
    private static final String KEY_LOGIN_USER_PSWD = "LOGIN_USER_PSWD";
    private static final String KEY_LOGIN_USER_REAL_NAME = "LOGIN_USER_REAL_NAME";

//    private static final String STATE_HEADER_DEF_VAL = "dDwyODE2NTM0OTg7Oz6PUWaWJVz623LrXp10j4dGgzlnbw==";

    private static Context mContext;

    public static void init(Context context) {
        mContext = context;
    }

    public static void updateLoginUser(User user) {
//        SharedPreferences.Editor editor = mContext
//                .getSharedPreferences(Config.SP_NAME,Context.MODE_PRIVATE).edit();
//        editor.putString(KEY_LOGIN_USER_ID,user.userId);
//        editor.putString(KEY_LOGIN_USER_REAL_NAME,user.userRealName);
//        editor.putString(KEY_LOGIN_USER_PSWD,user.userPswd);
//        editor.apply();
        BassLoginDataSource.getInstance(mContext).updateLoginUser(user);
    }

    public static User getLoginUser() {
//        User user = new User();
//        SharedPreferences sp = mContext.getSharedPreferences(Config.SP_NAME,Context.MODE_PRIVATE);
//        user.userId = sp.getString(KEY_LOGIN_USER_ID,"");
//        user.userRealName = sp.getString(KEY_LOGIN_USER_REAL_NAME,"");
//        user.userPswd = sp.getString(KEY_LOGIN_USER_PSWD,"");
        return BassLoginDataSource.getInstance(mContext).getLoginUser();
    }

    public static void clearLoginUser() {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(Config.SP_NAME, Context.MODE_PRIVATE).edit();
        editor.remove(KEY_LOGIN_USER_REAL_NAME);
        editor.remove(KEY_LOGIN_USER_PSWD);
        editor.remove(KEY_LOGIN_USER_ID);
        editor.apply();
    }

    public static void saveLoginViewStateParam(String newerHeaderVal) {
        SharedPreferences.Editor editor = mContext
                .getSharedPreferences(Config.SP_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(KEY_LOGIN_VIEWSTATE, newerHeaderVal);
        editor.apply();
    }

    public static String getLoginViewStateParam() {
        String spText = mContext.getSharedPreferences(Config.SP_NAME, Context.MODE_PRIVATE)
                .getString(KEY_LOGIN_VIEWSTATE, "");
        return spText;
    }
}
