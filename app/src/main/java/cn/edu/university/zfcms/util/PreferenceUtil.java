package cn.edu.university.zfcms.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.edu.university.zfcms.base.func.Config;
import cn.edu.university.zfcms.data.login.local.LocalLoginDataSource;
import cn.edu.university.zfcms.model.Setting;
import cn.edu.university.zfcms.model.User;

/**
 * Created by hjw on 16/4/15.
 */
public class PreferenceUtil {

    private static String tag = PreferenceUtil.class.getSimpleName();

    private static final String KEY_LOGIN_VIEWSTATE = "LOGIN_VIEWSTATE";

    private static Context mContext;

    public static void init(Context context) {
        mContext = context;
    }

    public static void set(String key, String val) {
        SharedPreferences.Editor editor = mContext
                .getSharedPreferences(Config.SP_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(key, val);
        editor.apply();
    }

    public static String get(String key) {
        String spText = mContext.getSharedPreferences(Config.SP_NAME, Context.MODE_PRIVATE)
                .getString(key, "");
        return spText;
    }

    public static void set(Object obj, Class claz) {
        SharedPreferences.Editor editor = mContext
                .getSharedPreferences(Config.SP_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(claz.getSimpleName(), GsonUtil.getGson().toJson(obj));
        editor.apply();
    }

    public static <T> T get(Class<T> clazz) {
        String spText = mContext.getSharedPreferences(Config.SP_NAME, Context.MODE_PRIVATE)
                .getString(clazz.getSimpleName(), "");
        T content = null;
        if (!spText.isEmpty()) {
            content = GsonUtil.getGson().fromJson(spText, clazz);
        }
        return content;
    }

    public static Setting getSettingConfig() {
        Setting setting = get(Setting.class);
        if (setting == null && getLoginUser() != null) {
            BmobQuery<Setting> query = new BmobQuery<>();
            query.addWhereEqualTo("userid", getLoginUser().getUsername());
            query.findObjects(mContext, new FindListener<Setting>() {
                @Override
                public void onSuccess(List<Setting> list) {
                    if (!list.isEmpty()) {
                        updateSettingConfig(list.get(0));
                    } else {
                        Log.e(tag, "setting config by userid " +
                                getLoginUser().getUsername() + " query results is empty");
                    }
                }

                @Override
                public void onError(int i, String s) {
                    Log.e(tag, "setting config query results error " + s);
                }
            });
        }
        return setting;
    }

    public static void updateSettingConfig(final Setting setting) {
        set(setting, Setting.class);
        BmobQuery<Setting> query = new BmobQuery<>();
        query.addWhereEqualTo("userid", setting.userId);
        query.findObjects(mContext, new FindListener<Setting>() {
            @Override
            public void onSuccess(List<Setting> list) {
                if (!list.isEmpty()) {
                    setting.update(mContext, new UpdateListener() {

                        @Override
                        public void onSuccess() {
                            Log.d(tag, "setting config update succesfully.");
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            Log.e(tag, "setting config update fail error " + s);
                        }
                    });
                } else {
                    setting.save(mContext, new SaveListener() {
                        @Override
                        public void onSuccess() {
                            Log.e(tag, "setting config save successfully");
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            Log.e(tag, "setting config save fail error " + s);
                        }
                    });
                    Log.e(tag, "setting config by userid " +
                            getLoginUser().getUsername() + " query results is empty");
                }
            }

            @Override
            public void onError(int i, String s) {
                Log.e(tag, "setting config query results error " + s);
            }
        });
    }

    public static void updateLoginUser(User user) {
        LocalLoginDataSource.getInstance(mContext).updateLoginUser(user);
    }

    public static User getLoginUser() {
        return LocalLoginDataSource.getInstance(mContext).getLoginUser();
    }

    public static void clearLoginUser() {
        LocalLoginDataSource.getInstance(mContext).logout();
    }

    public static void saveLoginViewStateParam(String newerHeaderVal) {
        set(KEY_LOGIN_VIEWSTATE, newerHeaderVal);
    }

    public static String getLoginViewStateParam() {
        return get(KEY_LOGIN_VIEWSTATE);
    }
}
