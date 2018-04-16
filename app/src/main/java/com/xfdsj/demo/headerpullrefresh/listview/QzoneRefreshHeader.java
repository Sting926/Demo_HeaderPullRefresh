package com.xfdsj.demo.headerpullrefresh.listview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.github.jdsjlzx.util.WeakHandler;
import com.xfdsj.demo.headerpullrefresh.R;

public class QzoneRefreshHeader extends FrameLayout implements IRefreshHeader {

  private ImageView mHeaderView; // 头图
  private int mHeaderViewHeight; // 头图高度
  private int mDeltaHeight; // 头图和头部布局的差值
  private ImageView mRefreshView; // 旋转刷新的图片
  private float mRefreshHideTranslationY; // 刷新图片上移的最大距离
  private float mRefreshShowTranslationY; // 刷新图片下拉的最大移动距离
  private float mRotateAngle; // 旋转角度
  private int mState = STATE_NORMAL;

  private WeakHandler mHandler = new WeakHandler();

  public QzoneRefreshHeader(Context context) {
    super(context);
    initView();
  }

  /**
   * @param context
   * @param attrs
   */
  public QzoneRefreshHeader(Context context, AttributeSet attrs) {
    super(context, attrs);
    initView();
  }

  private void initView() {
    AbsListView.LayoutParams lp = new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    this.setLayoutParams(lp);
    this.setPadding(0, 0, 0, 0);
    inflate(getContext(), R.layout.qzone_header, this);
    mHeaderView = findViewById(R.id.iv_header);
    mRefreshView = findViewById(R.id.iv_refresh);
    measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    mHeaderViewHeight = mHeaderView.getMeasuredHeight();
    mDeltaHeight = getMeasuredHeight() - mHeaderViewHeight;
    mRefreshHideTranslationY = -mRefreshView.getMeasuredHeight() - 20;
    mRefreshShowTranslationY = mRefreshView.getMeasuredHeight();
  }

  private int getHeaderViewHeight() {
    return mHeaderView.getHeight();
  }

  private void setHeaderViewHeight(int height) {
    if (height < mHeaderViewHeight) height = mHeaderViewHeight;
    mHeaderView.getLayoutParams().height = height;
    mHeaderView.requestLayout();
  }

  public void setState(int state) {
    if (state == mState) return;

    if (state == STATE_REFRESHING) {  // 显示进度
      mRefreshView.setTranslationY(mRefreshShowTranslationY);
      refreshing();
    } else if (state == STATE_DONE) {
      reset();
    }

    mState = state;
  }

  @Override public void onRefreshing() {
    setState(STATE_REFRESHING);
  }

  @Override public void onMove(float offSet) {
    int top = getTop();// 相对父容器listview的顶部位置 负数表示向上划出父容器的距离
    int currentHeight = getHeaderViewHeight();
    int targetHeight = currentHeight - (int) offSet;
    if (offSet < 0 && top == 0) { // 向下拉
      setHeaderViewHeight(targetHeight);
      refreshTranslation(currentHeight, offSet); // 向上推
    } else if (offSet > 0 && currentHeight > mHeaderViewHeight) {
      layout(getLeft(), 0, getRight(), targetHeight + mDeltaHeight); //重新布局让header显示在顶端，直到不再缩小图片
      setHeaderViewHeight(targetHeight);
      refreshTranslation(currentHeight, offSet);
    }
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

  @Override public boolean onRelease() {
    boolean isOnRefresh = false;
    int currentHeight = mHeaderView.getLayoutParams().height;// 使用 mHeaderView.getLayoutParams().height 可以防止快速快速下拉的时候图片不回弹
    if (currentHeight > mHeaderViewHeight) {
      if ((currentHeight - mHeaderViewHeight) / 2 > mRefreshShowTranslationY - mRefreshHideTranslationY && mState < STATE_REFRESHING) {
        setState(STATE_REFRESHING);
        isOnRefresh = true;
      }
      headerRest();
    }
    if (!isOnRefresh && mRefreshView.getTranslationY() != mRefreshHideTranslationY) {
      refreshRest();
    }
    return isOnRefresh;
  }

  @Override public void refreshComplete() {
    mHandler.postDelayed(new Runnable() {
      public void run() {
        setState(STATE_DONE);
      }
    }, 200);
  }

  @Override public View getHeaderView() {
    return this;
  }

  @Override public int getOffset() {
    return getHeight() - mHeaderViewHeight;
  }

  private void refreshing() {
    mHandler.postDelayed(new Runnable() {
      @Override public void run() {
        if (mState == STATE_REFRESHING) {
          mRefreshView.setRotation(mRotateAngle += 8);
          mHandler.post(this);
        }
      }
    }, 50);
  }

  public void reset() {
    refreshRest();
    mHandler.postDelayed(new Runnable() {
      public void run() {
        setState(STATE_NORMAL);
      }
    }, 500);
  }

  private void headerRest() {
    ValueAnimator animator = ValueAnimator.ofInt(mHeaderView.getLayoutParams().height, mHeaderViewHeight);
    //animator.setStartDelay(60);
    animator.setDuration(300).start();
    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override public void onAnimationUpdate(ValueAnimator animation) {
        if (mHeaderView.getLayoutParams().height == mHeaderViewHeight) { // 停止动画，防止快速上划松手后动画产生抖动
          animation.cancel();
        } else {
          setHeaderViewHeight((Integer) animation.getAnimatedValue());
        }
      }
    });
  }

  private void refreshRest() {
    ValueAnimator animator = ValueAnimator.ofFloat(mRefreshView.getTranslationY(), mRefreshHideTranslationY);
    animator.setStartDelay(60);
    animator.setDuration(300).start();
    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override public void onAnimationUpdate(ValueAnimator animation) {
        if (mRefreshView.getTranslationY() == mRefreshHideTranslationY) {
          animation.cancel();
        } else {
          mRefreshView.setTranslationY((Float) animation.getAnimatedValue());
        }
      }
    });
  }
}