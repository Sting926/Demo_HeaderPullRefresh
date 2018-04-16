package com.xfdsj.demo.headerpullrefresh.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * Created by Mr Jian on 2018/4/2.
 */

public class HeaderPullRefreshListView extends ListView {

  private IRefreshHeader mRefreshHeader;
  private boolean mRefreshing = false;//是否正在下拉刷新

  private OnRefreshListener mRefreshListener;

  public HeaderPullRefreshListView(Context context) {
    this(context, null);
  }

  public HeaderPullRefreshListView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  /**
   * 设置自定义的RefreshHeader
   * 注意：setRefreshHeader方法必须在setAdapter方法之前调用才能生效
   */
  public void setRefreshHeader(IRefreshHeader refreshHeader) {
    this.mRefreshHeader = refreshHeader;
  }

  /**
   * 重写滑动过度回调方法
   *
   * @param deltaY dy增量 正数是上拉 负数是下拉
   */
  @Override protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX,
      int maxOverScrollY, boolean isTouchEvent) {
    if (deltaY < 0 && isTouchEvent) { //下拉 ImageView进行放大效果
      mRefreshHeader.onMove(deltaY);
    } else if (deltaY > 0 && isTouchEvent) { //上拉过度（ListView内容太少的时候） 减少ImageView大小
      mRefreshHeader.onMove(deltaY);
    }
    return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
  }

  /**
   * 下拉后往上推 需要ImageView变小
   */
  @Override protected void onScrollChanged(int l, int t, int oldl, int oldt) {
    int top = mRefreshHeader.getHeaderView().getTop();//header 滑动到屏幕上方的距离
    if (top < 0) {
      mRefreshHeader.onMove(-top);
    }
    super.onScrollChanged(l, t, oldl, oldt);
  }

  @Override public boolean onTouchEvent(MotionEvent ev) {
    if (ev.getAction() == MotionEvent.ACTION_UP) {
      if (mRefreshHeader != null && mRefreshHeader.onRelease()) {
        if (mRefreshListener != null) {
          mRefreshing = true;
          mRefreshListener.onRefresh();
        }
      }
    }
    return super.onTouchEvent(ev);
  }

  public void refresh() {
    if (mRefreshHeader.getOffset() > 0 || mRefreshing) {// if RefreshHeader is Refreshing, return
      return;
    }
    if (mRefreshListener != null) {
      mRefreshHeader.onRefreshing();
      mRefreshing = true;
      mRefreshListener.onRefresh();
    }
  }

  public void refreshComplete() {
    if (mRefreshing) {
      mRefreshing = false;
      mRefreshHeader.refreshComplete();
    }
  }

  public void setRefreshListener(OnRefreshListener mRefreshListener) {
    this.mRefreshListener = mRefreshListener;
  }

  public interface OnRefreshListener {
    void onRefresh();
  }
}
