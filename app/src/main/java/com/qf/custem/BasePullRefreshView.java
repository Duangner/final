package com.qf.custemview;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * ListView的下拉刷新封装
 */
public class BasePullRefreshView extends LinearLayout implements View.OnTouchListener {

    private static final String TAG = "print";
    private View headView;//头部控件
    private int headViewHeight;//头部控件的高度

    private ListView lv;//显示的内容

    private static final int NONE = 0;//普通状态
    private static final int PULL = 1;//正在下拉
    private static final int PULL_REFRESH = 2;//松手刷新
    private static final int REFRESHING = 3;//刷新中
    private int state = NONE;//控件当前的状态

    //回调接口对象
    private OnRefreshListener onRefresh;

    public BasePullRefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    /**
     * 初始化当前控件
     */
    private void init() {
        this.setOrientation(VERTICAL);//设为纵向排列

    }

    /**
     * 设置头部的方法
     * @param view
     */
    public void setHeadView(View view){
        headView = view;
        initView();
    }

    public void setHeadView(int resId){
        View view = LayoutInflater.from(getContext()).inflate(resId, null);
        setHeadView(view);
    }

    /**
     * 初始化内部控件
     */
    private void initView() {
        //处理下拉的头部
        headView.measure(0, 0);//测量组件的宽高
        headViewHeight = headView.getMeasuredHeight();//获得测量后的宽高
        headView.setPadding(0, -headViewHeight, 0, 0);
        this.addView(headView);


        //处理ListView
        lv = new ListView(getContext());
        lv.setOnTouchListener(this);
        lv.setDividerHeight(0);//取消listview的分割线
        this.addView(lv);
    }

    /**
     * ListView onTouch监听 -- 该方法先与ListView OnTouchEvent方法被调用，如果返回false，则调用OnTouchEvent,反之则不调用
     * @return
     */
    private int by = -1;
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //判断是否可以下拉 -- 返回true表示可以下拉，返回false表示不能下拉
        boolean ispull = isPull(event);
        //计算手势在Y轴上的位移
        int y = (int) event.getRawY();
        if(by == -1){
            by = y;
        }
        int movey = y - by;
        by = y;
        if(ispull){
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_MOVE:

//                    Log.d(TAG, "onTouch() 下滑的条件: " + movey + "   " + (headView.getPaddingTop() <= -headViewHeight) + "   " + (state == NONE));
                    if(movey <= 0 && headView.getPaddingTop() <= -headViewHeight && state == NONE){
                        return false;
                    }

                    //执行到这里，表示开始下拉
                    headView.setPadding(headView.getPaddingLeft(), headView.getPaddingTop() + movey / 2, headView.getPaddingRight(), headView.getPaddingBottom());
                    //通过偏移量改变头部的图片
                    if(state != REFRESHING) {
                        if(headView.getPaddingTop() > -headViewHeight && state == NONE){
                            state = PULL;//已经开拉
                        }

                        if(onRefresh != null) {
                            boolean flag = onRefresh.pullMoveY(headView.getPaddingTop() + headViewHeight, headView, headViewHeight);//调用接口回调中的方法
                            if(flag){
                                onRefresh.pullRefresh(headView.getPaddingTop() + headViewHeight, headView);
                                state = PULL_REFRESH;//当前状态为松手刷新
                            } else {
                                state = PULL;
                            }
                        }
                    }

                    break;
                case MotionEvent.ACTION_UP:
                default:
                    //松手执行方法
                    reset();
                    break;
            }

            return true;
        }
        return false;
    }

    /**
     * 重置控件
     */
    int backHeight = -headViewHeight;
    private Handler handler = new Handler();
    private void reset() {
        if(state != NONE){
            by = -1;

            if(state == PULL || state == REFRESHING){//普通下拉 或者 正在刷新
                backHeight = -headViewHeight;
                state = NONE;
            } else if(state == PULL_REFRESH){//松手刷新
                backHeight = 0;
                state = REFRESHING;//状态变为正在刷新
                if(onRefresh != null){
                    //回调刷新方法
                    onRefresh.refreshing(headView);
                }
            }

            final Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if(headView.getPaddingTop() > backHeight) {
                                headView.setPadding(headView.getPaddingLeft(), headView.getPaddingTop() - 5,
                                        headView.getPaddingRight(), headView.getPaddingBottom());
                            } else {
                                headView.setPadding(headView.getPaddingLeft(), backHeight,
                                        headView.getPaddingRight(), headView.getPaddingBottom());
                                timer.cancel();
                            }
                        }
                    });
                }
            }, 0, 5);
        }
    }

    /**
     * 判断是否可以下拉
     */
    private int y = -1;
    private boolean isPull(MotionEvent event) {
        if(state == REFRESHING){
            return false;
        }

        if(lv.getChildCount() > 0){
            View view = lv.getChildAt(0);
            if(view.getTop() == 0){

                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        y = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        if(y == (int)event.getRawY()){
                            y = -1;
                            return false;
                        }
                        break;
                }

                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    /**
     * 刷新完成
     */
    public void refreshCompelet(){
        reset();
        if (onRefresh != null)
            onRefresh.refreshCompelet(headView);
    }

    /**
     * 内部ListView的操作封装
     * @param adapter
     */
    public void setAdapter(ListAdapter adapter){
        lv.setAdapter(adapter);
    }

    public void addHeaderView(View view){
        lv.addHeaderView(view);
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener){
        lv.setOnItemClickListener(onItemClickListener);
    }

    public ListView getLv() {
        return lv;
    }

    /**
     * 回调的接口
     */

    public interface OnRefreshListener{
        //下拉偏移量回调方法 -- 该方法返回true 开始回调pullRefresh方法， 返回false 停止回调pullRefresh方法
        boolean pullMoveY(int movey, View headView, int headViewHeight);

        //松手刷新时回调方法
        void pullRefresh(int movey, View headView);

        //正在刷新
        void refreshing(View headView);

        //刷新完成
        void refreshCompelet(View headView);
    }

    public void setOnRefresh(OnRefreshListener onRefresh) {
        this.onRefresh = onRefresh;
    }
}
