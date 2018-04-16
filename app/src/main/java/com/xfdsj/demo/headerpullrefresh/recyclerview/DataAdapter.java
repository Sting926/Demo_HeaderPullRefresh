package com.xfdsj.demo.headerpullrefresh.recyclerview;

import android.widget.TextView;
import com.xfdsj.demo.R;
import java.util.List;

/**
 * Created by Lzx on 2016/12/30.
 */

public class DataAdapter extends BaseRvAdapter<ItemModel> {

  public DataAdapter(int layoutResId, List<ItemModel> data) {
    super(layoutResId, data);
  }

  @Override public void onBindItemHolder(BaseViewHolder holder, ItemModel item) {
    TextView titleText = holder.getView(R.id.info_text);
    titleText.setText(item.title);
  }
}
