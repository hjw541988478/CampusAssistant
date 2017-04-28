package cn.edu.university.zfcms.ui.course.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import cn.edu.university.zfcms.ui.course.fragment.DayCourseFragment;

/**
 * Created by hjw on 16/4/25.
 */
public class DayCoursesAdapter extends FragmentStatePagerAdapter {
    private static final String[] DAYOFWEEKS = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};

    public DayCoursesAdapter(Context context, FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return DayCourseFragment.newInstance();
    }

    @Override
    public int getCount() {
        return DAYOFWEEKS.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return DAYOFWEEKS[position];
    }
}
