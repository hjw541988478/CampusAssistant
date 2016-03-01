package com.ywxy.ca.fragment;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.gc.materialdesign.views.ButtonFlat;
import com.ywxy.ca.R;
import com.ywxy.ca.adapter.DrawerListAdapter;
import com.ywxy.ca.entity.DrawerListItem;
import com.ywxy.ca.util.ViewUtil;
import com.ywxy.ca.view.DrawerArrowDrawable;

import java.util.ArrayList;
import java.util.List;

import static android.view.Gravity.START;

public class DrawerFragment extends BaseFragment {
    private BaseFragment.NavigationDrawerCallback mCallbacks;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerListView;
    private DrawerArrowDrawable drawerArrowDrawable;
    private float offset;
    private boolean flipped;
    private int mCurrentSelectedPosition = 0;
    private List<DrawerListItem> mData = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mData = new ArrayList<DrawerListItem>();
        initData();
    }

    private void initData() {
        String[] itemTitle = getResources().getStringArray(R.array.item_title);
        int[] itemIconRes = {R.drawable.ic_drawer_home,
                R.drawable.ic_drawer_update, R.drawable.ic_drawer_sug,
                R.drawable.ic_drawer_about};
        for (int i = 0; i < itemTitle.length; i++) {
            mData.add(new DrawerListItem(getResources().getDrawable(
                    itemIconRes[i]), itemTitle[i]));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDrawerListView = (ListView) inflater.inflate(
                R.layout.fragment_navigation_drawer, container, false);
        DrawerListAdapter adapter = new DrawerListAdapter(getActivity(), mData);
        mDrawerListView.setAdapter(adapter);
        mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);
        mDrawerListView
                .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        selectItem(position);
                    }
                });
        selectItem(mCurrentSelectedPosition);
        return mDrawerListView;
    }

    public void setUp(final DrawerLayout drawerLayout,
                      ImageView drawer_indicator) {
        mDrawerLayout = drawerLayout;
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
                GravityCompat.START);
        final Resources resources = getResources();
        drawerArrowDrawable = new DrawerArrowDrawable(resources);
        drawerArrowDrawable.setStrokeColor(resources.getColor(R.color.white));

        mDrawerLayout
                .setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {
                        offset = slideOffset;
                        if (slideOffset >= .995) {
                            flipped = true;
                            drawerArrowDrawable.setFlip(flipped);
                        } else if (slideOffset <= .005) {
                            flipped = false;
                            drawerArrowDrawable.setFlip(flipped);
                        }

                        drawerArrowDrawable.setParameter(offset);
                    }
                });
        drawer_indicator.setImageDrawable(drawerArrowDrawable);
        drawer_indicator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerVisible(START)) {
                    drawerLayout.closeDrawer(START);
                } else {
                    drawerLayout.openDrawer(START);
                }
            }
        });

    }

    private void selectItem(int position) {
        mCurrentSelectedPosition = position;
        if (mDrawerListView != null) {
            mDrawerListView.setItemChecked(position, true);
        }
        if (mCallbacks != null) {
            if (position == 1) {
                final android.app.Dialog dg = new android.app.Dialog(
                        getActivity());
                dg.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dg.setContentView(R.layout.dialog_loading);
                dg.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dg.dismiss();
                        final android.app.Dialog no_dg = new android.app.Dialog(
                                getActivity());
                        no_dg.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        View view = getActivity().getLayoutInflater().inflate(
                                R.layout.dialog_no_latest, null);
                        ButtonFlat bt_ok = (ButtonFlat) view
                                .findViewById(R.id.btn_ok);
                        bt_ok.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                if (ViewUtil.isFastDoubleClick()) {
                                    return;
                                }
                                no_dg.dismiss();
                            }
                        });
                        no_dg.setContentView(view);
                        no_dg.show();
                    }
                }, 3000);
            } else
                mCallbacks.onNavigationDrawerItemSelected(mData.get(position)
                        .getTitle());
        }
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(START);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallback) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement Callbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

}
