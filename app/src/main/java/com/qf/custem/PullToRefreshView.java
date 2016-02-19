package com.qf.custem;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 下拉刷新控件
 */
public class PullToRefreshView extends LinearLayout implements View.OnTouchListener {

    private static final String TAG = "print";
    private View headView;//头部控件
    private int headViewHeight;//头部控件的高度
    private ListView lvCont;//头部下方的ListView
    private LinearLayout.LayoutParams layoutParams;
    private OnRefreshListener onRefreshListener;


    /**
     * 定义下拉刷新的4种状态
     * @param context
     * @param attrs
     */
    private static final int NONE = 0;//普通状态
    private static final int PULL = 1;//下拉状态
    private static final int PULL_REFRESH = 2;//松手刷新状态
    private static final int REFRESHING = 3;//正在刷新状态
    private int state = NONE;//控件当前的状态，默认为普通状态

    public PullToRefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * 设置接口回调
     * @param onRefreshListener
     */
    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }

    /**
     * 初始化
     */
    private void init() {
        this.setOrientation(VERTICAL);//将线性布局设置为纵向
        layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    /**
     * 必须调用的方法 -- 设置头部的控件
     */
    public void setHeadView(View headView){
        this.headView = headView;
        initView();
    }

    //重载方法 -- 通过布局文件的ID 获得头部组件的对象headView
    public void setHeadView(int resid){
        View view = LayoutInflater.from(getContext()).inflate(resid, null);
        setHeadView(view);
    }

    /**
     * 初始化头部组件和ListView
     */
    private void initView() {
        //将头部控件添加进线性布局
        if(headView != null){
            headView.measure(0, 0);//测量头部组件的宽高
            headViewHeight = headView.getMeasuredHeight();//得到测量后的头部高度
            headView.setPadding(0, -headViewHeight, 0, 0);//设置paddingTop为-headViewHeight，隐藏头部控件
            this.addView(headView);
        }

        //将ListView添加进线性布局
        lvCont = new ListView(getContext());
        lvCont.setLayoutParams(layoutParams);
        lvCont.setOnTouchListener(this);//设置ListView的触摸监听方法
        this.addView(lvCont);
    }

    /**
     * ListView的触摸监听
     * onTouch 和 onTouchEvent的区别:
     * 如果设置了onTouch,那么onTouch会优先于onTouchEvent方法被调用，
     * 如果onTouch返回true，则事件无法传递到onTouchEvent方法中，即onTouchEvent方法不会被调用。
     * 如果onTouch返回false，则onTouchEvent正常被调用。
     * @param v
     * @param event
     * @return
     */
    private int by = -1;//记录上一次的y坐标
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        boolean ispull = isPull();//判断是否需要处理事件
        //计算偏移量
        int y = (int) event.getRawY();
        if(by == -1){
            by = y;
        }
        int movey = y - by;//计算偏移量--两两之间计算的偏移量
        by = y;
        if(ispull){
            /***
             * movey <= 0 表示手指往上滑动，那么事件交给ListView
             * headView.getPaddingTop() <= -headViewHeight 表示头部没有被拉出，事件交给ListView
             * state == NONE 如果当前的状态为普通状态，事件交给ListView
             */
            if(movey <= 0 && headView.getPaddingTop() <= -headViewHeight && state == NONE){
                return false;
            }

            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_MOVE:
                    /**
                     * 根据偏移量开始设置头部控件的PaddingTop -- 表示已经开拉了
                     */
                    headView.setPadding(headView.getPaddingLeft(), headView.getPaddingTop() + movey/2, headView.getPaddingRight(), headView.getPaddingBottom());

                    if(headView.getPaddingTop() > -headViewHeight){
                        //回调偏移量方法
                        if(onRefreshListener != null){
                            onRefreshListener.pullMoveY(headView.getPaddingTop() + headViewHeight, headView);
                        }

                        if(headView.getPaddingTop() >= 0){
                            state = PULL_REFRESH;
                        } else {
                            state = PULL;//状态置为下拉状态
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    //手指放开时触发
                    reset();
                    break;
            }

            return true;
        }
        //开始处理事件
        return false;
    }

    /**
     * 用于判断当前事件是否交给ListView -- 返回true表示拦截事件，返回false表示透传事件
     * @return
     */
    private boolean isPull(){
        if(state == REFRESHING){//如果当前的状态正在下拉，则不处理事件
            return false;
        }

        if(lvCont.getCount() > 0){
            View viewChile = lvCont.getChildAt(0);//获取ListView下的第一个Item
            int top = viewChile.getTop();//获取该控件的top（上边缘），所在的位置
            if(top == 0){//如果top == 0表示ListView已经拉到顶部了
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * 重置控件的状态
     */
    private int backHeight = -headViewHeight;
    private Handler handler = new Handler();
    private void reset() {
        if(state != NONE){//如果当前状态不是普通状态，再进行重置处理
            by = -1;//重置坐标记录点的位置

            if(state == PULL || state == REFRESHING){//如果当前的状态为‘下拉状态’或者为‘正在刷新’状态
                //直接回到顶部
                backHeight = -headViewHeight;
                state = NONE;//将状态置为普通状态
            } else if(state == PULL_REFRESH){//如果当前的状态为‘松手刷新’状态，那么控件不回到顶部
                //将控件刚好展示出来
                backHeight = 0;
                state = REFRESHING;//将状态置为正在刷新
                //回调刷新方法
                if(onRefreshListener != null){
                    onRefreshListener.refresh(headView);
                }
            }

            /**
             * 定义一个定时器
             */
            final Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //在子线程中执行
                            if(headView.getPaddingTop() > backHeight){
                                //表示还没有返回到预定的结果，继续返回
                                headView.setPadding(headView.getPaddingLeft(), headView.getPaddingTop() - 5, headView.getPaddingRight(), headView.getPaddingBottom());
                            } else {
                                //已经减到了预定高度，为了避免超过的情况发生，直接将预定高度赋给控件
                                headView.setPadding(headView.getPaddingLeft(), backHeight, headView.getPaddingRight(), headView.getPaddingBottom());
                                timer.cancel();//关闭定时器
                            }
                        }
                    });
                }
            }, 0, 5);

            //---->100 run -->5 run --> 5 run --->5 run
        }
    }

    /**
     * 刷新完成调用该方法
     */
    public void refreshSucc(){
        reset();//重置控件
        //回调刷新完成的方法
        if(onRefreshListener != null){
            onRefreshListener.refreshCompelet(headView);
        }
    }

    /**
     * 添加ListView的头部
     * @param view
     */
    public void addHeaderView(View view){
        lvCont.addHeaderView(view);
    }

    /**
     * 给ListView添加适配器
     * @param adapter
     */
    public void setAdapter(ListAdapter adapter){
        lvCont.setAdapter(adapter);
    }

    /**
     * 获得控件内部的ListView--便于做一些其他操作
     * @return
     */
    public ListView getListView(){
        return lvCont;
    }

    /**
     * 定义接口回调
     */
    public interface OnRefreshListener{
        /**
         * 下拉偏移量的方法
         * @param movey 往下拉的偏移量
         * @param headView 头部的控件
         */
        void pullMoveY(int movey, View headView);

        /**
         * 需要刷新的方法
         * @param headView
         */
        void refresh(View headView);

        /**
         * 刷新完成时回调的方法
         * @param headView
         */
        void refreshCompelet(View headView);
    }
}
