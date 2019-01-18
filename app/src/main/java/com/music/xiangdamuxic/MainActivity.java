package com.music.xiangdamuxic;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;


import com.music.xiangdamuxic.fragment.MainFragment;
import com.music.xiangdamuxic.fragment.MessageFragment;
import com.music.xiangdamuxic.fragment.MineFragment;
import com.music.xiangdamuxic.utils.ActivityManager;
import com.music.xiangdamuxic.utils.Constant;

import java.util.ArrayList;
import java.util.List;

import eu.long1.spacetablayout.SpaceTabLayout;


public class MainActivity extends AppCompatActivity {
    private long mPressedTime = 0;
    private SpaceTabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //状态栏透明化
//        StatusBarUtil.setTransparent(this);

        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new MainFragment());
        fragmentList.add(new MessageFragment());
        fragmentList.add(new MineFragment());
        ViewPager viewPager = findViewById(R.id.Main_viewPager);
        viewPager.setOffscreenPageLimit(Constant.HOMEPAGENUM);
        tabLayout = findViewById(R.id.spaceTabLayout);

        //we need the savedInstanceState to get the position
        tabLayout.initialize(viewPager, getSupportFragmentManager(),
                fragmentList, savedInstanceState);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        tabLayout.saveState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        long mNowTime = System.currentTimeMillis();//获取第一次按键时间
        if ((mNowTime - mPressedTime) > 2000) {//比较两次按键时间差
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_LONG).show();
            mPressedTime = mNowTime;
        } else {//退出程序
            ActivityManager.getInstance().exit();
        }
    }
}
