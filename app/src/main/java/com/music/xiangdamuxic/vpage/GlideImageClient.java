package com.music.xiangdamuxic.vpage;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class GlideImageClient extends ImageLoadClient {
    @Override
    public void loadImage(ImageView imageView, Object obj, Context context) {
        Glide.with(context).load(obj).into(imageView);
    }
}
