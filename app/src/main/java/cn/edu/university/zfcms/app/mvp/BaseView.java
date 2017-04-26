package cn.edu.university.zfcms.app.mvp;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

/**
 * Created by Administrator on 013 2017/2/13.
 */

public interface BaseView<T> {
    // 子页面想使用父页面的Presenter时候可以这么干
    void setPresenter(T presenter);
    RxAppCompatActivity getRxLifeActivity();
}

