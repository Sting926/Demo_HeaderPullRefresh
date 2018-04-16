package com.xfdsj.demo.headerpullrefresh.listview;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;

import static android.view.View.OVER_SCROLL_NEVER;

public class MomentsHeaderListViewActivity extends AppCompatActivity {

  private HeaderPullRefreshListView lv;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    lv = new HeaderPullRefreshListView(this);
    setContentView(lv);

    lv.setOverScrollMode(OVER_SCROLL_NEVER);

    ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new String[] {
        "朋友圈", "朋友圈", "朋友圈", "朋友圈", "朋友圈", "朋友圈", "朋友圈", "朋友圈", "朋友圈", "朋友圈", "朋友圈", "朋友圈", "朋友圈", "朋友圈", "朋友圈", "朋友圈", "朋友圈", "朋友圈", "朋友圈", "朋友圈",
        "朋友圈", ".........."
    });

    //头部拉伸 朋友圈
    MomentsRefreshHeader header = new MomentsRefreshHeader(this);
    lv.addHeaderView(header);
    lv.setHeaderDividersEnabled(false);
    lv.setRefreshHeader(header);
    lv.setAdapter(adapter);
    lv.setRefreshListener(new HeaderPullRefreshListView.OnRefreshListener() {
      @Override public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
          @Override public void run() {
            lv.refreshComplete();
          }
        }, 1000);
      }
    });
    lv.refresh();
  }
}
