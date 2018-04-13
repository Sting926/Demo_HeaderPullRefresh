package com.xfdsj.demo.headerzoom.listview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import com.xfdsj.demo.R;

public class HeaderZoomListViewActivity extends AppCompatActivity {

  private HeaderZoomListView lv;
  private ImageView headerImg;
  private ImageView refreshImg;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_header_zoom_list_view);
    lv = findViewById(R.id.lv);

    ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new String[] {
        "111111111111111", "111111111111111", "111111111111111", "111111111111111", "111111111111111", "111111111111111", "111111111111111",
        "111111111111111", "111111111111111", "111111111111111", "111111111111111", "111111111111111", "111111111111111", "111111111111111",
        "111111111111111", "111111111111111", "111111111111111", "111111111111111", "111111111111111", "111111111111111", "111111111111111",
        ".........."
    });
    View header = getLayoutInflater().inflate(R.layout.zoom_header, null);
    headerImg = header.findViewById(R.id.iv_header);
    refreshImg = header.findViewById(R.id.iv_refresh);
    lv.addHeaderView(header);
    lv.setHeaderDividersEnabled(false);
    lv.setHeaderView(header);
    lv.setRefreshView(refreshImg);
    lv.setAdapter(adapter);
  }
}
