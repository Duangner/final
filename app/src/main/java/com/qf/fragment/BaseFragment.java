package com.qf.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import java.io.Serializable;

/**
 * Created by Ken on 2015/12/14.
 */
public class BaseFragment<K extends Serializable> extends Fragment {

    private static final String DATA_KEY = "datas";

    /**
     * 静态工厂方法
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T extends BaseFragment> T newInstance(Class<T> tClass){
        if(tClass != null){
            try {
                T t = tClass.newInstance();
                return t;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 绑定数据源
     * @param k
     * @return
     */
    public BaseFragment bindDatas(K k){
        Bundle bundle = new Bundle();
        bundle.putSerializable(DATA_KEY, k);
        this.setArguments(bundle);
        return this;
    }

    /**
     * 不属于fragment的生命周期方法
     * 紧跟着onCreateView方法后执行
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        loadDatas();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null) {
            getDatas((K) bundle.getSerializable(DATA_KEY));
        }
    }

    /**
     * 获得参数
     * @param k
     */
    public void getDatas(K k){

    }

    /**
     * 加载数据的方法
     */
    public void loadDatas() {

    }

    /**
     *  初始化控件
     * @param view
     */
    public void init(View view) {

    }

}
