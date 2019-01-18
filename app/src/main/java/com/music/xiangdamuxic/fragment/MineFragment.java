package com.music.xiangdamuxic.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.music.xiangdamuxic.R;
import com.music.xiangdamuxic.utils.ActivityManager;
import com.music.xiangdamuxic.utils.Constant;
import com.music.xiangdamuxic.utils.Utils;
import com.suke.widget.SwitchButton;

import java.util.Timer;
import java.util.TimerTask;


public class MineFragment extends Fragment {

    private View layout;
    private Activity activity;
    Timer timer;
    int count = 0;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int max = msg.getData().getInt("max");
            count++;
            if (max <= count) {
                Utils.putBool(activity, Constant.ISTIMING, false);
                ActivityManager.getInstance().exit();
            }

            int h = (max - count) / 3600;
            int m = (max - (h * 3600) - count) / 60;
            int s = (max - count) % 60;

            timerNum.setText("还剩" + h + ":" + m + ":" + s);

        }
    };
    private TextView timerNum;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = View.inflate(getActivity().getApplicationContext(), R.layout.fragment_mine, null);
        activity = getActivity();
        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        timerNum = layout.findViewById(R.id.timerNum);

        final SwitchButton switchButton = layout.findViewById(R.id.fragement_mine_switch_button);

        boolean isTiming = Utils.getBool(activity, Constant.ISTIMING, false);
        switchButton.setChecked(isTiming);

        if (isTiming) {
            timerNum.setText("已开启定时");
        } else {
            timerNum.setText("定时关闭");
        }

        switchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                Utils.putBool(activity, Constant.ISTIMING, isChecked);
                if (isChecked) {
                    //1.创建一个Dialog对象，如果是AlertDialog对象的话，弹出的自定义布局四周会有一些阴影，效果不好
                    final Dialog mDialog = new Dialog(activity);
                    //去除标题栏
                    mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    //2.填充布局
                    LayoutInflater inflater = LayoutInflater.from(activity);
                    final View dialogView = inflater.inflate(R.layout.item_view_dialog, null);
                    //将自定义布局设置进去
                    mDialog.setContentView(dialogView);
                    //3.设置指定的宽高,如果不设置的话，弹出的对话框可能不会显示全整个布局，当然在布局中写死宽高也可以
                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    Window window = mDialog.getWindow();
                    lp.copyFrom(window.getAttributes());
                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                    mDialog.show();
                    window.setAttributes(lp);

                    //设置点击其它地方不让消失弹窗
                    mDialog.setCancelable(false);


                    dialogView.findViewById(R.id.opt).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            EditText etv = dialogView.findViewById(R.id.etv);
//                            final int text = Integer.valueOf(String.valueOf(etv.getText()));
                            String s = etv.getText().toString();
                            final int text = Integer.valueOf(s);
                            Utils.putInt(activity, Constant.TIMINGNUM, text);

                            Toast.makeText(activity, "已成功开启定时--" + text + "分钟", Toast.LENGTH_SHORT).show();

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    final int max = text * 60;
//                                    final int max=text;
                                    timer = new Timer();
                                    TimerTask timerTask = new TimerTask() {
                                        @Override
                                        public void run() {
                                            Message msg = Message.obtain();
                                            Bundle b = new Bundle();
                                            b.putInt("max", max);
                                            msg.setData(b);
                                            handler.sendMessage(msg);
                                        }
                                    };

                                    timer.schedule(timerTask, 0, 1000);
                                }
                            }).start();

                            mDialog.dismiss();
                        }
                    });

                    dialogView.findViewById(R.id.neg).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            switchButton.setChecked(false);
                            mDialog.dismiss();
                        }
                    });
                } else {
                    if (timer != null) {
                        timer.cancel();
                        timerNum.setText("定时关闭");
                    }
                }


            }
        });

        final EditText etitv = layout.findViewById(R.id.fragement_mine_editText);

        etitv.setText(Utils.getString(activity, Constant.MEMORANDUM, ""));

        layout.findViewById(R.id.fragement_mine_button_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String s = etitv.getText().toString();
                Utils.putString(activity, Constant.MEMORANDUM, s);
                Toast.makeText(activity, "保存成功", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
