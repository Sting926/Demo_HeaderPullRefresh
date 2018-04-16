package com.xfdsj.demo.headerzoom.recyclerview;

import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.github.jdsjlzx.util.WeakHandler;
import com.xfdsj.demo.R;
import java.util.ArrayList;

import static android.view.View.OVER_SCROLL_NEVER;

public class MomentsHeaderRecyclerViewActivity extends AppCompatActivity {

  private static final String TAG = "HeaderZoom";

  /** 服务器端一共多少条数据 */
  private static final int TOTAL_COUNTER = 34;//如果服务器没有返回总数据或者总页数，这里设置为最大值比如10000，什么时候没有数据了根据接口返回判断

  /** 每一页展示多少条数据 */
  private static final int REQUEST_COUNT = 10;

  /** 已经获取到多少条数据了 */
  private int mCurrentCounter = 0;

  private HeaderZoomRecyclerView mRecyclerView = null;

  private DataAdapter mDataAdapter = null;

  private LRecyclerViewAdapter mLRecyclerViewAdapter = null;

  //WeakHandler必须是Activity的一个实例变量.原因详见：http://dk-exp.com/2015/11/11/weak-handler/
  private WeakHandler mHandler = new WeakHandler() {
    @Override public void handleMessage(Message msg) {
      super.handleMessage(msg);
      switch (msg.what) {

        case -1:

          int currentSize = mDataAdapter.getItemCount();

          //模拟组装10个数据
          ArrayList<ItemModel> newList = new ArrayList<>();
          for (int i = 0; i < 10; i++) {
            if (newList.size() + currentSize >= TOTAL_COUNTER) {
              break;
            }

            ItemModel item = new ItemModel();
            item.id = currentSize + i;
            item.title = "item" + (item.id);

            newList.add(item);
          }

          addItems(newList);

          mRecyclerView.refreshComplete(REQUEST_COUNT);

          break;
        case -3:
          mRecyclerView.refreshComplete(REQUEST_COUNT);
          notifyDataSetChanged();
          mRecyclerView.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
            @Override public void reload() {
              requestData();
            }
          });

          break;
        default:
          break;
      }
    }
  };

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
    //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
    mRecyclerView = new HeaderZoomRecyclerView(this);
    setContentView(mRecyclerView);

    mRecyclerView.setOverScrollMode(OVER_SCROLL_NEVER);
    mRecyclerView.setRefreshHeader(new MomentsRefreshHeader(this));

    mDataAdapter = new DataAdapter(R.layout.sample_item_text, new ArrayList<ItemModel>());
    mLRecyclerViewAdapter = new LRecyclerViewAdapter(mDataAdapter);
    mRecyclerView.setAdapter(mLRecyclerViewAdapter);

    mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    mRecyclerView.setRefreshProgressStyle(ProgressStyle.LineSpinFadeLoader);
    mRecyclerView.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);
    mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);

    //add a HeaderView
    //final View header = LayoutInflater.from(this).inflate(R.layout.qzone_header, null);
    //mLRecyclerViewAdapter.addHeaderView(header);

    //mRecyclerView.setPullRefreshEnabled(false);
    //requestData();

    mRecyclerView.setOnRefreshListener(new OnRefreshListener() {
      @Override public void onRefresh() {

        mDataAdapter.clear();
        mLRecyclerViewAdapter.notifyDataSetChanged();//fix bug:crapped or attached views may not be recycled. isScrap:false isAttached:true
        mCurrentCounter = 0;
        requestData();
      }
    });

    //是否禁用自动加载更多功能,false为禁用, 默认开启自动加载更多功能
    mRecyclerView.setLoadMoreEnabled(true);

    mRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
      @Override public void onLoadMore() {

        if (mCurrentCounter < TOTAL_COUNTER) {
          // loading more
          requestData();
        } else {
          //the end
          mRecyclerView.setNoMore(true);
        }
      }
    });

    mRecyclerView.setLScrollListener(new HeaderZoomRecyclerView.LScrollListener() {

      @Override public void onScrollUp() {
      }

      @Override public void onScrollDown() {
      }

      @Override public void onScrolled(int distanceX, int distanceY) {
      }

      @Override public void onScrollStateChanged(int state) {

      }
    });

    //设置底部加载文字提示
    mRecyclerView.setFooterViewHint("拼命加载中", "我是有底线的", "网络不给力啊，点击再试一次吧");

    mRecyclerView.refresh();
  }

  private void notifyDataSetChanged() {
    mLRecyclerViewAdapter.notifyDataSetChanged();
  }

  private void addItems(ArrayList<ItemModel> list) {

    mDataAdapter.addAll(list);
    mCurrentCounter += list.size();
  }

  /**
   * 模拟请求网络
   */
  private void requestData() {
    Log.d(TAG, "requestData");
    new Thread() {

      @Override public void run() {
        super.run();

        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        mHandler.sendEmptyMessage(-1);
      }
    }.start();
  }
}
