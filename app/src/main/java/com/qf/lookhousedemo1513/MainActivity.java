package com.qf.lookhousedemo1513;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import com.qf.fragment.DisFragment;
import com.qf.fragment.HomeFragment;


/**
 * AS常用快捷键
 * eclipse : ctrl + 1   AS : alt + 回车
 * eclipse : alt + /    AS : ctrl + alt + 空格
 * eclipse : ctrl + t   AS : ctrl + h
 * eclipse : ctrl + d   AS : ctrl + y / ctrl + x(剪切)
 * eclipse : ctrl + shift + f  AS : ctrl + alt + l
 * eclipse : shift + alt + s AS : alt + insert
 * 查看方法参数 : ctrl + p
 */
public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    private RadioGroup rg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        rg = (RadioGroup) findViewById(R.id.rg_tab);
        rg.setOnCheckedChangeListener(this);

        View v = rg.getChildAt(0);//获得radiogroup下的第一个子控件
        v.performClick();//模拟点击事件
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.rb_home:
                fragmentManager(R.id.fl_fragment, HomeFragment.class);
                break;
            case R.id.rb_dis:
                fragmentManager(R.id.fl_fragment, DisFragment.class);
                break;
        }
    }
}
