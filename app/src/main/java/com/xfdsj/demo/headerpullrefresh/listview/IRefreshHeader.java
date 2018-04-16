package com.xfdsj.demo.headerpullrefresh.listview;

import android.view.View;

public interface IRefreshHeader {

  int STATE_NORMAL = 0;
  int STATE_REFRESHING = 1;
  int STATE_DONE = 2;

  /**
   * 正在刷新
   */
  void onRefreshing();

  /**
   * 下拉移动
   */
  void onMove(float offSet);

  /**
   * 下拉松开
   */
  boolean onRelease();

  /**
   * 下拉刷新完成
   */
  void refreshComplete();

  /**
   * 获取HeaderView
   */
  View getHeaderView();

  /**
   * 获取Header拉伸的长度
   */
  int getOffset();
}