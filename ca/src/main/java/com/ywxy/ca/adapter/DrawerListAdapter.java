package com.ywxy.ca.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ywxy.ca.R;
import com.ywxy.ca.entity.DrawerListItem;

import java.util.List;

public class DrawerListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<DrawerListItem> mItems;

    public DrawerListAdapter(Context context, List<DrawerListItem> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mItems = data;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public DrawerListItem getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.fragment_drawer_list_item,
                    null);
            holder = new ViewHolder();
            holder.item_icon = (ImageView) convertView
                    .findViewById(R.id.item_icon);
            holder.item_title = (TextView) convertView
                    .findViewById(R.id.item_title);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.item_icon.setImageDrawable(mItems.get(position).getIcon());
        holder.item_title.setText(mItems.get(position).getTitle());
        return convertView;
    }

    public class ViewHolder {
        public ImageView item_icon;
        public TextView item_title;
    }

}
