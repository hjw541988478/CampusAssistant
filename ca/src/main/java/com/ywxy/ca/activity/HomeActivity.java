package com.ywxy.ca.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import com.ywxy.ca.R;
import com.ywxy.ca.fragment.AboutFragment;
import com.ywxy.ca.fragment.BaseFragment.NavigationDrawerCallback;
import com.ywxy.ca.fragment.DrawerFragment;
import com.ywxy.ca.fragment.FeedbackFragment;
import com.ywxy.ca.fragment.HomeFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends FragmentActivity implements
        NavigationDrawerCallback {

    private DrawerFragment mNavigationDrawerFragment;
    private Fragment currentFragment;
    private Fragment lastFragment;
    private List<Map<String, String>> data;
    private SimpleAdapter adapter;
    private Context context;

    private void initAboutData() {
        data = new ArrayList<Map<String, String>>();
        Map<String, String> map = new HashMap<String, String>();
        map.put("emp_name", "Summer");
        map.put("emp_pos", "后台开发");
        data.add(map);
        map = new HashMap<String, String>();
        map.put("emp_name", "Mr浅");
        map.put("emp_pos", "APP开发");
        data.add(map);
        map = new HashMap<String, String>();
        map.put("emp_name", "杨干");
        map.put("emp_pos", "产品策划");
        data.add(map);
        map = new HashMap<String, String>();
        map.put("emp_name", "陈武臣");
        map.put("emp_pos", "APP开发");
        data.add(map);
        adapter = new SimpleAdapter(this, data, R.layout.fragment_team_item,
                new String[]{"emp_name", "emp_pos"}, new int[]{
                R.id.tv_emp_name, R.id.tv_emp_pos});
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initAboutData();
        setContentView(R.layout.activity_home);
        mNavigationDrawerFragment = (DrawerFragment) this
                .getSupportFragmentManager().findFragmentById(
                        R.id.navigation_drawer);

        // 设置抽屉
        mNavigationDrawerFragment.setUp(
                (DrawerLayout) findViewById(R.id.drawer_layout),
                (ImageView) findViewById(R.id.btn_signin_back));

    }

    @Override
    public void onNavigationDrawerItemSelected(String title) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        currentFragment = fragmentManager.findFragmentByTag(title);
        if (currentFragment == null) {
            if (title.equals("关于我们")) {
                currentFragment = new AboutFragment(adapter);
            } else if (title.equals("首页"))
                currentFragment = new HomeFragment();
            else if (title.equals("反馈建议")) {
                currentFragment = new FeedbackFragment();
            } else
                currentFragment = new HomeFragment();
            ft.add(R.id.container, currentFragment, title);
        }
        if (lastFragment != null) {
            ft.hide(lastFragment);
        }
        if (currentFragment.isDetached()) {
            ft.attach(currentFragment);
        }
        ft.show(currentFragment);
        lastFragment = currentFragment;
        ft.commit();
    }

}
