package com.example.linghao.newsclient.pager;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.linghao.newsclient.activity.MainActivity;
import com.example.linghao.newsclient.base.BasePager;
import com.example.linghao.newsclient.base.NewsDetailBasePager;
import com.example.linghao.newsclient.domain.NewsPagerBean;
import com.example.linghao.newsclient.domain.PhotosMenuDetailBean;
import com.example.linghao.newsclient.fragment.LeftmenuFragment;
import com.example.linghao.newsclient.menudetailPager.InteracDetailPager;
import com.example.linghao.newsclient.menudetailPager.NewsDetailPager;
import com.example.linghao.newsclient.menudetailPager.PhotosDetailPager;
import com.example.linghao.newsclient.menudetailPager.TopciDetailPager;
import com.example.linghao.newsclient.utils.CacheUtils;
import com.example.linghao.newsclient.utils.Constans;
import com.example.linghao.newsclient.utils.LogUtil;
import com.google.gson.Gson;
import com.google.gson.annotations.Until;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linghao on 2017/6/26.
 */

public class newspager extends BasePager {
    public newspager(Context context) {
        super(context);
    }
private List<NewsPagerBean.DataBean> data;
    private ArrayList<NewsDetailBasePager> newsDetailBasePagers;

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("news数据初始化");
        TextView tv=new TextView(context);
        tv_title.setText("news");
        tv_title.setGravity(Gravity.CENTER);
        ib_menu.setVisibility(View.VISIBLE);

        tv_title.setText("新闻中心");
        tv.setText("xinwen页面");
        tv.setGravity(Gravity.CENTER);
        fl_content.addView(tv);
        String saveJson =CacheUtils.getString(context,Constans.netsUrl);
        if(!TextUtils.isEmpty(saveJson)){
        ProcessData(saveJson);}
        getDataFromNet();
    }

    private void getDataFromNet() {
        RequestParams params=new RequestParams(Constans.netsUrl);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("成功联网"+result);
                ProcessData(result);//解析数据
                //缓存数据
                CacheUtils.putString(context,Constans.netsUrl,result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                    LogUtil.e("联网失败"+ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e("联网cancel"+cex.getMessage());
            }

            @Override
            public void onFinished() {
                LogUtil.e("结束");
            }
        });
    }

    private void ProcessData(String json) {
        LogUtil.e("1");
        NewsPagerBean bean=parseJson(json);
        data = bean.getData();
        MainActivity mainActivity= (MainActivity) context;
        //得到左侧菜单
        LeftmenuFragment leftmenuFragment=mainActivity.getLeftmenuFragment();
        newsDetailBasePagers=new ArrayList<>();
        newsDetailBasePagers.add(new NewsDetailPager(context,data.get(0)));
        newsDetailBasePagers.add(new TopciDetailPager(context,data.get(0)));
        newsDetailBasePagers.add(new PhotosDetailPager(context,data.get(2)));
        newsDetailBasePagers.add(new InteracDetailPager(context,data.get(2)));
        leftmenuFragment.setData(data);


    }

    private NewsPagerBean parseJson(String json) {
        Gson gson=new Gson();
        NewsPagerBean bean=gson.fromJson(json,NewsPagerBean.class);
     return bean;
    }

    public void switchPager(int position) {
        //设置标题
        tv_title.setText(data.get(position).getTitle());
        //移除之前内容
        fl_content.removeAllViews();
        //添加新内容
        NewsDetailBasePager detailbasepagers=newsDetailBasePagers.get(position);
        View view=detailbasepagers.rootView;
        detailbasepagers.initData();
        LogUtil.e("****2");
        fl_content.addView(view);
        if(position==2){
            ib_switch.setVisibility(View.VISIBLE);
            ib_switch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PhotosDetailPager photosDetailPager= (PhotosDetailPager) newsDetailBasePagers.get(2);
                    photosDetailPager.switchListAndGrid (ib_switch);
                }
            });

        }
        else
        {ib_switch.setVisibility(View.GONE);

        }

    }


}