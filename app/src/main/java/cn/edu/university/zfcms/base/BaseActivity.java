package cn.edu.university.zfcms.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.ViewGroup;

import cn.edu.university.zfcms.util.SnackbarUtil;

/**
 * Created by hjw on 16/3/9.
 */
public abstract class BaseActivity<T extends BasePresenter> extends SimpleActivity implements BaseView {

    protected T mPresenter;

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        super.onViewCreated(savedInstanceState);
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        super.onDestroy();
    }

    @Override
    public void showErrorMsg(String msg) {
        SnackbarUtil.show(((ViewGroup) findViewById(android.R.id.content)).getChildAt(0), msg);
    }

    @Override
    public void useNightMode(boolean isNight) {
        if (isNight) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO);
        }
        recreate();
    }

    @Override
    public void stateError() {
    }

    @Override
    public void stateLoading() {
    }

    @Override
    public void stateMain() {
    }

}