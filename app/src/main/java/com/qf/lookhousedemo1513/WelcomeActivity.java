package com.qf.lookhousedemo1513;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


/**
 * Created by Ken on 2015/12/14.
 */
public class WelcomeActivity extends BaseActivity{

    public static Handler handler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                finish();
            }
        }, 2000);
    }
}
