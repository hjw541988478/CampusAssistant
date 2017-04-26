package cn.edu.university.zfcms.app;

import android.app.Application;
import android.util.Log;

import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;

import cn.edu.university.zfcms.util.PreferenceUtil;

/**
 * APP
 */
public class ZfsoftCampusAsstApp extends Application{

    private static final String TAG = ZfsoftCampusAsstApp.class.getSimpleName();

    private static ZfsoftCampusAsstApp appContext;

    public static ZfsoftCampusAsstApp getInstance() {
        return appContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate() called");
        appContext = this;
        PreferenceUtil.init(getApplicationContext());
        Stetho.initializeWithDefaults(this);
        LeakCanary.install(this);
    }


}
