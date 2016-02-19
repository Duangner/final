package com.qf.custem;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Ken on 2015/12/14.
 */
public class SideView extends View{

    private static final String TAG = "print";
    private static String[] labels = {"热门","A","B","C","D","E","F","G","H","I","J","K","L","M",
            "N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};

//    /**
//     * 自己new的时候调用该构造方法
//     * @param context
//     */
//    public SideView(Context context) {
//        super(context);
//    }

    private Paint paint;
    private Paint checkPaint;
    private int txtHeight;//每一个文本所需要的高度

    private int checkedIndex = -1;

    private OnSelectListener onSelectListener;

    /**
     * 写到xml布局文件中时，系统默认调用该构造方法
     * @param context
     * @param attrs
     */
    public SideView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.parseColor("#88888888"));
        paint.setTextSize(20);
        paint.setAntiAlias(true);//抗锯齿

        checkPaint = new Paint();
        checkPaint.setColor(Color.parseColor("#5da5df"));
        checkPaint.setTextSize(20);
        checkPaint.setAntiAlias(true);//抗锯齿
    }

    /**
     * 绘制组件内容
    * @param canvas
    */
    @Override
    protected void onDraw(Canvas canvas) {
        txtHeight = getHeight()/labels.length;

        for(int i = 0; i < labels.length; i++){
            if(i == checkedIndex){
                canvas.drawText(labels[i], getWidth() / 2 - paint.measureText(labels[i]) / 2, txtHeight * (i + 1), checkPaint);
            } else {
                canvas.drawText(labels[i], getWidth() / 2 - paint.measureText(labels[i]) / 2, txtHeight * (i + 1), paint);
            }
        }
    }

    /**
     * 测量组件大小
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWH(widthMeasureSpec, 0), measureWH(heightMeasureSpec, 1));
    }

    /**
     * 测量宽高
     * type=0 测量宽度， type=1 测量高度
     */
    private int measureWH(int measureSpec, int type){
        int model = MeasureSpec.getMode(measureSpec);//获得当前空间值的一个模式
        int size = MeasureSpec.getSize(measureSpec);//获得当前空间值的推荐值

        switch (model){
            case MeasureSpec.EXACTLY://当你的控件设置了一个精确的值或者为match_parent时, 为这种模式
                return size;
            case MeasureSpec.AT_MOST://当你的控件设置为wrap_content时，为这种模式
                if(type == 0){
                    //测量宽度
                    size = (int) paint.measureText(labels[0]);
                    return size;
                } else {
                    //测量高度
                    return size;
                }
            case MeasureSpec.UNSPECIFIED://尽可能的多
                break;
        }
        return 0;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int y = (int) event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                moveY(y);
                break;
            case MotionEvent.ACTION_MOVE:
                moveY(y);
                break;
            case MotionEvent.ACTION_UP:
                checkedIndex = -1;
                invalidate();
                break;
        }
        return true;
    }

    private void moveY(int y){
        int checkindex = y/txtHeight;
        if(checkindex < 0){
            checkindex = 0;
        }

        if(checkindex >= labels.length){
            checkindex = labels.length - 1;
        }

        //如果当前选中的下标和上次记录的下标一样，则不再进行重绘，避免不必要的性能损耗
        if(checkindex == checkedIndex){
            return;
        }

        checkedIndex = checkindex;

        if(onSelectListener != null){
            onSelectListener.onSelect(labels[checkedIndex]);
        }

        invalidate();//重绘控件 -- 调用该方法后会立刻执行onDraw方法
    }

    public interface OnSelectListener{
        public void onSelect(String str);
    }

    public void setOnSelectListener(OnSelectListener onSelectListener) {
        this.onSelectListener = onSelectListener;
    }

    //    /**
//     * 该方法多用于自定义ViewGroup，摆放子控件的位置
//     * @param changed
//     * @param left
//     * @param top
//     * @param right
//     * @param bottom
//     */
//    @Override
//    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//        super.onLayout(changed, left, top, right, bottom);
//    }
}
