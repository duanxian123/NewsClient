package com.example.linghao.newsclient.menudetailPager;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.linghao.newsclient.R;
import com.example.linghao.newsclient.base.NewsDetailBasePager;
import com.example.linghao.newsclient.domain.NewsPagerBean;
import com.example.linghao.newsclient.domain.PhotosMenuDetailBean;
import com.example.linghao.newsclient.utils.CacheUtils;
import com.example.linghao.newsclient.utils.Constans;
import com.example.linghao.newsclient.utils.LogUtil;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * Created by linghao on 2017/6/28.
 */

public class PhotosDetailPager extends NewsDetailBasePager {
    private final NewsPagerBean.DataBean dataBean;
    @ViewInject(R.id.listview)
    private ListView listView;
    @ViewInject(R.id.gridview)
    private GridView gridview;
    private String Url;
    private List<PhotosMenuDetailBean.DataBean.NewsBean> news;
    private MyListAdapter adapter;

    public PhotosDetailPager(Context context, NewsPagerBean.DataBean dataBean) {
        super(context);
        this.dataBean=dataBean;
    }

    @Override
    public View initView() {
        View view=View.inflate(context, R.layout.photos_menu_details,null);
        x.view().inject(this,view);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        Url= Constans.BaseUrl+dataBean.getUrl();
       String saveJson= CacheUtils.getString(context,Url);
        if(!TextUtils.isEmpty(saveJson)){
            ProcessData(saveJson);
        }
        getDataFromNet();
    }
    private void getDataFromNet() {
        RequestParams params=new RequestParams(Url);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("成功联网"+result);
                ProcessData(result);//解析数据
                //缓存数据
                CacheUtils.putString(context,Url,result);
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

    private void ProcessData(String result) {
       PhotosMenuDetailBean bean= parseJson(result);
        LogUtil.e(bean.getData().getNews().get(0).getTitle());
        news=bean.getData().getNews();
        adapter=new MyListAdapter();
        listView.setAdapter(adapter);
    }

    private PhotosMenuDetailBean parseJson(String json) {
        return  new Gson().fromJson(json, PhotosMenuDetailBean.class);
    }
   private Boolean isshowListView=true;//默认显示listView
    public void switchListAndGrid(ImageButton ib_switch) {
        if(isshowListView){
            isshowListView=false;
            gridview.setVisibility(View.VISIBLE);
           adapter=new MyListAdapter();
            gridview.setAdapter(adapter);
            listView.setVisibility(View.GONE);
            ib_switch.setImageResource(R.drawable.icon_pic_list_type);
        }
        else
        {  isshowListView=true;
            listView.setVisibility(View.VISIBLE);
            adapter=new MyListAdapter();
            gridview.setAdapter(adapter);
            gridview.setVisibility(View.GONE);
            ib_switch.setImageResource(R.drawable.icon_pic_grid_type);
        }
    }

    private class MyListAdapter extends BaseAdapter {
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
            viewHolder viewHolder;
            if(convertView==null){
                convertView=View.inflate(context,R.layout.item_photos_menu_detail,null);
                viewHolder=new viewHolder();
               viewHolder.iv_icon= (ImageView) convertView. findViewById(R.id.iv_icon);
                viewHolder.tv_title= (TextView) convertView. findViewById(R.id.tv_title);
                convertView.setTag(viewHolder);
            }
            else
            {
                viewHolder= (viewHolder) convertView.getTag();
            }
            PhotosMenuDetailBean.DataBean.NewsBean newsBean=news.get(position);
            viewHolder.tv_title.setText(newsBean.getTitle());
            String imageUrl = Constans.BaseUrl + newsBean.getListimage();
            x.image().bind(viewHolder.iv_icon, imageUrl);
            return convertView;
        }
    }
    static class viewHolder{
        ImageView iv_icon;
        TextView tv_title;
    }
}
