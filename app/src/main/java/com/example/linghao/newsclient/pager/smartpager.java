package com.example.linghao.newsclient.pager;

import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;

import com.example.linghao.newsclient.base.BasePager;
import com.example.linghao.newsclient.utils.LogUtil;

/**
 * Created by linghao on 2017/6/26.
 */

public class smartpager extends BasePager {
    public smartpager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("smart数据初始化");
        TextView tv=new TextView(context);
        tv_title.setText("smart");
        tv_title.setGravity(Gravity.CENTER);
    }
}