package com.music.xiangdamuxic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;

import com.hanks.htextview.scale.ScaleTextView;
import com.jaeger.library.StatusBarUtil;
import com.music.xiangdamuxic.utils.Constant;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class WelcomePage extends AppCompatActivity {

    int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcomepage);
        //状态栏透明化
        StatusBarUtil.setTransparent(WelcomePage.this);

        //获取开始时间
        Date date = new Date();
        final long startTime = date.getTime();

        //1.0 不透明，0.0完全透明
        //设置渐变动画
        AlphaAnimation ac = new AlphaAnimation(0.0f, 1.0f);
        ac.setDuration(3000);
        //设置渐变动画
        findViewById(R.id.wlecomePage_relativeLayout).startAnimation(ac);

        //设置动态字段
        final ScaleTextView scaleTextView = findViewById(R.id.wlecomePage_scaleTextView);
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                scaleTextView.animateText(Constant.SENTENCES[(index++) % Constant.SENTENCES.length]);
            }
        };

        //开启任务，1.5S更换
        Timer timer = new Timer();
        timer.schedule(timerTask, 0, 1500);

        //获取结束时间
        final long endTime = date.getTime();

        //进行时间计算
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep((5000) - (endTime - startTime));
                    Intent intent = new Intent(WelcomePage.this, MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.next_in, R.anim.next_out);

                    //延迟销毁WelcomePage
                    Thread.sleep(3000);
                    WelcomePage.this.finish();
                } catch (InterruptedException e) {
                }
            }
        }).start();


    }

}


