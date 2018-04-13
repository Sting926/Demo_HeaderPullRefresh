package com.xfdsj.demo.headerzoom.listview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.ListView;
import com.xfdsj.demo.R;

/**
 * Created by Mr Jian on 2018/4/2.
 */

public class HeaderZoomListView extends ListView {

  private View mHeaderView; // 头图
  private int mHeaderViewHeight; // 头图高度
  private ImageView mRefreshView; // 旋转刷新的图片
  private float mRefreshHideTranslationY; // 刷新图片上移的最大距离
  private float mRefreshShowTranslationY; // 刷新图片下拉的最大移动距离
  private float mRotateAngle; // 旋转角度

  public HeaderZoomListView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override public void onWindowFocusChanged(boolean hasWindowFocus) {
    super.onWindowFocusChanged(hasWindowFocus);
    if (hasWindowFocus) {
      this.mHeaderViewHeight = mHeaderView.getHeight();// 获取图片原始高度
      mRefreshHideTranslationY = -mRefreshView.getHeight() - 20;
      mRefreshShowTranslationY = mRefreshView.getHeight();
      mHeaderView.findViewById(R.id.iv_header).setTranslationY(-100);
      mRefreshView.setTranslationY(mRefreshHideTranslationY);
    }
  }

  /**
   * 重写滑动过度回调方法
   *
   * @param deltaY dy增量 正数是上拉 负数是下拉
   */
  @Override protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX,
      int maxOverScrollY, boolean isTouchEvent) {
    if (deltaY < 0 && isTouchEvent) { //下拉 ImageView进行放大效果
      mHeaderView.getLayoutParams().height = mHeaderView.getHeight() - deltaY;//增加高度
      mHeaderView.requestLayout();
      refreshTranslation(mHeaderView.getHeight(), deltaY);
    } else if (deltaY > 0 && mHeaderView.getHeight() > mHeaderViewHeight && isTouchEvent) { //上拉过度（ListView内容太少的时候） 减少ImageView大小
      mHeaderView.getLayoutParams().height = mHeaderView.getHeight() - deltaY;//减少高度
      mHeaderView.requestLayout();
      refreshTranslation(mHeaderView.getHeight(), deltaY);
    }
    return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
  }

  /**
   * 下拉后往上推 需要ImageView变小
   */
  @Override protected void onScrollChanged(int l, int t, int oldl, int oldt) {
    View header = mHeaderView;
    int top = header.getTop();//header 滑动到屏幕上方的距离
    int height = mHeaderView.getHeight();
    if (top < 0 && height + top > mHeaderViewHeight) {
      header.layout(header.getLeft(), 0, header.getRight(), header.getHeight());//防止header被ListView划上去，重新布局让header显示在顶端，直到不再缩小图片
      mHeaderView.getLayoutParams().height = height + top;
      mHeaderView.requestLayout();
      refreshTranslation(height, -top);
    }
    super.onScrollChanged(l, t, oldl, oldt);
  }

  @Override public boolean onTouchEvent(MotionEvent ev) {
    if (ev.getAction() == MotionEvent.ACTION_UP) {
      int currentHeight = mHeaderView.getLayoutParams().height;// 使用 mHeaderView.getLayoutParams().height 可以防止快速快速下拉的时候图片不回弹
      if (currentHeight > mHeaderViewHeight) {
        headerRest();
        refreshRest();
      }
    }
    return super.onTouchEvent(ev);
  }

  /**
   * refreshView在刷新区间内相对位移并跟随位移速度旋转
   */
  private void refreshTranslation(int currentHeight, float offSet) {
    if ((currentHeight - mHeaderViewHeight) / 2 < mRefreshShowTranslationY - mRefreshHideTranslationY) { // 判断是否在非刷新区间
      float translationY = mRefreshView.getTranslationY() - offSet / 2; // 布局高度增加offset 相当于距离上边距offSet / 2
      if (translationY > mRefreshShowTranslationY) {
        translationY = mRefreshShowTranslationY;
      } else if (translationY < mRefreshHideTranslationY) {
        translationY = mRefreshHideTranslationY;
      }
      if (Math.abs(translationY) != mRefreshView.getTranslationY()) {
        mRefreshView.setTranslationY(translationY);
      }
    }
    mRefreshView.setRotation(mRotateAngle -= offSet);//旋转，角度大小跟随偏移量
  }

  private void headerRest() {
    ValueAnimator animator = ValueAnimator.ofInt(mHeaderView.getHeight(), mHeaderViewHeight);
    animator.setDuration(300).start();
    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override public void onAnimationUpdate(ValueAnimator animation) {
        mHeaderView.getLayoutParams().height = (int) animation.getAnimatedValue();
        mHeaderView.requestLayout();
      }
    });
    animator.start();
  }

  private void headerRest1() {
    HeaderResetAnimation animation = new HeaderResetAnimation();
    animation.setDuration(300);
    mHeaderView.startAnimation(animation);
  }

  private void refreshRest() {
    ValueAnimator animator = ValueAnimator.ofFloat(mRefreshView.getTranslationY(), mRefreshHideTranslationY);
    animator.setStartDelay(60);
    animator.setDuration(300).start();
    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override public void onAnimationUpdate(ValueAnimator animation) {
        mRefreshView.setTranslationY((Float) animation.getAnimatedValue());
      }
    });
    animator.start();
  }

  /**
   * 重置Header动画
   */
  public class HeaderResetAnimation extends Animation {

    @Override protected void applyTransformation(float interpolatedTime, Transformation t) {
      int currentHeight = mHeaderView.getHeight();
      int deltaHeight = currentHeight - mHeaderViewHeight;
      mHeaderView.getLayoutParams().height = (int) (currentHeight - deltaHeight * interpolatedTime);
      mHeaderView.requestLayout();
      super.applyTransformation(interpolatedTime, t);
    }
  }

  public void setHeaderView(View iv) {
    mHeaderView = iv;
  }

  public void setRefreshView(ImageView mRefreshView) {
    this.mRefreshView = mRefreshView;
  }
}
