package cn.edu.university.zfcms.base;

import android.app.Application;
import android.util.Log;

import com.squareup.leakcanary.LeakCanary;

import cn.bmob.v3.Bmob;
import cn.edu.university.zfcms.base.func.Config;
import cn.edu.university.zfcms.http.HttpManager;
import cn.edu.university.zfcms.util.PreferenceUtil;

/**
 * APP
 */
public class ZfsoftCampusAsstApp extends Application{

    private static final String tag = ZfsoftCampusAsstApp.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(tag,"CampusAsstApp is initializing...");
        PreferenceUtil.init(getApplicationContext());

        LeakCanary.install(this);
        Bmob.initialize(this, Config.BMOB_APP_ID);
    }


}
