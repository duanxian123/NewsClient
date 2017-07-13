package com.example.linghao.newsclient.menudetailPager.tabledetailpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.linghao.newsclient.R;
import com.example.linghao.newsclient.base.NewsDetailBasePager;
import com.example.linghao.newsclient.domain.NewsPagerBean;
import com.example.linghao.newsclient.domain.TabDetailBean;
import com.example.linghao.newsclient.utils.CacheUtils;
import com.example.linghao.newsclient.utils.Constans;
import com.example.linghao.newsclient.utils.DensityUtil;
import com.example.linghao.newsclient.utils.LogUtil;
import com.example.linghao.newsclient.view.HorizontalScollViewPager;
import com.example.refreshlistviewlibrary.RefreshListview;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.extras.SoundPullEventListener;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

/**
 * Created by linghao on 2017/6/29.
 */

public class ZhuantiDetailPager extends NewsDetailBasePager {
    private TextView tv_title;
    private ListView listview;
    private ViewPager viewpager;
    private LinearLayout ll_group;
    private final NewsPagerBean.DataBean.ChildrenBean childrenBean;
    private View view;
    private String url;
    private List<TabDetailBean.DataBean.TopnewsBean>  topnews;//顶部新闻
    private  int prePosition=0;
    private List<TabDetailBean.DataBean.NewsBean> news;
    private  newsdetailAdapter adapter;
    /**
     * 下一页的联网路径
     */
    private String moreUrl;
    /**
     * 是否加载更多
     */
    private boolean isLoadMore = false;
    private PullToRefreshListView mPullRefreshListView;

    public ZhuantiDetailPager(Context context, NewsPagerBean.DataBean.ChildrenBean childrenBean) {
        super(context);
        this.childrenBean = childrenBean;
    }

    @Override
    public View initView() {
        view = new View(context);
        view = View.inflate(context, R.layout.zhuanti_detail_pager, null);
        mPullRefreshListView = (PullToRefreshListView) view.findViewById(R.id.pull_refresh_list);
        listview= mPullRefreshListView.getRefreshableView();
        /**
         * Add Sound Event Listener
         */
        SoundPullEventListener<ListView> soundListener = new SoundPullEventListener<ListView>(context);
        soundListener.addSoundEvent(PullToRefreshBase.State.PULL_TO_REFRESH, R.raw.pull_event);
        soundListener.addSoundEvent(PullToRefreshBase.State.RESET, R.raw.reset_sound);
        soundListener.addSoundEvent(PullToRefreshBase.State.REFRESHING, R.raw.refreshing_sound);
        mPullRefreshListView.setOnPullEventListener(soundListener);
        View TopView=View.inflate(context,R.layout.topnews,null);
        viewpager= (HorizontalScollViewPager) TopView.findViewById(R.id.viewpager);
        tv_title= (TextView) TopView.findViewById(R.id.tv_title);
        ll_group= (LinearLayout) TopView.findViewById(R.id.ll_point);
        //把顶部轮播图部分视图，以头的方式添加到ListView中
        listview.addHeaderView(TopView);

//        listview.addTopNewsView(TopView);

        //设置监听下拉刷新
       mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
           @Override
           public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
               getDataFromNet();
           }

           @Override
           public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
               if(TextUtils.isEmpty(moreUrl)){
                   //没有更多数据
                   Toast.makeText(context, "没有更多数据", Toast.LENGTH_SHORT).show();
//                   listview.onRefreshFinish(false);
                   mPullRefreshListView.onRefreshComplete();
               }else{
                   getMoreDataFromNet();
               }

           }
       });


        return view;
    }
