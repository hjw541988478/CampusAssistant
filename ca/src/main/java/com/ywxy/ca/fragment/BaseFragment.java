package com.ywxy.ca.fragment;

import android.support.v4.app.Fragment;

public class BaseFragment extends Fragment {
    public interface NavigationDrawerCallback {
        void onNavigationDrawerItemSelected(String title);
    }

}
