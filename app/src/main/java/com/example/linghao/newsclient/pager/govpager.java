package com.example.linghao.newsclient.pager;

import android.content.Context;
import android.view.Gravity;

import com.example.linghao.newsclient.base.BasePager;
import com.example.linghao.newsclient.utils.LogUtil;

/**
 * Created by linghao on 2017/6/26.
 */

public class govpager extends BasePager {
    public govpager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("GOV数据初始化");
        tv_title.setText("gov");
        tv_title.setGravity(Gravity.CENTER);
    }
}