//    class MyOnRefreshListener implements RefreshListview.OnRefreshListener {
//
//        @Override
//        public void onPullDownRefresh() {
////            Toast.makeText(context, "下拉刷新被回调了", Toast.LENGTH_SHORT).show();
//            getDataFromNet();
//        }
//
//        @Override
//        public void onLoadMore() {
////
//            if(TextUtils.isEmpty(moreUrl)){
//                //没有更多数据
//                Toast.makeText(context, "没有更多数据", Toast.LENGTH_SHORT).show();
//                listview.onRefreshFinish(false);
//            }else{
//                getMoreDataFromNet();
//            }
//
//        }
//    }
    private void getMoreDataFromNet() {
        RequestParams params = new RequestParams(moreUrl);
        params.setConnectTimeout(4000);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                LogUtil.e("加载更多联网成功=="+result);
//                listview.onRefreshFinish(false);
                mPullRefreshListView.onRefreshComplete();
                //把这个放在前面
                isLoadMore = true;
                //解析数据
                processData(result);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("加载更多联网失败onError=="+ex.getMessage());
//                listview.onRefreshFinish(false);
                mPullRefreshListView.onRefreshComplete();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e("加载更多联网onCancelled"+cex.getMessage());
            }

            @Override
            public void onFinished() {
                LogUtil.e("加载更多联网onFinished");
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        url = Constans.BaseUrl + childrenBean.getUrl();
        String savejson=CacheUtils.getString(context,url);
        if(!TextUtils.isEmpty(savejson)){
            processData(savejson);
        }
        LogUtil.e(url);
        getDataFromNet();
    }

    private void getDataFromNet() {
        RequestParams params = new RequestParams(url);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                CacheUtils.putString(context,url,result);

                //解析和显示数据
                processData(result);
//                listview.onRefreshFinish(true);
                mPullRefreshListView.onRefreshComplete();

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
             LogUtil.e("页面请求失败"+ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e("页面请求取消"+cex);
            }

            @Override
            public void onFinished() {
          LogUtil.e("finish");
            }
        });
    }

    private void processData(String json) {
        TabDetailBean bean=parsedJson(json);
        LogUtil.e("解析成功"+bean.getData().getNews().get(0).getTitle());
        moreUrl = "";
        if(TextUtils.isEmpty(bean.getData().getMore())){
            moreUrl = "";
        }else{
            moreUrl = Constans.BaseUrl + bean.getData().getMore();
        }

        LogUtil.e("加载更多的地址==="+moreUrl);
        if(!isLoadMore){
            //默认
            //顶部轮播图数据
            topnews = bean.getData().getTopnews();
            //设置ViewPager的适配器
            viewpager.setAdapter(new TopNewsAdapter());


            //添加红点
            addpoint();

            //监听页面的改变，设置红点变化和文本变化
            viewpager.addOnPageChangeListener(new MyOnPageChangeListener());
            tv_title.setText(topnews.get(prePosition).getTitle());

            //准备ListView对应的集合数据
            news = bean.getData().getNews();
            //设置ListView的适配器
            adapter = new newsdetailAdapter();
            listview.setAdapter(adapter);
        }else{
            //加载更多
            isLoadMore = false;
            //添加到原来的集合中
            news.addAll(bean.getData().getNews());
            //刷新适配器
            adapter.notifyDataSetChanged();


        }
//        topnews=bean.getData().getTopnews();
//        news=bean.getData().getNews();
//        //设置顶部news适配器
//        viewpager.setAdapter(new TopNewsAdapter());
//        addpoint();
//        viewpager.addOnPageChangeListener(new MyOnPageChangeListener());
//        tv_title.setText(topnews.get(0).getTitle());
//         adapter=new newsdetailAdapter();
//        listview.setAdapter(adapter);
    }

    private void addpoint() {
        ll_group.removeAllViews();
        for(int i=0;i<topnews.size();i++){
            ImageView imageView=new ImageView(context);
            imageView.setBackgroundResource(R.drawable.point_selector);//设置红白点选择

            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(DensityUtil.dip2px(context,8),DensityUtil.dip2px(context,8));
            if(i==0){
                imageView.setEnabled(true);
            }
            else
            {
                imageView.setEnabled(false);
                params.leftMargin=DensityUtil.dip2px(context,8);
            }
            imageView.setLayoutParams(params);
           ll_group.addView(imageView);
        }
    }

    class newsdetailAdapter extends BaseAdapter {
        ViewHolder viewHolder;
        @Override
        public int getCount() {

            return news.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView==null)
            { viewHolder=new ViewHolder();
                convertView=View.inflate(context,R.layout.item_tabdetail_pager,null);
                viewHolder.iv_icon= (ImageView) convertView.findViewById(R.id.iv_icon);
                viewHolder.tv_title= (TextView) convertView.findViewById(R.id.tv_title);
                viewHolder.tv_data= (TextView) convertView.findViewById(R.id.tv_time);
                convertView.setTag(viewHolder);
            }
            else{
               viewHolder= (ViewHolder) convertView.getTag();
            }
            TabDetailBean.DataBean.NewsBean newsBean=news.get(position);
            String imageUrl=Constans.BaseUrl+newsBean.getListimage();
            x.image().bind(viewHolder.iv_icon,imageUrl);
            viewHolder.tv_title.setText(newsBean.getTitle());
           viewHolder.tv_data.setText(newsBean.getPubdate());
            return convertView;
        }
    }
    static class ViewHolder{
        ImageView iv_icon;
        TextView tv_title;
        TextView tv_data;

    }
    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            //设置文本
            tv_title.setText(topnews.get(position).getTitle());
            //移除上一个页面红点
           ll_group.getChildAt(prePosition).setEnabled(false);
            //将当前页面变成红点
            ll_group.getChildAt(position).setEnabled(true);
            prePosition=position;

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private TabDetailBean parsedJson(String json) {
        return  new Gson().fromJson(json,TabDetailBean.class);
    }
    class TopNewsAdapter extends PagerAdapter{
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView  imageView=new ImageView(context);
            imageView.setBackgroundResource(R.drawable.home_scroll_default);
            //x轴y轴拉伸
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            container.addView(imageView);
            TabDetailBean.DataBean.TopnewsBean topnewsBean=topnews.get(position);
            String imageUrl=Constans.BaseUrl+topnewsBean.getTopimage();
            x.image().bind(imageView,imageUrl);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return topnews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }
    }
}
