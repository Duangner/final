package com.qf.fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qf.custem.CacheImageView;
import com.qf.lookhousedemo1513.R;
import com.qf.model.HeadEntity;


/**
 * 头部ViewPager中的Fragment
 */
public class HeadVpFragment extends BaseFragment<HeadEntity>{

    private static final String TAG = "print";
    private CacheImageView civ;
    private TextView tv;

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_headvp, null);
    }

    @Override
    public void init(View view) {
        civ = (CacheImageView) view.findViewById(R.id.civ_headvp);
        tv = (TextView) view.findViewById(R.id.tv_headvp);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        HeadEntity headEntity = (HeadEntity) bundle.getSerializable("datas");

        civ.setUrl(headEntity.getPicurl());
        tv.setText(headEntity.getTitle());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView() 被调用");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() 被调用");
    }
}
