package com.wizardev.loadmoreforrecyclerview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.RelativeLayout;

import com.wizardev.loadmoreforrecyclerview.R;
import com.wizardev.loadmoreforrecyclerview.interfaces.ILoadMoreFooter;

/**
 * Created by wizardev on 17-12-22.
 */

public class LoadMoreFooter extends RelativeLayout implements ILoadMoreFooter{
    private State mState;
    private View mLoadingView; //正在加载的图标
    private View mTheEndView; //加载全部的图标
    private static final String TAG = "LoadMoreFooter";


    public LoadMoreFooter(Context context) {
        super(context);
        init(context);
    }

    public LoadMoreFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LoadMoreFooter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);

    }

    private void init(Context context) {
        inflate(context, R.layout.layout_recyclerview_footer, this);
        onReset();
    }

    @Override
    public void onReset() {
        onComplete();
    }

    @Override
    public void onLoading() {
        Log.i(TAG, "onLoading: ");
        setState(State.Loading);
    }



    @Override
    public void onComplete() {
        setState(State.Normal);
    }

    @Override
    public void LoadNoMore() {
        setState(State.NoMore);
    }

    private void setState(State state) {
        if (mState == state) {
            return;
        }
        mState = state;
        switch (state) {
            case Normal:
                if (mLoadingView != null) {
                    mLoadingView.setVisibility(GONE);
                }

                if (mTheEndView != null) {
                    mTheEndView.setVisibility(GONE);
                }

                break;
            case Loading:

                if (mTheEndView != null) {
                    mTheEndView.setVisibility(GONE);
                }

                if (mLoadingView == null) {
                    ViewStub viewStub = findViewById(R.id.loading_viewstub);
                    mLoadingView= viewStub.inflate();
                }
                mLoadingView.setVisibility(VISIBLE);

                break;
            case NoMore:
                if (mLoadingView != null) {
                    mLoadingView.setVisibility(GONE);
                }
                if (mTheEndView == null) {
                    ViewStub viewStub = findViewById(R.id.end_viewstub);
                    mTheEndView= viewStub.inflate();
                }
                mTheEndView.setVisibility(VISIBLE);
                break;
        }
    }

    @Override
    public View getFooterView() {
        return this;
    }



    public enum State {
        Normal,
        Loading,
        NoMore,
    }

}
