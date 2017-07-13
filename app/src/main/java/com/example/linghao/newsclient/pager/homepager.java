package com.example.linghao.newsclient.pager;

import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;

import com.example.linghao.newsclient.base.BasePager;
import com.example.linghao.newsclient.utils.LogUtil;

/**
 * Created by linghao on 2017/6/26.
 */

public class homepager extends BasePager {
    public homepager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("home数据初始化");
        TextView tv=new TextView(context);
        tv_title.setText("主页面");
        tv.setText("主页面");
        tv.setGravity(Gravity.CENTER);
        fl_content.addView(tv);
    }
}
