package com.xfdsj.demo.headerpullrefresh;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.xfdsj.demo.headerpullrefresh.listview.MomentsHeaderListViewActivity;
import com.xfdsj.demo.headerpullrefresh.listview.QzoneHeaderListViewActivity;
import com.xfdsj.demo.headerpullrefresh.recyclerview.MomentsHeaderRecyclerViewActivity;
import com.xfdsj.demo.headerpullrefresh.recyclerview.QzoneHeaderRecyclerViewActivity;

public class MainActivity extends AppCompatActivity {

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
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
