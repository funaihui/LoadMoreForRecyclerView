package com.wizardev.loadmoreforrecyclerview.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.wizardev.loadmoreforrecyclerview.adapter.BaseListAdapter;
import com.wizardev.loadmoreforrecyclerview.interfaces.ILoadMoreFooter;

/**
 * Created by wizardev on 17-12-22.
 */

public class LoadMoreRecyclerView extends RecyclerView {
    private LayoutManagerType layoutManagerType;
    private int mLastVisibleItemPosition;
    private int[] lastPositions;
    private boolean mLoadMoreEnabled;
    private boolean isNoMore;
    private ILoadMoreFooter mILoadMoreFooter;
    private boolean mLoadingData = false;//是否正在加载数据
    private View mFootView;
    private OnLoadMoreListener mLoadMoreListener;
    private static final String TAG = "LoadMoreRecyclerView";


    public LoadMoreRecyclerView(Context context) {
        this(context, null);
    }

    public LoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setLoadMoreFooter(new LoadMoreFooter(context));
    }

    //在此方法中获取RecyclerView的lastVisibleItemPosition
    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);
        LayoutManager layoutManager = getLayoutManager();

        if (layoutManagerType == null) {
            if (layoutManager instanceof LinearLayoutManager) {
                layoutManagerType = LayoutManagerType.LinearLayout;
            } else if (layoutManager instanceof GridLayoutManager) {
                layoutManagerType = LayoutManagerType.GridLayout;
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                layoutManagerType = LayoutManagerType.StaggeredGridLayout;
            } else {
                throw new RuntimeException("LayoutManager不符合规范!");
            }
        }

        switch (layoutManagerType) {
            case LinearLayout:
                mLastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                break;
            case GridLayout:
                mLastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
                break;
            case StaggeredGridLayout:
                StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                if (lastPositions == null) {
                    lastPositions = new int[staggeredGridLayoutManager.getSpanCount()];
                    staggeredGridLayoutManager.findLastVisibleItemPositions(lastPositions);
                }
                mLastVisibleItemPosition = findMax(lastPositions);
                break;
        }
    }

    //此方法用来确定LayoutManager为staggeredGridLayoutManager时的最后一个可见的item的position
    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    @Override
    public void onScrollStateChanged(int state) {
        //手指滑动后离开屏幕的状态
        if (state == RecyclerView.SCROLL_STATE_IDLE) {
            if (mLoadMoreEnabled) {
                LayoutManager layoutManager = getLayoutManager();
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                //手指滑动后松开回调
                Log.i(TAG, "onScrollStateChanged: SCROLL_STATE_IDLE");
            /*    Log.i(TAG, "totalItemCount: "+totalItemCount);
                Log.i(TAG, "mLastVisibleItemPosition: "+mLastVisibleItemPosition);*/
                if (visibleItemCount > 0
                        && mLastVisibleItemPosition >= totalItemCount-1
                        && totalItemCount>visibleItemCount
                        && !isNoMore) {
                    mFootView.setVisibility(View.VISIBLE);

                    if (mLoadingData) {
                        return;
                    } else {
                        mLoadingData = true;
                        mILoadMoreFooter.onLoading();
                        if (mLoadMoreListener != null) {
                            mLoadMoreListener.onLoadMore();
                        }
                    }
                }
            }
        }
        if (state == RecyclerView.SCROLL_STATE_DRAGGING) {
            //手指在屏幕上拖动时调用
            Log.i(TAG, "onScrollStateChanged: SCROLL_STATE_DRAGGING");
        }
        if (state == RecyclerView.SCROLL_STATE_SETTLING) {
            //惯性滑动时回调
            Log.i(TAG, "onScrollStateChanged: SCROLL_STATE_SETTLING");
        }
    }

    public enum LayoutManagerType {
        LinearLayout,
        StaggeredGridLayout,
        GridLayout
    }

    //设置RecyclerView是否允许加载更多
    public void setLoadMoreEnabled(boolean enabled) {
        mLoadMoreEnabled = enabled;
    }

    //设置loadMoreFooter
    public void setLoadMoreFooter(ILoadMoreFooter loadMoreFooter) {
        this.mILoadMoreFooter = loadMoreFooter;
        mFootView = loadMoreFooter.getFooterView();
        mFootView.setVisibility(GONE);
    }

    public void refreshComplete(int pageSize) {
//        this.mPageSize = pageSize;
        if (mLoadingData) {
            mLoadingData = false;
            mILoadMoreFooter.onComplete();
        }

    }

    /**
     * 设置是否已加载全部
     * @param noMore
     */
    public void setNoMore(boolean noMore){
        mLoadingData = false;
        isNoMore = noMore;
        if(isNoMore) {
            mILoadMoreFooter.LoadNoMore();
        } else {
            mILoadMoreFooter.onComplete();
        }
    }
    public interface OnLoadMoreListener {

        void onLoadMore();

    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        mLoadMoreListener = listener;
    }

    @Override
    public void setAdapter(Adapter adapter) {
        BaseListAdapter baseListAdapter = (BaseListAdapter) adapter;
        super.setAdapter(baseListAdapter);

        if (mLoadMoreEnabled && baseListAdapter.getFooterViewCount()==0) {
            baseListAdapter.addFooterView(mFootView);
        }

    }
}
