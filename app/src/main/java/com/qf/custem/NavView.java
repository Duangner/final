package com.qf.custem;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.qf.lookhousedemo1513.R;

/**
 * 导航控件
 */
public class NavView extends LinearLayout{

    private int checkImg;//被选中的图片资源
    private int unCheckImg;//未被选中的图片资源
    private int count;//共有几个点

    private LinearLayout.LayoutParams layoutParams;

    public NavView(Context context) {
        super(context);
        init();
    }

    public NavView(Context context, AttributeSet attrs) {
        super(context, attrs);
        parestAttr(attrs);
        init();
    }

    /**
     * 控件初始化
     */
    private void init() {
        this.setOrientation(HORIZONTAL);
        layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if(count > 0){
            for(int i = 0; i < count; i++){
                ImageView imageView = new ImageView(getContext());
                layoutParams.leftMargin = 4;
                layoutParams.rightMargin = 4;
                imageView.setLayoutParams(layoutParams);
                if(i == 0){
                    imageView.setImageResource(checkImg);
                    imageView.setTag("checked");
                } else {
                    imageView.setImageResource(unCheckImg);
                }
                this.addView(imageView);
            }
        }
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

    /**
     * 设置导航的数量
     * @param count
     */
    public void setCount(int count){
        this.count = count;
        init();
    }

    /**
     * 设置第几个被选中
     * @param index
     */
    public void setIndex(int index){
        //找到原来被选中的图片 设为未被选中
        ImageView ivchecked = (ImageView) this.findViewWithTag("checked");
        ivchecked.setImageResource(unCheckImg);
        ivchecked.setTag(null);

        //找到要被选中的图片 设为被选中
        ImageView ivunchecked = (ImageView) this.getChildAt(index);
        ivunchecked.setImageResource(checkImg);
        ivunchecked.setTag("checked");
    }
}
