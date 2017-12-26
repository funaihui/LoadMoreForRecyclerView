package com.wizardev.loadmoreforrecyclerview;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.wizardev.loadmoreforrecyclerview.adapter.BaseListAdapter;
import com.wizardev.loadmoreforrecyclerview.base.BaseViewHolder;
import com.wizardev.loadmoreforrecyclerview.view.LoadMoreFooter;
import com.wizardev.loadmoreforrecyclerview.view.LoadMoreRecyclerView;
import com.wizardev.loadmoreforrecyclerview.view.SampleFooter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private LoadMoreRecyclerView mRecyclerView;
    private LoadMoreAdapter mLoadMoreAdapter;
    private List<String> mDatas = new ArrayList<>();
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 10000) {
                if (mDatas.size() > 50) {
                    mRecyclerView.setNoMore(true);
                    return false;
                }
                mRecyclerView.refreshComplete(10);
                mDatas.add("1");
                mLoadMoreAdapter.addData(mDatas);
                mLoadMoreAdapter.notifyDataSetChanged();
            }
            return false;
        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.recyclerView);
        initData();
        setupRecyclerView();
    }

    private void initData() {
        for (int i = 0; i < 15; i++) {
            mDatas.add("");
        }
    }

    private void setupRecyclerView() {
        mLoadMoreAdapter = new LoadMoreAdapter(this, mDatas);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        /*SampleFooter sampleFooter = new SampleFooter(this);
        mLoadMoreAdapter.addFooterView(sampleFooter);*/
        mRecyclerView.setLoadMoreEnabled(true);
        mRecyclerView.setLoadMoreFooter(new LoadMoreFooter(this));
        mRecyclerView.setAdapter(mLoadMoreAdapter);

        mRecyclerView.setOnLoadMoreListener(new LoadMoreRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                requestDataFromNet();//模拟请求数据
            }
        });
    }

    private void requestDataFromNet() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1500);
                    Message message = Message.obtain();
                    message.what = 10000;
                    mHandler.sendMessage(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    class LoadMoreAdapter extends BaseListAdapter<String> {
        public LoadMoreAdapter(Context context, List<String> datas) {
            super(context, datas);
        }

        @Override
        public void onBindItemHolder(BaseViewHolder holder, int position) {

        }

        @Override
        public int getLayoutId() {
            return R.layout.normal_item;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(null);
    }
}
