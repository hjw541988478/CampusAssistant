package cn.edu.university.zfcms.widget.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

/**
 * Created by hjw on 16/4/26.
 */
public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    /**
     * 用于保存findViewById加载过的view
     */
    private final SparseArray<View> views;
    private View convertView;

    public RecyclerViewHolder(View convertView) {
        super(convertView);
        this.views = new SparseArray<>();
        this.convertView = convertView;
    }

    /**
     * 设置Item的点击事件
     */
    public void setOnItemClickListener(final RecyclerViewHolder.OnItemClickListener listener, final int position) {
        if (listener == null) {
            this.itemView.setOnClickListener(null);
        } else {
            this.itemView.setOnClickListener(v -> listener.onItemClick(v, position));
        }
    }

    /**
     * 设置Item的长点击事件
     */
    public void setOnItemLongClickListener(final RecyclerViewHolder.OnItemLongClickListener listener, final int position) {
        if (listener == null) {
            this.itemView.setOnLongClickListener(null);
        } else {
            this.itemView.setOnLongClickListener(v -> listener.onItemLongClick(v, position));
        }
    }

    /**
     * 由于findViewById性能过低
     * findViewById过的view会被缓存下来，以供下次find相同view的时候
     * ViewHolder模式 查找子View
     */
    public <T extends View> T findViewById(int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = convertView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 点击事件回调
     */
    public interface OnItemClickListener {

        void onItemClick(View convertView, int position);
    }

    /**
     * 长点击事件回调
     */
    public interface OnItemLongClickListener {

        boolean onItemLongClick(View convertView, int position);
    }

}
