package cn.edu.university.zfcms.base.mvp;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by hjw on 16/4/15.
 */
public interface BasePresenter  {
    Handler uiHandler = new Handler(Looper.getMainLooper());

    void start();
}
