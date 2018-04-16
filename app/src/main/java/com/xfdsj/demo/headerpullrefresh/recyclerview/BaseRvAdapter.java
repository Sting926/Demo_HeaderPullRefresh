package com.xfdsj.demo.headerpullrefresh.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Administrator on 2017/5/23.
 */

public abstract class BaseRvAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {

  protected Context mContext;
  protected int mLayoutResId;
  protected LayoutInflater mInflater;
  protected List<T> mData = new ArrayList<>();

  /**
   * @param layoutResId The layout resource id of each item.
   * @param data A new list is created out of this one to avoid mutable list
   */
  public BaseRvAdapter(int layoutResId, List<T> data) {
    this.mData = data == null ? new ArrayList<T>() : data;
    if (layoutResId != 0) {
      this.mLayoutResId = layoutResId;
    }
  }

  public BaseRvAdapter(List<T> data) {
    this(0, data);
  }

  @Override public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    this.mContext = parent.getContext();
    this.mInflater = LayoutInflater.from(mContext);
    View itemView = mInflater.inflate(mLayoutResId, parent, false);
    return new BaseViewHolder(itemView);
  }

  @Override public void onBindViewHolder(BaseViewHolder holder, int position) {
    onBindItemHolder(holder, mData.get(position));
  }

  //局部刷新关键：带payload的这个onBindViewHolder方法必须实现
  @Override public void onBindViewHolder(BaseViewHolder holder, int position, List<Object> payloads) {
    if (payloads.isEmpty()) {
      onBindViewHolder(holder, position);
    } else {
      onBindItemHolder(holder, mData.get(position), payloads);
    }
  }

  @Override public int getItemCount() {
    return mData.size();
  }

  public abstract void onBindItemHolder(BaseViewHolder holder, T item);

  public void onBindItemHolder(BaseViewHolder holder, T item, List<Object> payloads) {
    onBindItemHolder(holder, item);
  }

  public List<T> getData() {
    return mData;
  }

  public T getItem(int position) {
    return mData.get(position);
  }

  public void setData(Collection<T> list) {
    this.mData.clear();
    this.mData.addAll(list);
    notifyDataSetChanged();
  }

  public void addAll(Collection<T> list) {
    int lastIndex = this.mData.size();
    if (this.mData.addAll(list)) {
      notifyItemRangeInserted(lastIndex, list.size());
    }
  }

  public void remove(int position) {
    this.mData.remove(position);
    notifyItemRemoved(position);

    if (position != (getData().size())) { // 如果移除的是最后一个，忽略
      notifyItemRangeChanged(position, this.mData.size() - position);
    }
  }

  public void clear() {
    mData.clear();
    notifyDataSetChanged();
  }
}
