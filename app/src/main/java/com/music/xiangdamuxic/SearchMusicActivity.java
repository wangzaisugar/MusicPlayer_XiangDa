package com.music.xiangdamuxic;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jaeger.library.StatusBarUtil;
import com.music.xiangdamuxic.utils.Constant;
import com.music.xiangdamuxic.utils.Utils;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.File;
import java.util.LinkedList;

public class SearchMusicActivity extends AppCompatActivity {

    //搜索图标
    private ImageView imageViewSearch;

    //加载动画
    private AVLoadingIndicatorView avi;

    LinkedList<File> musicsList;

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            avi.hide();
            imageViewSearch.setVisibility(View.VISIBLE);
            Toast.makeText(SearchMusicActivity.this, "共查询到" + musicsList.size() + "首歌曲", Toast.LENGTH_LONG).show();
            //添加到sp里
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Utils.saveBeanToSp(SearchMusicActivity.this, musicsList, Constant.MUSICLISTKEY);
                }
            }).start();
        }
    };
    private TextView tv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchmusic);
        //状态栏透明化
        StatusBarUtil.setColor(SearchMusicActivity.this, Color.parseColor("#31c25c"), 112);

        imageViewSearch = findViewById(R.id.searchMusicActivity_searchImageView);

        avi = findViewById(R.id.searchMusicActivity_loadingView);

        tv = findViewById(R.id.searchMusicActivity_textView);

    }


    public void back(View view) {
        Intent intent = new Intent(SearchMusicActivity.this, MusicListActivity.class);
        startActivity(intent);
        this.finish();
        overridePendingTransition(R.anim.pre_in, R.anim.pre_out);
    }


    public void search(View view) {
        imageViewSearch.setVisibility(View.GONE);
        avi.setVisibility(View.VISIBLE);
        avi.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
//                try {
//                    Thread.sleep(3000);
//                } catch (InterruptedException e) {
//                }
                musicsList = new LinkedList<>();
//                File file = new File("/storage/emulated/0/Music");
                File file = new File("/storage/emulated/0");
                if (file.exists()) {
                    LinkedList<File> list = new LinkedList<>();
                    File[] files = file.listFiles();
                    for (final File file2 : files) {
                        if (file2.isDirectory()) {
                            list.add(file2);
                        } else {
                            if (file2.getName().endsWith(".MP3") || file2.getName().endsWith(".mp3")) {
                                if (file2.length() / ( 1024 * 1024) >= 1) {
                                    musicsList.add(file2);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            tv.setText(file2.getName());
                                        }
                                    });
                                }


                            }
                        }
                    }
                    File temp_file;
                    while (!list.isEmpty()) {
                        temp_file = list.removeFirst();
                        files = temp_file.listFiles();
                        for (final File file2 : files) {
                            if (file2.isDirectory()) {
                                list.add(file2);
                            } else {
                                if (file2.getName().endsWith(".MP3") || file2.getName().endsWith(".mp3")) {
                                    if (file2.length() / ( 1024 * 1024) >= 1) {
                                        musicsList.add(file2);

                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                tv.setText(file2.getName());
                                            }
                                        });
                                    }
                                }
                            }
                        }
                    }
                }

                Message msg = Message.obtain();
                SearchMusicActivity.this.handler.sendMessage(msg);

            }
        }).start();
    }

}
