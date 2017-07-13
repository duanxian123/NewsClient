package com.example.linghao.newsclient;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.linghao.newsclient.activity.GuideActivity;
import com.example.linghao.newsclient.activity.MainActivity;
import com.example.linghao.newsclient.utils.CacheUtils;

public class SplashActivity extends Activity {
    public static final String START_MAIN = "start_main";
    private RelativeLayout splash;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        splash=(RelativeLayout) findViewById(R.id.splash);
        //渐变动画
        AlphaAnimation jianbian=new AlphaAnimation(0,1);
//        jianbian.setDuration(500);//播放时间
        jianbian.setFillAfter(true);//停留

        //缩放动画
        ScaleAnimation suofang=new ScaleAnimation(0,1,0,1,ScaleAnimation.RELATIVE_TO_SELF,0.5f,ScaleAnimation.RELATIVE_TO_SELF,0.5f);
//        suofang.setDuration(500);
        jianbian.setFillAfter(true);
        //旋转动画
        RotateAnimation xuanzhuan=new RotateAnimation(0,360,ScaleAnimation.RELATIVE_TO_SELF,0.5f,ScaleAnimation.RELATIVE_TO_SELF,0.5f);
//        xuanzhuan.setDuration(500);
        xuanzhuan.setFillAfter(true);
        AnimationSet set=new AnimationSet(false);
        set.addAnimation(jianbian);
        set.addAnimation(xuanzhuan);
        set.addAnimation(suofang);
        set.setDuration(2000);
        splash.startAnimation(set);
        set.setAnimationListener(new MyAnimationListener());
    }
    class MyAnimationListener implements Animation.AnimationListener{

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        //动画结束时
        public void onAnimationEnd(Animation animation) {
            Boolean isstartmain= CacheUtils.getBoolean(SplashActivity.this,START_MAIN);
            if(isstartmain){
                //进入主界面
                Intent intent=new Intent(SplashActivity.this,MainActivity.class);
                startActivity(intent);
            Toast.makeText(SplashActivity.this,"jinlail",Toast.LENGTH_SHORT).show();
        }
        else{
                //进入引导页面
                Intent intent=new Intent(SplashActivity.this, GuideActivity.class);
                startActivity(intent);
            }
            finish();
        }



        public void onAnimationRepeat(Animation animation) {

        }

}}
