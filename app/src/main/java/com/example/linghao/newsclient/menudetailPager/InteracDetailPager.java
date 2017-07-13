package com.example.linghao.newsclient.menudetailPager;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.linghao.newsclient.R;
import com.example.linghao.newsclient.base.NewsDetailBasePager;
import com.example.linghao.newsclient.domain.NewsPagerBean;
import com.example.linghao.newsclient.domain.PhotosMenuDetailBean;
import com.example.linghao.newsclient.utils.BitmapCacheUtils;
import com.example.linghao.newsclient.utils.CacheUtils;
import com.example.linghao.newsclient.utils.Constans;
import com.example.linghao.newsclient.utils.LogUtil;
import com.example.linghao.newsclient.utils.NetCacheUtils;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.squareup.picasso.Picasso;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * Created by linghao on 2017/6/28.
 */

public class InteracDetailPager extends NewsDetailBasePager {
    private final NewsPagerBean.DataBean dataBean;
    @ViewInject(R.id.listview)
    private ListView listView;
    @ViewInject(R.id.gridview)
    private GridView gridview;
    private String Url;
    private List<PhotosMenuDetailBean.DataBean.NewsBean> news;
    private MyListAdapter adapter;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case NetCacheUtils.SUCESS://图片请求成功
                    int position = msg.arg1;
                    Bitmap bitmap = (Bitmap) msg.obj;

                    if(listView.isShown()){
                        ImageView iv_icon = (ImageView) listView.findViewWithTag(position);
                        if(iv_icon != null && bitmap!= null){
                            iv_icon.setImageBitmap(bitmap);
                        }
                    }

                    if(gridview.isShown()){
                        ImageView iv_icon = (ImageView) gridview.findViewWithTag(position);
                        if(iv_icon != null && bitmap!= null){
                            iv_icon.setImageBitmap(bitmap);
                        }
                    }

                    LogUtil.e("请求图片成功=="+position);

                    break;
                case NetCacheUtils.FAIL://图片请求失败
                    position = msg.arg1;
                    LogUtil.e("请求图片失败=="+position);
                    break;
            }
        }
    };
    private BitmapCacheUtils bitmapCacheUtils;
    public InteracDetailPager(Context context, NewsPagerBean.DataBean dataBean) {
        super(context);
        this.dataBean=dataBean;
        bitmapCacheUtils = new BitmapCacheUtils(handler);
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
        private DisplayImageOptions options;
        public MyListAdapter(){
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.home_scroll_default)
					.showImageForEmptyUri(R.drawable.home_scroll_default)
					.showImageOnFail(R.drawable.home_scroll_default)
					.cacheInMemory(true)
					.cacheOnDisk(true)
					.considerExifParams(true)
					.bitmapConfig(Bitmap.Config.RGB_565)
					.displayer(new RoundedBitmapDisplayer(20))
                .build();}
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
            PhotosDetailPager.viewHolder viewHolder;
            if(convertView==null){
                convertView=View.inflate(context,R.layout.item_photos_menu_detail,null);
                viewHolder=new PhotosDetailPager.viewHolder();
                viewHolder.iv_icon= (ImageView) convertView. findViewById(R.id.iv_icon);
                viewHolder.tv_title= (TextView) convertView. findViewById(R.id.tv_title);
                convertView.setTag(viewHolder);
            }
            else
            {
                viewHolder= (PhotosDetailPager.viewHolder) convertView.getTag();
            }
            PhotosMenuDetailBean.DataBean.NewsBean newsBean=news.get(position);
            viewHolder.tv_title.setText(newsBean.getTitle());
            String imageUrl = Constans.BaseUrl + newsBean.getListimage();
//            x.image().bind(viewHolder.iv_icon, imageUrl);
//                        viewHolder.iv_icon.setTag(position);
//            Bitmap bitmap = bitmapCacheUtils.getBitmap(imageUrl,position);//内存或者本地
//            if(bitmap != null){
//                viewHolder.iv_icon.setImageBitmap(bitmap);
//            }
            //使用第三方 picasso请求图片
//            Picasso.with(context)
//                    .load(imageUrl)
//                    .placeholder(R.drawable.home_scroll_default)
//                    .error(R.drawable.home_scroll_default)
//                    .into(viewHolder.iv_icon);
            //使用glide加载图片
//            Glide.with(context)
//                    .load(imageUrl)
//
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .error(R.drawable.home_scroll_default)
//                    .placeholder(R.drawable.home_scroll_default)
//                   .into(viewHolder.iv_icon);
            //image loader加载图片
            ImageLoader.getInstance().displayImage(imageUrl, viewHolder.iv_icon, options);
            return convertView;
        }
    }
    static class viewHolder{
        ImageView iv_icon;
        TextView tv_title;
    }
}
