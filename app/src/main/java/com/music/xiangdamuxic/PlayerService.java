package com.music.xiangdamuxic;

import android.app.Service;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;

import com.music.xiangdamuxic.utils.Constant;
import com.music.xiangdamuxic.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

public class PlayerService extends Service {


    private MediaPlayer mp;
    private int playingNum;
    private LinkedList<File> listOfSong;
    //    int numSong = 0;
    IBinder b = new MyBinder();

    Timer timer = null;

//    ArrayList<File> files = new ArrayList<>();

    // 自定义中间人对象,向外提供服务内的功能
    public class MyBinder extends Binder {
        public void start() {
            PlayerService.this.start();
            PlayerService.this.updataSeekbar();
        }

        public void pause() {
            PlayerService.this.pause();
            timer.cancel();
        }


        public void updataMediaPlay(int i) {
            PlayerService.this.updataMediaPlay(i);
        }

        public void next() {
            PlayerService.this.next();
        }

        public void pre() {
            PlayerService.this.pre();
        }


        public void playNumSong(int x) throws IOException {
            PlayerService.this.playNumSong(x);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // 设置初始音乐文件路径,正在播放的
        try {
            mp.setDataSource(listOfSong.get(playingNum).getAbsolutePath()); // 给播放器设置路径
            mp.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return b;
    }

    @Override
    public void unbindService(ServiceConnection conn) {
        super.unbindService(conn);
        if (mp != null) {
            mp.release(); //从内存里清除
            mp = null;
        }
    }

    // 创建音乐对象 new
    @Override
    public void onCreate() {
        super.onCreate();
        mp = new MediaPlayer();
        //获取歌曲列表
        listOfSong = Utils.getBeanFromSp(this, Constant.MUSICLISTKEY);

        //获取正在播放的序号
        playingNum = Utils.getInt(this, Constant.PLAYINGNUM, 0);
    }

    public void start() {
        mp.start();
    }


    public void pause() {
        mp.pause();
    }

    public void updataSeekbar() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                int duration = mp.getDuration();
                int currentPosition = mp.getCurrentPosition();

                Message msg = Message.obtain();
                msg.what = 0;
                Bundle bundle = new Bundle();
                bundle.putInt("duration", duration);
                bundle.putInt("currentPosition", currentPosition);
                msg.setData(bundle);
                PlayerActivity.handler.sendMessage(msg);
            }
        };
        timer = new Timer();
        timer.schedule(task, 0, 1000);
    }

    public void updataMediaPlay(int process) {
        mp.seekTo(process);
    }

    public void next() {
        try {
        int mode = Utils.getInt(this, Constant.MODE, 0);
        switch (mode){
            case 0:
                playNumSong((++playingNum) % listOfSong.size());
                break;
            case 1:
                playNumSong(playingNum);
                break;
            case 2:
                int x = (int) (Math.random() * (listOfSong.size() - 1));
                playNumSong(x);
                break;
        }

        } catch (IOException e) {
        }
    }

    private void playNumSong(int x) throws IOException {
        timer.cancel();
        Utils.putInt(this, Constant.PLAYINGNUM, x);
        mp.stop();
        mp.release();
        mp = null;
        mp = new MediaPlayer();
        mp.setDataSource(listOfSong.get(x).getAbsolutePath()); // 给播放器设置路径
        mp.prepare();
        Message msg = Message.obtain();
        msg.what = 1;
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.PLAYINGNUM, x);
        msg.setData(bundle);
        PlayerActivity.handler.sendMessage(msg);
        start();
        updataSeekbar();
    }

    public void pre() {
        try {
            int mode = Utils.getInt(this, Constant.MODE, 0);
            switch (mode){
                case 0:
                    playNumSong((--playingNum+listOfSong.size()) % listOfSong.size());
                    break;
                case 1:
                    playNumSong(playingNum);
                    break;
                case 2:
                    int x = (int) (Math.random() * (listOfSong.size() - 1));
                    playNumSong(x);
                    break;
            }

        } catch (IOException e) {
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null)
            timer.cancel();
        if (mp != null) {
            mp.release();
            mp = null;
        }
    }

}
