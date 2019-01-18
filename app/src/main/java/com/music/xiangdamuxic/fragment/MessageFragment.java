package com.music.xiangdamuxic.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.music.xiangdamuxic.R;
import com.taobao.library.BaseBannerAdapter;
import com.taobao.library.VerticalBannerView;

import java.util.ArrayList;
import java.util.List;


public class MessageFragment extends Fragment {

    private View layout;
    private Activity activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = View.inflate(getActivity().getApplicationContext(), R.layout.fragment_message, null);
        activity = getActivity();
        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        List<Model> datas03 = new ArrayList<>();
        datas03.add(new Model("汪苏泷新曲发布", "New"));
        datas03.add(new Model("沙漠骆驼--展展与罗", "Hot"));
        final SampleAdapter03 adapter03 = new SampleAdapter03(datas03);
        final VerticalBannerView banner03 = layout.findViewById(R.id.banner_03);
        banner03.setAdapter(adapter03);
        banner03.start();
    }

    private class SampleAdapter03 extends BaseBannerAdapter<Model> {
        private List<Model> mDatas;

        public SampleAdapter03(List<Model> datas) {
            super(datas);
        }

        @Override
        public View getView(VerticalBannerView parent) {
            return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_verticalaadvertisement, null);
        }

        @Override
        public void setItem(final View view, final Model data) {
            TextView tv = view.findViewById(R.id.title);
            tv.setText(data.title);

            TextView tag = view.findViewById(R.id.tag);
            tag.setText(data.url);


        }
    }

    private class Model {
        public String title;
        public String url;

        public Model(String title, String url) {
            this.title = title;
            this.url = url;
        }
    }
}
