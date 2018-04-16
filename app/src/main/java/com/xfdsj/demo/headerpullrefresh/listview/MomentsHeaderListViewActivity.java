package com.xfdsj.demo.headerpullrefresh.listview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import com.xfdsj.demo.R;

import static android.view.View.OVER_SCROLL_NEVER;

public class MomentsHeaderListViewActivity extends AppCompatActivity {

  private MomentsHeaderListView lv;
  private ImageView refreshImg;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    lv = new MomentsHeaderListView(this);
    setContentView(lv);

    lv.setOverScrollMode(OVER_SCROLL_NEVER);

    ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new String[] {
        "111111111111111", "111111111111111", "111111111111111", "111111111111111", "111111111111111", "111111111111111", "111111111111111",
        "111111111111111", "111111111111111", "111111111111111", "111111111111111", "111111111111111", "111111111111111", "111111111111111",
        "111111111111111", "111111111111111", "111111111111111", "111111111111111", "111111111111111", "111111111111111", "111111111111111",
        ".........."
    });

    //头部拉伸 朋友圈
    View header = getLayoutInflater().inflate(R.layout.moments_header, null);
    refreshImg = header.findViewById(R.id.iv_refresh);
    lv.addHeaderView(header);
    lv.setHeaderDividersEnabled(false);
    lv.setHeaderView(header);
    lv.setRefreshView(refreshImg);
    lv.setAdapter(adapter);
  }
}
