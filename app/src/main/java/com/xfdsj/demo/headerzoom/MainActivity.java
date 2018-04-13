package com.xfdsj.demo.headerzoom;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import com.xfdsj.demo.R;
import com.xfdsj.demo.headerzoom.listview.HeaderZoomListViewActivity;
import com.xfdsj.demo.headerzoom.recyclerview.HeaderZoomRecyclerViewActivity;

public class MainActivity extends AppCompatActivity {

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

/*    RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
    rotateAnimation.setDuration(3000);
    rotateAnimation.setRepeatCount(-1);
    findViewById(R.id.btn_recyclerview).startAnimation(rotateAnimation);*/
  }

  public void listViewClick(View view) {
    startActivity(new Intent(this, HeaderZoomListViewActivity.class));
  }

  public void recyclerViewClick(View view) {
    startActivity(new Intent(this, HeaderZoomRecyclerViewActivity.class));
  }
}
