package cn.edu.university.zfcms.base;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import butterknife.ButterKnife;

/**
 * Created by hjw on 16/3/9.
 */
public abstract class BaseAppCompatActivity extends RxAppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(getLayoutId());
        ButterKnife.bind(this);

        this.initToolbar(savedInstanceState);
        this.initViews(savedInstanceState);
        this.initData();
        this.initListeners();
    }

    protected abstract int getLayoutId();

    protected <V extends View> V findView(int id) {return (V) this.findViewById(id);}

    protected abstract void initViews(Bundle savedInstanceState);

    protected abstract void initToolbar(Bundle savedInstanceState);

    protected abstract void initListeners();

    protected abstract void initData();

    public void showToast(String msg) {this.showToast(msg, Toast.LENGTH_SHORT);}

    @SuppressLint("ShowToast")
    public void showToast(String msg, int duration) {
        if (msg == null) return;
        if (duration == Toast.LENGTH_SHORT || duration == Toast.LENGTH_LONG) {
            Toast.makeText(this, msg, duration);
        } else {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        }
    }

    public RxAppCompatActivity getRxLifeActivity() {
        return this;
    }
}