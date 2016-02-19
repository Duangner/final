package com.qf.lookhousedemo1513;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.qf.adapter.CityAdapter2;
import com.qf.custem.LabelView;
import com.qf.custem.SideView;
import com.qf.fragment.HomeFragment;
import com.qf.model.CityEntity;
import com.qf.util.Constants;
import com.qf.util.DownUtil;
import com.qf.util.JSONUtil;

import java.util.List;

/**
 * 城市选择页
 */
public class CityActivity extends BaseActivity implements DownUtil.OnDownCompeletListern, AdapterView.OnItemClickListener, SideView.OnSelectListener {

    private static final String TAG = "print";
    private ListView lvCity;
    private CityAdapter2 cityAdapter;

    private SideView sv;
    private LabelView labelv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);

        init();
        loadDatas();
    }

    private void init() {
        lvCity = (ListView) findViewById(R.id.lv_city);
        cityAdapter = new CityAdapter2(this);
        lvCity.setAdapter(cityAdapter);
        lvCity.setOnItemClickListener(this);

        sv = (SideView) findViewById(R.id.sv_id);
        sv.setOnSelectListener(this);

        labelv = (LabelView) findViewById(R.id.label_id);
    }

    private void loadDatas() {
        DownUtil.downString(Constants.URL.CITY_URL, this);
    }

    @Override
    public void downSucc(String url, Object obj) {
        if(obj != null) {
            String json = obj.toString();
            List<CityEntity> datas = JSONUtil.getCitysByJSON(json);
            cityAdapter.setDatas(datas);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CityEntity cityEntity = (CityEntity) cityAdapter.getItem(position);//获取选中的城市实体对象
        Intent intent = getIntent();
        intent.putExtra("citys", cityEntity);
        setResult(HomeFragment.RESULET_CODE, intent);
        finish();
    }

    /**
     * 侧边自定义控件的选中回调方法
     * @param str
     */
    @Override
    public void onSelect(String str) {
        int index = cityAdapter.eqIndex(str);
        if(index != -1) {
            lvCity.smoothScrollToPositionFromTop(index, 0);
        }
        labelv.setLabel(str);
    }
}
