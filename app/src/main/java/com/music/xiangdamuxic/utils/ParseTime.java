package com.music.xiangdamuxic.utils;

import java.text.SimpleDateFormat;

public class ParseTime {
    public static String msToString(int time) {
        SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
        return formatter.format(time);
    }
}
