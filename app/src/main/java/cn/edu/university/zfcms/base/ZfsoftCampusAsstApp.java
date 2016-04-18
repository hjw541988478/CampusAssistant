package cn.edu.university.zfcms.base;

import android.app.Application;
import android.util.Log;

import cn.bmob.v3.Bmob;
import cn.edu.university.zfcms.util.SpUtil;

/**
 * APP
 */
public class ZfsoftCampusAsstApp extends Application{

    private static final String tag = ZfsoftCampusAsstApp.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(tag,"CampusAsstApp is initializing...");
        SpUtil.init(getApplicationContext());

        Bmob.initialize(this,Config.BMOB_APP_ID);
    }


}
