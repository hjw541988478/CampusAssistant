package cn.edu.university.zfcms.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public abstract class BaseFragment extends Fragment {

    protected View self;

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
        this.initData();
        this.initListeners();
        return this.self;
    }

    protected abstract int getLayoutId();

    protected abstract void initViews(View self, Bundle savedInstanceState);

    protected abstract void initListeners();

    protected abstract void initData();

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
