package com.music.xiangdamuxic.fragment;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.hanks.htextview.evaporate.EvaporateTextView;
import com.music.xiangdamuxic.MusicListActivity;
import com.music.xiangdamuxic.R;
import com.music.xiangdamuxic.utils.Constant;
import com.music.xiangdamuxic.vpage.BezierRoundView;
import com.music.xiangdamuxic.vpage.BezierViewPager;
import com.music.xiangdamuxic.vpage.CardPagerAdapter;
import com.music.xiangdamuxic.vpage.GlideImageClient;
import com.music.xiangdamuxic.vpage.ImageLoadFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class MainFragment extends Fragment {
    private View layout;
    private Activity activity;

    int index = 0;

    private List<Object> imgList;

    Timer textTimer = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = View.inflate(getActivity().getApplicationContext(), R.layout.fragment_main, null);
        activity = getActivity();
        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        //初始化动态字体
        initDynamicText();

        //初始化ViewPage
        new Thread(new Runnable() {
            @Override
            public void run() {
                InitViewPage();
            }
        }).start();

        initButton();

//        ActivityManager.getInstance().addActivity(this);
    }

    private void initButton() {
        //本地音乐
        layout.findViewById(R.id.item_activity_main_entryPlayActivityButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, MusicListActivity.class);
                startActivity(intent);
                //关闭定时字体任务，不然会空指针异常
                textTimer.cancel();
                activity.overridePendingTransition(R.anim.next_in, R.anim.next_out);
            }
        });

        //我的下载
        layout.findViewById(R.id.item_activity_main_myDownLoadButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, MusicListActivity.class);
                startActivity(intent);
                //关闭定时字体任务，不然会空指针异常
                textTimer.cancel();
                activity.overridePendingTransition(R.anim.next_in, R.anim.next_out);
            }
        });


    }

    private void InitViewPage() {
        ImageLoadFactory.getInstance().setImageClient(new GlideImageClient());

        imgList = new ArrayList<>();
        imgList.add(Constant.IMAGE_URL_1);
        imgList.add(Constant.IMAGE_URL_2);
        imgList.add(Constant.IMAGE_URL_3);
        imgList.add(Constant.IMAGE_URL_4);

        int mWidth = activity.getWindowManager().getDefaultDisplay().getWidth();
        float heightRatio = 0.565f;  //高是宽的 0.565 ,根据图片比例

        CardPagerAdapter cardAdapter = new CardPagerAdapter(activity);
        cardAdapter.addImgUrlList(imgList);


        //设置阴影大小，即vPage  左右两个图片相距边框  maxFactor + 0.3*CornerRadius   *2
        //设置阴影大小，即vPage 上下图片相距边框  maxFactor*1.5f + 0.3*CornerRadius
        int maxFactor = mWidth / 25;
        cardAdapter.setMaxElevationFactor(maxFactor);

        int mWidthPading = mWidth / 8;

        //因为我们adapter里的cardView CornerRadius已经写死为10dp，所以0.3*CornerRadius=3
        //设置Elevation之后，控件宽度要减去 (maxFactor + dp2px(3)) * heightRatio
        //heightMore 设置Elevation之后，控件高度 比  控件宽度* heightRatio  多出的部分
        float heightMore = (1.5f * maxFactor + dp2px(3)) - (maxFactor + dp2px(3)) * heightRatio;
        int mHeightPading = (int) (mWidthPading * heightRatio - heightMore);

        final BezierViewPager viewPager = layout.findViewById(R.id.mainActivity_viewPage);
        viewPager.setLayoutParams(new RelativeLayout.LayoutParams(mWidth, (int) (mWidth * heightRatio)));
        viewPager.setPadding(mWidthPading, mHeightPading, mWidthPading, mHeightPading);
        viewPager.setClipToPadding(false);
        viewPager.setAdapter(cardAdapter);
        viewPager.showTransformer(0.2f);


        BezierRoundView bezRound = layout.findViewById(R.id.mainActivity_viewPageBezRound);
        bezRound.attach2ViewPage(viewPager);

        viewPager.setCurrentItem(0);

        //执行无限循环任务
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int currentItem = viewPager.getCurrentItem();
                        if (currentItem == imgList.size() - 1) {
                            viewPager.setCurrentItem(0);
                        } else {
                            viewPager.setCurrentItem(currentItem + 1);
                        }
                    }
                });

            }
        };
        timer.schedule(task, Constant.DELAY_TIME, Constant.DELAY_TIME);
    }

    private void initDynamicText() {
        //设置变化字体
        final EvaporateTextView cvaporateTextView = layout.findViewById(R.id.mainActivity_evaporateTextView);
        TimerTask textTimerTask = new TimerTask() {
            @Override
            public void run() {
                cvaporateTextView.animateText(Constant.SENTENCES[(index++) % Constant.SENTENCES.length]);
            }
        };

        //开启任务，3S更换
        textTimer = new Timer();
        textTimer.schedule(textTimerTask, 0, Constant.DELAY_TIME);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dp2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


}
