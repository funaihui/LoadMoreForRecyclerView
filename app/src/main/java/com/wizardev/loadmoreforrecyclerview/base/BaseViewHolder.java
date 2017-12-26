package com.wizardev.loadmoreforrecyclerview.base;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

/**
 * Created by wizardev on 17-12-20.
 */

public class BaseViewHolder extends RecyclerView.ViewHolder{
    private SparseArray<View> views;



    public BaseViewHolder(View itemView) {
        super(itemView);
        this.views = new SparseArray<>();
    }

    public <T extends View> T getView(int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }
}
