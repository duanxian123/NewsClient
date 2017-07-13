package com.example.linghao.newsclient.activity;

import android.app.Activity;

import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import com.example.linghao.newsclient.R;
import com.example.linghao.newsclient.fragment.ContextFragment;
import com.example.linghao.newsclient.fragment.LeftmenuFragment;
import com.example.linghao.newsclient.utils.BitmapCacheUtils;
import com.example.linghao.newsclient.utils.CacheUtils;
import com.example.linghao.newsclient.utils.DensityUtil;
import com.example.linghao.newsclient.utils.LocalCacheUtils;
import com.example.linghao.newsclient.utils.NetCacheUtils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class MainActivity extends SlidingFragmentActivity {

    public static final String LEFT_MENU_TAG = "left_menu_tag";
    public static final String MAIN_CONTEXT_TAG = "main_context_tag";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        initSlidingMenu();
        initFragment();
        if(ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission
                .WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
        else
        {

        }
    }

    private void initSlidingMenu() {
        setContentView(R.layout.activity_main);
        //显示左侧菜单
        setBehindContentView(R.layout.activity_menu);
        SlidingMenu slidingMenu=getSlidingMenu();
        //设置模式
        slidingMenu.setMode(SlidingMenu.LEFT);
        //设置滑动模式
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        //设置占屏幕宽度
        slidingMenu.setBehindOffset(DensityUtil.dip2px(MainActivity.this,200));
    }

    private void initFragment() {
        //得到fragment管理器
        FragmentManager fragmentmanager=getSupportFragmentManager();
        //开启事务
        FragmentTransaction ft=fragmentmanager.beginTransaction();
        ft.replace(R.id.left_menu,new LeftmenuFragment(),LEFT_MENU_TAG);
        ft.replace(R.id.main,new ContextFragment(),MAIN_CONTEXT_TAG);
        ft.commit();

        }

    /**
     * 让与mainactivity有关联的能得到leftfragment
     * @return
     */
    public LeftmenuFragment getLeftmenuFragment() {
        FragmentManager fragmentmanager=getSupportFragmentManager();
       LeftmenuFragment leftmenuFragment= (LeftmenuFragment) fragmentmanager.findFragmentByTag(LEFT_MENU_TAG);
        return  leftmenuFragment;
    }

    public ContextFragment getContextFragment() {
        FragmentManager fragmentmanager=getSupportFragmentManager();
        ContextFragment contextFragment= (ContextFragment) fragmentmanager.findFragmentByTag(MAIN_CONTEXT_TAG);
        return  contextFragment;
    }
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){

                }
                else
                {
                    Toast.makeText(this,"没给权限",Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }
}

