package com.ywxy.ca.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ywxy.ca.R;
import com.ywxy.ca.entity.SemesterAllGradeItem;

import java.util.ArrayList;
import java.util.List;

public class GradeDescListAdapter extends BaseAdapter {

    private List<SemesterAllGradeItem> mlist;
    private LayoutInflater inflater;
    private boolean isRed;
    private Context context;

    public GradeDescListAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        mlist = new ArrayList<SemesterAllGradeItem>();
    }

    public boolean isRed() {
        return !isRed;
    }

    public void setRed(boolean isRed) {
        this.isRed = isRed;
    }

    public List<SemesterAllGradeItem> getGradeList() {
        return mlist;
    }

    public void setGradeList(List<SemesterAllGradeItem> mList) {
        this.mlist = mList;
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater
                    .inflate(R.layout.aty_history_desc_item, null);
            holder = new ViewHolder();
            holder.fail_subname = (TextView) convertView
                    .findViewById(R.id.fail_sub_name);
            holder.fail_subgrade = (TextView) convertView
                    .findViewById(R.id.fail_sub_grade);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.fail_subname.setText(mlist.get(position).getCoursename());
        holder.fail_subgrade.setText(mlist.get(position).getGrade());
        if (!isRed())
            holder.fail_subgrade.setTextColor(context.getResources().getColor(
                    R.color.red));
        else
            holder.fail_subgrade.setTextColor(context.getResources().getColor(
                    R.color.black));
        return convertView;
    }

    public class ViewHolder {
        public TextView fail_subname;
        public TextView fail_subgrade;
    }

}
