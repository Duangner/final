package com.qf.custem;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.qf.lookhousedemo1513.R;

/**
 * Created by Ken on 2015/12/16.
 */
public class NavAnimView extends FrameLayout{

    private LinearLayout linearLayout_checked;
    private LinearLayout linearLayout_unchecked;
    private LinearLayout.LayoutParams layoutParams_checked;
    private LinearLayout.LayoutParams layoutParams_unchecked;

    private int checkImg;//被选中的图片资源
    private int unCheckImg;//未被选中的图片资源
    private int count;//共有几个点

    public NavAnimView(Context context) {
        super(context);
    }

    public NavAnimView(Context context, AttributeSet attrs) {
        super(context, attrs);
        parestAttr(attrs);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.custem_nav, this, true);
        linearLayout_unchecked = (LinearLayout) findViewById(R.id.ll_unchecked);
        linearLayout_checked = (LinearLayout) findViewById(R.id.ll_checked);

        layoutParams_checked = new LinearLayout.LayoutParams(20, 20);
        layoutParams_unchecked = new LinearLayout.LayoutParams(20, 20);
    }

    /**
     * 解析自定义属性
     * @param attrs
     */
    private void parestAttr(AttributeSet attrs) {
        TypedArray typedArray = getResources().obtainAttributes(attrs, R.styleable.NavView);
        checkImg = typedArray.getResourceId(R.styleable.NavView_checkedImg, 0);
        unCheckImg = typedArray.getResourceId(R.styleable.NavView_uncheckedImg, 0);
    }


    public void setCount(int count){
        this.count = count;
        initView();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        linearLayout_checked.removeAllViews();
        linearLayout_unchecked.removeAllViews();
        if(count > 0) {
            ImageView iv = new ImageView(getContext());
            iv.setLayoutParams(layoutParams_checked);
            iv.setImageResource(checkImg);
            iv.setPadding(4, 4, 4, 4);
            linearLayout_checked.addView(iv);

            for (int i = 0; i < count; i++) {
                ImageView univ = new ImageView(getContext());
                univ.setLayoutParams(layoutParams_unchecked);
                univ.setImageResource(unCheckImg);
                univ.setPadding(4, 4, 4, 4);
                linearLayout_unchecked.addView(univ);
            }
        }
    }

    public void setIndexAnim(int position, float positionOffset){
        ImageView iv = (ImageView) linearLayout_checked.getChildAt(0);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) iv.getLayoutParams();
        layoutParams.leftMargin = (int) (iv.getWidth() * (position + positionOffset));
        iv.setLayoutParams(layoutParams);
    }
}
