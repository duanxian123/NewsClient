package com.example.linghao.newsclient.base;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.linghao.newsclient.R;
import com.example.linghao.newsclient.activity.MainActivity;

/**
 * Created by linghao on 2017/6/26.
 */

public class BasePager {
    public final Context context;//MainActivity
    public View rootView;
    public TextView tv_title;
    public FrameLayout fl_content;
    public ImageButton ib_menu;
    public ImageButton ib_switch;
    public BasePager(Context context){
        this.context=context;
        rootView=initView();
    }
    /**
     * 用于初始化公共部分视图，并且初始化加载子视图的FrameLayout
     */
    private View initView() {
        rootView=View.inflate(context, R.layout.base_pager,null);
        tv_title=(TextView) rootView.findViewById(R.id.tv_title);
        fl_content= (FrameLayout) rootView.findViewById(R.id.fl_content);
        ib_menu= (ImageButton) rootView.findViewById(R.id.bt_menu);
        ib_switch=(ImageButton) rootView.findViewById(R.id.bt_switch_listorgrid);
        ib_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity= (MainActivity) context;
                mainActivity.getSlidingMenu().toggle();
            }
        });
        return rootView;
    }

    /**
     * 初始化数据;当子类需要初始化数据;或者绑定数据;联网请求数据并且绑定的时候，重写该方法
     */
    public  void initData(){

    }
}
