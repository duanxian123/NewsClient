package com.example.linghao.newsclient.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.linghao.newsclient.R;
import com.example.linghao.newsclient.SplashActivity;
import com.example.linghao.newsclient.utils.CacheUtils;
import com.example.linghao.newsclient.utils.DensityUtil;

import java.util.ArrayList;

public class GuideActivity extends Activity {

    private static final String TAG = "GuideActivity";
    private ViewPager viewPager;
    private Button button;
   private LinearLayout ll_point;
    private ArrayList<ImageView> imageviews;
    private ImageView red_paint;
    private int leftmax;
    private  int width;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        viewPager= (ViewPager) findViewById(R.id.viewpager);
        button=(Button) findViewById(R.id.btn_start);
        red_paint= (ImageView) findViewById(R.id.red_point);
        ll_point=(LinearLayout) findViewById(R.id.point_group);
        imageviews=new ArrayList<>();
        int []image=new int[]{R.drawable.guide1,
                               R.drawable.guide2,
                               R.drawable.guide3};
        width= DensityUtil.dip2px(this,10);
        //将图片放入viewpager中
        Log.e(TAG,width+"......");
        for(int i=0;i<image.length;i++)
        {
            ImageView imageview=new ImageView(this);
            imageview.setBackgroundResource(image[i]);
            imageviews.add(imageview);
            //创建点 添加到线性布局
            ImageView point=new ImageView(this);
            point.setBackgroundResource(R.drawable.point);
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(width,width);
            if(i!=0){
                //点相距为10像素
                params.leftMargin=10;
            }
            point.setLayoutParams(params);
            ll_point.addView(point);
    }
           viewPager.setAdapter(new MyViewPager());
        //根据view 的生命周期 ，视图执行到onlayout和ondraw时 视图的长宽高数据已有
                red_paint.getViewTreeObserver().addOnGlobalLayoutListener(new MyOnGlobalLayoutListener());
        //计算屏幕滑动百分比
        viewPager.addOnPageChangeListener(new MyOnPageChangeListener());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //保存是否曾经进入过主界面
                CacheUtils.putBoolean(GuideActivity.this, SplashActivity.START_MAIN,true);
                //跳转到主界面
                Intent intent=new Intent(GuideActivity.this,MainActivity.class);
                startActivity(intent);
                //关闭引导页面
                finish();
            }
        });
    }
    class  MyOnPageChangeListener implements ViewPager.OnPageChangeListener{
        /**
         *
         * @param position 当前滑动页面的位置
         * @param positionOffset  滑动占的百分比
         * @param positionOffsetPixels 滑动的像素
         */
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
             //两点间移动距离=屏幕滑动百分比*间距
//            int leftmargin= (int) (positionOffset*leftmax);
            //两点间滑动的坐标=两点移动距离+原来的起始位置
            int leftmargin=position*leftmax+(int) (positionOffset*leftmax);
            //params.leftmargin=两点间滑动的坐标
            //因为是从布局中获得 所以getLayoutParams
            RelativeLayout.LayoutParams params= (RelativeLayout.LayoutParams) red_paint.getLayoutParams();
            params.leftMargin=leftmargin;
            red_paint.setLayoutParams(params);
        }

        /**
         * 当页面被选中时  回调这个方法
         * @param position  被选中时对应的位置
         */
        @Override
        public void onPageSelected(int position) {
            if(position==imageviews.size()-1){button.setVisibility(View.VISIBLE);}
            else{
                button.setVisibility(View.GONE);
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    class MyOnGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {

        @Override
        public void onGlobalLayout() {
            red_paint.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            leftmax=ll_point.getChildAt(1).getLeft()-ll_point.getChildAt(0).getLeft();
        }
    }
     class MyViewPager extends PagerAdapter{
         @Override
         public int getCount() {
             return imageviews.size();
         }
         @Override

         /**
          * container 就是Viewpager
          * position  创建页面的位置
          */
         public Object instantiateItem(ViewGroup container, int position) {
             ImageView imageview=imageviews.get(position);
             container.addView(imageview);
             return imageview;
         }

         /**
          *
          * @param view 当前创建的视图
          * @param object 等于insratiateItem返回的值
          * @return
          */
         public boolean isViewFromObject(View view, Object object) {
             return view==object;
         }

         @Override
         /**
          * object 要销毁的页面
          */
         public void destroyItem(ViewGroup container, int position, Object object) {
//             super.destroyItem(container, position, object);
         container.removeView((View) object);
         }


     }
}
