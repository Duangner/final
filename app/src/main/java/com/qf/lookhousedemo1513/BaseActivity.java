package com.qf.lookhousedemo1513;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.qf.fragment.BaseFragment;

/**
 * Created by Ken on 2015/12/14.
 */
public class BaseActivity extends FragmentActivity{

    private FragmentManager fm;
    private Fragment showFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.fm = getSupportFragmentManager();
    }


    /**
     * 自带过场动画的启动Activity的方式
     * @param intent
     */
    @Override
    public void startActivity(Intent intent) {
        startActivity(intent, R.anim.activity_in_right, R.anim.activity_out);
    }

    /**
     * 自定义过场动画的启动Activity的方式
     * @param intent
     * @param inAnim
     * @param outAnim
     */
    public void startActivity(Intent intent, int inAnim, int outAnim){
        super.startActivity(intent);
        overridePendingTransition(inAnim, outAnim);
    }

    /**
     * fragment管理方法
     * @param resid
     * @param tClass
     * @param <T>
     */
    public <T extends BaseFragment> void fragmentManager(int resid, Class<T> tClass){
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        Fragment fragmentByTag = fm.findFragmentByTag(tClass.getName());

        if(showFragment != null){
            fragmentTransaction.hide(showFragment);
        }

        if(fragmentByTag != null){
            fragmentTransaction.show(fragmentByTag);
            showFragment = fragmentByTag;
        } else {
            BaseFragment baseFragment = BaseFragment.newInstance(tClass);
            fragmentTransaction.add(resid, baseFragment, tClass.getName());
            showFragment = baseFragment;
        }
        fragmentTransaction.commit();
    }

}
