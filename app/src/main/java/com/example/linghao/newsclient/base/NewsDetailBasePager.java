package com.example.linghao.newsclient.base;

import android.content.Context;
import android.view.View;

/**
 * Created by linghao on 2017/6/28.
 */

public abstract class NewsDetailBasePager {
    public final Context context;
    public View rootView;

    public NewsDetailBasePager(Context context){
        this.context=context;
        rootView=initView();
    }

    public abstract View initView() ;
    public void initData(){
    }
}
