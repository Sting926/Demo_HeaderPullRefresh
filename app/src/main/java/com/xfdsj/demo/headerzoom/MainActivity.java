package com.xfdsj.demo.headerzoom;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.xfdsj.demo.R;
import com.xfdsj.demo.headerzoom.listview.QzoneHeaderListViewActivity;
import com.xfdsj.demo.headerzoom.listview.MomentsHeaderListViewActivity;
import com.xfdsj.demo.headerzoom.recyclerview.QzoneHeaderRecyclerViewActivity;
import com.xfdsj.demo.headerzoom.recyclerview.MomentsHeaderRecyclerViewActivity;

public class MainActivity extends AppCompatActivity {

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

/*    RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
    rotateAnimation.setDuration(3000);
    rotateAnimation.setRepeatCount(-1);
    findViewById(R.id.btn_recyclerview).startAnimation(rotateAnimation);*/
  }

  public void qzoneListViewClick(View view) {
    startActivity(new Intent(this, QzoneHeaderListViewActivity.class));
  }

  public void momentsListViewClick(View view) {
    startActivity(new Intent(this, MomentsHeaderListViewActivity.class));
  }

  public void qzoneRecyclerViewClick(View view) {
    startActivity(new Intent(this, QzoneHeaderRecyclerViewActivity.class));
  }

  public void momentsRecyclerViewClick(View view) {
    startActivity(new Intent(this, MomentsHeaderRecyclerViewActivity.class));
  }
}
