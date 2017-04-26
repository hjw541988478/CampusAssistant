package cn.edu.university.zfcms.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.ButterKnife;

public abstract class BaseFragment extends Fragment {

    protected View self;

    private boolean isVisible;

    private boolean isPrepared;

    private boolean isFirstLoad = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (this.self == null) {
            this.self = inflater.inflate(this.getLayoutId(), container, false);
        }
        if (this.self.getParent() != null) {
            ViewGroup parent = (ViewGroup) this.self.getParent();
            parent.removeView(this.self);
        }

        this.initViews(this.self, savedInstanceState);
        this.initNoLazyData();
        this.initListeners();
        return this.self;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    protected void onVisible() {
        lazyLoad();
    }

    protected void onInvisible() {
    }

    protected abstract int getLayoutId();

    protected void initViews(View self, Bundle savedInstanceState) {
        ButterKnife.bind(this,self);
        isPrepared = true;
        lazyLoad();
    }

    protected void lazyLoad() {
        if (!isPrepared || !isVisible || !isFirstLoad) {
            return;
        }
        isFirstLoad = false;
        initLazyData();
    }

    /**
     * 延迟加载时使用该方法
     */
    protected void initLazyData(){

    }

    protected abstract void initListeners();

    protected void initNoLazyData(){

    }

    protected <V extends View> V findView(int id) {
        return (V) this.self.findViewById(id);
    }

    public void showToast(String msg) {
        this.showToast(msg, Toast.LENGTH_SHORT);
    }

    public void showToast(String msg, int duration) {
        if (msg == null) return;
        if ( duration == Toast.LENGTH_LONG) {
            Toast.makeText(getActivity(),msg,Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
        }
    }

    public void showToast(int resId) {
        this.showToast(resId, Toast.LENGTH_SHORT);
    }


    public void showToast(int resId, int duration) {
        if ( duration == Toast.LENGTH_LONG) {
            Toast.makeText(getActivity(),getString(resId),Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getActivity(),getString(resId),Toast.LENGTH_SHORT).show();
        }
    }

}
