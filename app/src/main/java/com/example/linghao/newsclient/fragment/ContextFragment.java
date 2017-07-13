package com.example.linghao.newsclient.fragment;

import android.support.annotation.IdRes;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.linghao.newsclient.R;
import com.example.linghao.newsclient.activity.MainActivity;
import com.example.linghao.newsclient.base.BaseFragment;
import com.example.linghao.newsclient.base.BasePager;
import com.example.linghao.newsclient.pager.govpager;
import com.example.linghao.newsclient.pager.homepager;
import com.example.linghao.newsclient.pager.newspager;
import com.example.linghao.newsclient.pager.settingpager;
import com.example.linghao.newsclient.pager.smartpager;
import com.example.linghao.newsclient.view.NoScrollViewPager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by linghao on 2017/6/26.
 */

public class ContextFragment extends BaseFragment {
    private TextView textView;
    //通过注解形式初始化控件
    @ViewInject(R.id.viewpager)
   private   NoScrollViewPager  viewPager;
    @ViewInject(R.id.radiogroup1)
    private  RadioGroup radioGroup;
    private ArrayList<BasePager> basePagers;
    @Override
    public View initView() {
        View view=View.inflate(context, R.layout.context_fragment,null);
//    viewPager= (ViewPager) view.findViewById(R.id.viewpager);
//        radioGroup= (RadioGroup) view.findViewById(R.id.radiogroup1);

        x.view().inject(ContextFragment.this,view); //把视图注入到框架中，将ContextFragmen和view关联

        return view;
    }

    @Override
    public void initData() {
        super.initData();
        basePagers=new ArrayList<>();
        basePagers.add(new homepager(context));
        basePagers.add(new newspager(context));
        basePagers.add(new smartpager(context));
        basePagers.add(new govpager(context));
        basePagers.add(new settingpager(context));

       viewPager.setAdapter(new contextfragmentAdapter());//设置ViewPager的适配器


        viewPager.addOnPageChangeListener(new MyOnPageChangeListener());//监听某个页面被选中，初始对应的页面的数据
        radioGroup.setOnCheckedChangeListener(new MyOnCheckedChangeListener());//设置RadioGroup的选中状态改变的监听
        radioGroup.check(R.id.rb_home);//groupradio设置默认位置
        basePagers.get(0).initData();
        isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);//设置模式SlidingMenu不可以滑动
    }

    public newspager getNewsDetailPager() {
        return (newspager) basePagers.get(1);
    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        /**
         * 当某个页面被选中的时候回调这个方法
         */
        public void onPageSelected(int position) {
         basePagers.get(position).initData();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener{

        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            switch (checkedId){
                case R.id.rb_home:
                    viewPager.setCurrentItem(0);
                    isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);
                    break;
                case R.id.rb_newscenter:
                    viewPager.setCurrentItem(1);
                    isEnableSlidingMenu(SlidingMenu.TOUCHMODE_FULLSCREEN);
                    break;
                case R.id.rb_smart:
                    viewPager.setCurrentItem(2);
                    isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);
                    break;
                case R.id.rb_gov:
                    viewPager.setCurrentItem(3);
                    isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);
                    break;
                case R.id.rb_setting:
                    viewPager.setCurrentItem(4);
                    isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);
                    break;
            }
        }
    }

    private void isEnableSlidingMenu(int m) {
        MainActivity mainActivity= (MainActivity) context;
        mainActivity.getSlidingMenu().setTouchModeAbove(m);
    }

    class contextfragmentAdapter extends PagerAdapter{
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePager basePager=basePagers.get(position);
             View rootview=basePager.rootView;
            container.addView(rootview);
//            basePager.initData();
            return rootview;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return basePagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }
    }
}
