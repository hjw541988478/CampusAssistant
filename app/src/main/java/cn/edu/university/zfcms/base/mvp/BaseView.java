package cn.edu.university.zfcms.base.mvp;

import android.content.Context;

/**
 * Created by hjw on 16/4/15.
 */
public interface BaseView<T> {
    void setPresenter(T presenter);

    Context getLongLifeCycleContext();

    Context getActivityContext();
}
