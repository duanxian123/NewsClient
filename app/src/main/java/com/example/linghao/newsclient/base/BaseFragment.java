package com.example.linghao.newsclient.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by linghao on 2017/6/26.
 */

public abstract class BaseFragment extends Fragment {
    public Activity context;
    /**
     * 创建碎片时回调这方法
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getActivity();
    }

    @Nullable
    @Override
    /**
     * 当视图被创建的时候回调
     */
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return initView();
    }

    public abstract View initView() ;//抽象类没有方法体；子类实现视图

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    public void initData() {
        /**
         * 1.如果自页面没有数据，联网请求数据，并且绑定到initView初始化的视图上
         * 2.绑定到initView初始化的视图上
         */
    }


}
