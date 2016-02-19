package com.qf.custem;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;


/**
 * 自定义标签控件，中间那个圈
 */
public class LabelView extends View{

    private Paint paint, txtPaint;
    private String label;

    public LabelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.parseColor("#79b9ed"));
        paint.setAntiAlias(true);

        txtPaint = new Paint();
        txtPaint.setColor(Color.WHITE);
        txtPaint.setTextSize(40);
        txtPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, 30, paint);
        if(label != null){
            canvas.drawText(label, getWidth()/2 - txtPaint.measureText(label)/2, getHeight()/2 + (txtPaint.descent() - txtPaint.ascent())/2 - txtPaint.descent(), txtPaint);
        }
    }

    /**
     * 外部调用，设置控件中间显示的文本
     * @param label
     */
    public void setLabel(String label){
        this.label = label;
        count = 1;
        taskThread();

        this.setVisibility(VISIBLE);
        invalidate();
    }

    private int count = 1;
    private Handler handler = new Handler();
    public void taskThread(){
        if(this.getVisibility() != GONE){
            return ;
        }

        new Thread(){
            @Override
            public void run() {
                while(true){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    count--;

                    if(count < 0){
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                setVisibility(GONE);
                            }
                        });
                        break;
                    }
                }
            }
        }.start();
    }
}
