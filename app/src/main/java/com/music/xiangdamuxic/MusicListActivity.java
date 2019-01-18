package com.music.xiangdamuxic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jaeger.library.StatusBarUtil;
import com.music.xiangdamuxic.utils.ActivityManager;
import com.music.xiangdamuxic.utils.Constant;
import com.music.xiangdamuxic.utils.Utils;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.File;
import java.util.LinkedList;
import java.util.List;


public class MusicListActivity extends AppCompatActivity {

    private ListView musicListView;
    BaseAdapter adapter = null;
    private LinkedList<File> list;


    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View v;
            if (view == null) {
                v = View.inflate(MusicListActivity.this, R.layout.item_musiclist_adapter, null);
            } else {
                v = view;
            }

            TextView songName = v.findViewById(R.id.item_musicList_songName);
            TextView songer = v.findViewById(R.id.item_musicList_songer);
            AVLoadingIndicatorView loadingView = v.findViewById(R.id.item_loadingView);
            File f = (File) getItem(i);
            String name = f.getName();
            songName.setText(name.substring(0, name.length() - 4));

            int currPlayingNum = Utils.getInt(MusicListActivity.this, Constant.PLAYINGNUM, 0);
            if (currPlayingNum == i) {
                loadingView.setVisibility(View.VISIBLE);
            }else{
                loadingView.setVisibility(View.GONE);
            }


            return v;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musiclist);
        //状态栏透明化
        StatusBarUtil.setTransparent(MusicListActivity.this);

        musicListView = findViewById(R.id.musicListActivity_musicListView);

        //检查是否有音乐列表
        list = Utils.getBeanFromSp(this, Constant.MUSICLISTKEY);
        if (list != null) {
            if (list.size() == 0) {
                //空
            } else {
                adapter = new MyAdapter();
                musicListView.setAdapter(adapter);
            }
        }
        musicListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                int temp = Utils.getInt(MusicListActivity.this, Constant.PLAYINGNUM, 0);
                if (Utils.putInt(MusicListActivity.this, Constant.PLAYINGNUM, i)) {

//                    if (temp != i) {
//                        List<Activity> s = ActivityManager.getActivity();
//                        for (Activity a : s) {
//                            if (a.getLocalClassName().equals("PlayerActivity")) {
//                                a.finish();
//                            }
////                            System.out.println(a.getLocalClassName());
//                        }
//                    }

                    Intent intent = new Intent(MusicListActivity.this, PlayerActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.next_in, R.anim.next_out);
                }

            }
        });

    }


    public void back(View view) {
        MusicListActivity.this.finish();
        overridePendingTransition(R.anim.pre_in, R.anim.pre_out);
    }

    public void SearchMusic(View view) {
        Intent intent = new Intent(this, SearchMusicActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.next_in, R.anim.next_out);

    }
}
