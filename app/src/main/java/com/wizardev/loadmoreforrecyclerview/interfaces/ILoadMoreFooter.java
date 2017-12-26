package com.wizardev.loadmoreforrecyclerview.interfaces;

import android.view.View;

/**
 * Created by wizardev on 17-12-18.
 */

public interface ILoadMoreFooter {

    void onReset();//正常的状态下

    void onLoading();//正在加载中的状态

    void onComplete();//已经加载完成

    void LoadNoMore();//没有更多数据

    View getFooterView();//获取FooterView

}
