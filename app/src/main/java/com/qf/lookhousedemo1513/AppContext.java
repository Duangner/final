package com.qf.lookhousedemo1513;

import android.app.Application;

import com.qf.util.SharedUtil;

public class AppContext extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        //共享参数对象初始化
        SharedUtil.init(this);
    }
}
