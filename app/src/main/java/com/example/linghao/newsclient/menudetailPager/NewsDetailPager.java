package com.example.linghao.newsclient.menudetailPager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.linghao.newsclient.R;
import com.example.linghao.newsclient.activity.MainActivity;
import com.example.linghao.newsclient.base.BasePager;
import com.example.linghao.newsclient.base.NewsDetailBasePager;
import com.example.linghao.newsclient.domain.NewsPagerBean;
import com.example.linghao.newsclient.menudetailPager.tabledetailpager.TableDetailPager;
import com.example.linghao.newsclient.utils.LogUtil;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.viewpagerindicator.TabPageIndicator;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linghao on 2017/6/28.
 */

public class NewsDetailPager extends NewsDetailBasePager {
    private ArrayList<TableDetailPager> tableDetailPagers;//新闻中心各类页面的集合
    private List<NewsPagerBean.DataBean.ChildrenBean> children;//新闻中心各类页面数据的集合
    @ViewInject(R.id.viewpager)
    private ViewPager viewPager;
    @ViewInject(R.id.tablePageIndicator)
    private TabPageIndicator tabPageIndicator;
    @ViewInject(R.id.ib_news_next)
    private ImageButton ib_next;

    public NewsDetailPager(Context context, NewsPagerBean.DataBean dataBean) {
        super(context);
        children = dataBean.getChildren();
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.newsmenu_detail_pager, null);
        x.view().inject(this, view);
        //设置imagebutton的点击事件，切换到下一页面
        ib_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
            }
        });
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        tableDetailPagers = new ArrayList<>();

        for (int i = 0; i < children.size(); i++) {
            tableDetailPagers.add(new TableDetailPager(context, children.get(i)));
        }
        viewPager.setAdapter(new MyNewsDetailPagerAdapter());
/**
 * 为中国，生活等子页面设置指示器
 */
        tabPageIndicator.setViewPager(viewPager);
        tabPageIndicator.setOnPageChangeListener(new MyOnPageChangeListener());

    }
    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if(position==0){
                isEnableSlidingMenu(SlidingMenu.TOUCHMODE_FULLSCREEN);
            }
            else{
          isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
    private void isEnableSlidingMenu(int m) {
        MainActivity mainActivity= (MainActivity) context;
        mainActivity.getSlidingMenu().setTouchModeAbove(m);
    }

    class MyNewsDetailPagerAdapter extends PagerAdapter {
        @Override
        public CharSequence getPageTitle(int position) {

            return children.get(position).getTitle();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TableDetailPager tableDetailPager = tableDetailPagers.get(position);
            View rootview = tableDetailPager.rootView;

            tableDetailPager.initData();
            container.addView(rootview);
            return rootview;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return tableDetailPagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
