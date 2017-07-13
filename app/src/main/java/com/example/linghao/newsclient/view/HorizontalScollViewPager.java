package com.example.linghao.newsclient.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by linghao on 2017/6/30.
 */

public class HorizontalScollViewPager extends ViewPager{
    private float startx;
    private float starty;
    public HorizontalScollViewPager(Context context) {
        super(context);
    }

    public HorizontalScollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
        //请求父层视图不拦截当前控件的事件
        getParent().requestDisallowInterceptTouchEvent(true);//都把事件传给当前控件 HorizontalScollViewPager
               //1.记录起始坐标
                startx=ev.getX();
                starty=ev.getY();
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                getParent().requestDisallowInterceptTouchEvent(true);
                float endX=ev.getX();
                float endY=ev.getY();
                float distanceX=endX-startx;
                float distanceY=endY-starty;
                if(Math.abs(distanceX)>Math.abs(distanceY)) {
                    //水平方向滑动
                    //当滑动到ViewPager的第0个页面，并且是从左到右滑动
                    if(getCurrentItem()==0&&distanceX>0){
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }
                    //当滑动到ViewPager的最后一个页面，并且是从右到左滑动
                    else if((getCurrentItem()==(getAdapter().getCount()-1))&&distanceX<0){
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }
                    //其他,中间部分
                    else{
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                }
                else{
                    //竖直方向滑动
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
