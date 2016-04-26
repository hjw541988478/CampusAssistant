package cn.edu.university.zfcms.widget.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList mList;
    private RecyclerViewHolder.OnItemClickListener onItemClickListener;
    private RecyclerViewHolder.OnItemLongClickListener onItemLongClickListener;

    public RecyclerViewAdapter() {
        this.mList = new ArrayList();
    }

    @Override
    public int getItemCount() {
        return this.mList.size();
    }

    public int getListSize() {
        return this.mList.size();
    }

    public <T> T getItem(int position) {
        return (T) this.mList.get(position);
    }

    public <T> T getItemByPosition(int position) {
        return this.getItem(position);
    }

    public void setList(List list) {
        this.mList.clear();
        if (list == null) return;
        this.mList.addAll(list);
    }

    public void clear() {
        this.mList.clear();
    }

    public void remove(Object o) {
        this.mList.remove(o);
    }

    public List getList() {
        return this.mList;
    }

    public void addAll(Collection list) {
        this.mList.addAll(list);
    }

    /**
     * 请返回RecyclerView加载的布局Id数组
     *
     * @return 布局Id数组
     */
    public abstract int[] getItemLayouts();

    /**
     * 对接了onBindViewHolder
     * onBindViewHolder里的逻辑写在这
     *
     * @param viewHolder viewHolder
     * @param position   position
     */
    public abstract void onBindRecycleViewHolder(RecyclerViewHolder viewHolder, int position);

    /**
     * 如果是多布局的话，请写判断逻辑
     * 单布局可以不写
     *
     * @param position Item position
     * @return 布局Id数组中的index
     */
    public abstract int getRecycleViewItemType(int position);


    /**
     * 根据position获取ItemType
     *
     * @return 默认ItemType等于0
     */
    @Override
    public int getItemViewType(int position) {
        return this.getRecycleViewItemType(position);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        try {
            RecyclerViewHolder RecyclerViewHolder = (RecyclerViewHolder) holder;
            this.onBindRecycleViewHolder(RecyclerViewHolder, position);
            RecyclerViewHolder.setOnItemClickListener(this.onItemClickListener, position);
            RecyclerViewHolder.setOnItemLongClickListener(this.onItemLongClickListener, position);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType < 0) return null;
        if (this.getItemLayouts() == null) return null;
        int[] layoutIds = this.getItemLayouts();
        if (layoutIds.length < 1) return null;

        int itemLayoutId;
        if (layoutIds.length == 1) {
            itemLayoutId = layoutIds[0];
        } else {
            itemLayoutId = layoutIds[viewType];
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(itemLayoutId, null);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new RecyclerViewHolder(view);
    }

    /**
     * 设置点击事件
     *
     * @param onItemClickListener onItemClickListener
     */
    public void setOnItemClickListener(RecyclerViewHolder.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * 设置长点击事件
     *
     * @param onItemLongClickListener onItemLongClickListener
     */
    public void setOnItemLongClickListener(RecyclerViewHolder.OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }


}
