package com.qf.custem;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.qf.fragment.HeadVpFragment;
import com.qf.lookhousedemo1513.R;
import com.qf.model.HeadEntity;
import com.qf.util.Constants;
import com.qf.util.DownUtil;
import com.qf.util.JSONUtil;

import java.util.List;

/**
 * 头部组合控件
 */
public class HeadView extends FrameLayout implements DownUtil.OnDownCompeletListern, ViewPager.OnPageChangeListener {

    private static final String TAG = "print";
    private LoopViewPager vp;
    private NavAnimView nv;//导航控件
    private MyFragmentPagerAdapter myFragmentPagerAdapter;
    private FragmentManager fm;

    public HeadView(Context context, FragmentManager fm) {
        super(context);
        this.fm = fm;
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.custem_headview, this, true);
        vp = (LoopViewPager) this.findViewById(R.id.vp_head);
        nv = (NavAnimView) this.findViewById(R.id.nv_head);
    }

    /**
     * 加载数据的方法，供外界调用，参数为城市ID
     * @param cityid
     */
    public void loadDatas(int cityid){
        String url = String.format(Constants.URL.HOME_HEAD_URL, cityid);
        DownUtil.downString(url, this);
    }

    @Override
    public void downSucc(String url, Object obj) {
        if(obj != null){
            String json = obj.toString();
            List<HeadEntity> datas = JSONUtil.getHeadsByJSON(json);
            nv.setCount(datas.size());
            myFragmentPagerAdapter = new MyFragmentPagerAdapter(fm, datas);
            vp.setAdapter(myFragmentPagerAdapter);
            vp.addOnPageChangeListener(this);
        }
    }

    /**
     * ViewPager的监听
     * @param position
     * @param positionOffset 变化区间 0~1(永远不会到1)
     * @param positionOffsetPixels
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        nv.setIndexAnim(position, positionOffset);
    }

    @Override
    public void onPageSelected(int position) {
//        nv.setIndex(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * ViewPager的适配器
     * FragmentStatePagerAdapter
     * FragmentPagerAdapter
     */
    class MyFragmentPagerAdapter extends FragmentStatePagerAdapter{

        private List<HeadEntity> datas;

        public MyFragmentPagerAdapter(FragmentManager fm, List<HeadEntity> datas) {
            super(fm);
            this.datas = datas;
        }

        @Override
        public Fragment getItem(int position) {
            position = LoopViewPager.toRealPosition(position, getCount());
            HeadVpFragment hf = new HeadVpFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("datas", datas.get(position % getCount()));
            hf.setArguments(bundle);
            return hf;
        }

        @Override
        public int getCount() {
            return datas.size();
        }
    }
}
