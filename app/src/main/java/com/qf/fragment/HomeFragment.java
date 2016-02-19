package com.qf.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.qf.adapter.MsgAdapter;
import com.qf.custem.HeadView;
import com.qf.custem.PullToRefreshView;
import com.qf.lookhousedemo1513.CityActivity;
import com.qf.lookhousedemo1513.R;
import com.qf.model.CityEntity;
import com.qf.model.MsgEntity;
import com.qf.util.Constants;
import com.qf.util.DownUtil;
import com.qf.util.JSONUtil;
import com.qf.util.SharedUtil;

import java.util.List;

/**
 * Created by Ken on 2015/12/14.
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener, DownUtil.OnDownCompeletListern {
    private static final String TAG = "print";
    private Button btn_SelectCity;
    private PullToRefreshView lvMsg;
    private HeadView headView;//ListView的头部控件
    private MsgAdapter msgAdapter;
    public static final int REQUEST_CODE = 0x001;
    public static final int RESULET_CODE = 0x002;

    private int cityId = 1;//当前选中的城市ID，默认为北京

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home,null);
    }

    private Handler handler = new Handler();
    @Override
    public void init(View view) {
        lvMsg = (PullToRefreshView) view.findViewById(R.id.lv);
        lvMsg.setHeadView(R.layout.custem_pullhead);//给下拉刷新控件设置头部布局
        lvMsg.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void pullMoveY(int movey, View headView) {
                Log.d(TAG, "pullMoveY() returned: 下拉的偏移量" + movey);
                ImageView iv = (ImageView) headView.findViewById(R.id.iv_pullhead);//找到ImageView
                int count = movey/2;
                if(count <= 0){
                    count = 1;
                }
                if(count > 48){
                    count = 48;
                }
                String drawablename = "refresh_0";
                if(count < 10){
                    drawablename += ("0" + count);
                } else {
                    drawablename += count;
                }

                int resid = getResources().getIdentifier(drawablename, "drawable", getActivity().getPackageName());

                iv.setImageResource(resid);
            }

            @Override
            public void refresh(View headView) {
                //加载数据
                load();
                ImageView iv = (ImageView) headView.findViewById(R.id.iv_pullhead);//找到ImageView
                iv.setImageResource(R.drawable.icon_black_progressbar);
                //设置动画
                Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.pull_head_rotate);
                iv.startAnimation(animation);
            }

            @Override
            public void refreshCompelet(View headView) {
                Toast.makeText(getActivity(), "加载完成", Toast.LENGTH_SHORT).show();
                ImageView iv = (ImageView) headView.findViewById(R.id.iv_pullhead);//找到ImageView
                iv.setImageResource(R.drawable.refresh_001);//加载完成 重置图片
                iv.clearAnimation();//停止该控件动画
            }
        });

        headView = new HeadView(getActivity(), getActivity().getSupportFragmentManager());
        lvMsg.addHeaderView(headView);

        msgAdapter = new MsgAdapter(getActivity());
        lvMsg.setAdapter(msgAdapter);


        btn_SelectCity = (Button) view.findViewById(R.id.btnSelect);
        btn_SelectCity.setOnClickListener(this);

        //从共享参数中获取当前城市
        String cityname = SharedUtil.getString("cityname");
        int cityid = SharedUtil.getInt("cityid");
        if(cityname != null && cityid != -1){
            this.cityId = cityid;
            setButtonText(cityname);
        }
    }

    /**
     * 加载数据的方法
     */
    public void load() {
        String url = String.format(Constants.URL.HOME_LIST_URL, 20, 0, 0, this.cityId);
        DownUtil.downString(url, this);

        //加载头部数据
        headView.loadDatas(this.cityId);
    }

    @Override
    public void onClick(View v) {
        startActivityForResult(new Intent(getActivity(), CityActivity.class), REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE && resultCode == RESULET_CODE){
            CityEntity cityEntity = (CityEntity) data.getSerializableExtra("citys");
            cityId = cityEntity.getCityid();
            setButtonText(cityEntity.getCityname());

            //讲城市ID和城市名称保存到共享参数中
            SharedUtil.putString("cityname", cityEntity.getCityname());
            SharedUtil.putInt("cityid", cityId);
        }
    }

    /**
     * 修改button上的文本
     * @param cityname
     */
    private void setButtonText(String cityname){
        btn_SelectCity.setText(cityname);
        load();
    }

    /**
     * 下载JSON成功
     * @param url
     * @param obj
     */
    @Override
    public void downSucc(String url, Object obj) {
        if(obj != null){
            String json = obj.toString();
            List<MsgEntity> datas = JSONUtil.getMsgsByJSON(json);
            msgAdapter.setDatas(datas);
        }
        lvMsg.refreshSucc();
    }
}
