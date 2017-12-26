package com.wizardev.loadmoreforrecyclerview.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wizardev.loadmoreforrecyclerview.base.BaseViewHolder;
import com.wizardev.loadmoreforrecyclerview.interfaces.ILoadMoreFooter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by wizardev on 17-12-18.
 */

public abstract class BaseListAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {
    private static final int FOOTER_VIEW_TYPE = 1001;
    private List<View> mFooterViews = new ArrayList<>();
    private Context mContext;
    private List<T> datas;
    private LayoutInflater mInflater;

    public BaseListAdapter(Context context, List<T> datas) {
        mContext = context;
        this.datas = datas;
        mInflater = LayoutInflater.from(context);
    }


    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == FOOTER_VIEW_TYPE) {
            return new BaseViewHolder(getFooterView());
        }
        return new BaseViewHolder(mInflater.inflate(getLayoutId(), parent, false));
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        onBindItemHolder(holder, position);
    }


    public abstract void onBindItemHolder(BaseViewHolder holder, int position);

    public abstract int getLayoutId();

    public void addFooterView(View view) {

        removeFooterView();//移除已经存在的FooterView
        mFooterViews.add(view);
    }

    private void removeFooterView() {
        if (getFooterViewCount() > 0) {
            View view = getFooterView();
            mFooterViews.remove(view);
            this.notifyDataSetChanged();
        }
    }

    public boolean isFooter(int position) {
        int lastPosition = getItemCount() - getFooterViewCount();
        return getFooterViewCount() > 0 && position >= lastPosition;
    }


    public int getFooterViewCount() {
        return mFooterViews.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (isFooter(position)) {
            return FOOTER_VIEW_TYPE;
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public View getFooterView() {

        return getFooterViewCount() > 0 ? mFooterViews.get(0) : null;
    }

    public void addData(Collection collection) {
        if (datas != null) {
            datas.addAll(collection);
        }
    }

}
