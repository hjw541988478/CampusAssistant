package cn.edu.university.zfcms.app;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.facebook.stetho.Stetho;
import com.github.moduth.blockcanary.BlockCanary;
import com.squareup.leakcanary.LeakCanary;

import cn.edu.university.zfcms.util.PreferenceUtil;

/**
 * APP
 */
public class ZfsoftCampusAsstApp extends Application{

    private static ZfsoftCampusAsstApp appContext;

    public static ZfsoftCampusAsstApp getInstance() {
        return appContext;
    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
        PreferenceUtil.init(getApplicationContext());
        Stetho.initializeWithDefaults(this);
        LeakCanary.install(this);
    }


}
