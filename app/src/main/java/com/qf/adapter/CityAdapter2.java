package com.qf.adapter;

import android.content.Context;
import android.widget.TextView;

import com.qf.lookhousedemo1513.R;
import com.qf.model.CityEntity;

/**
 * Created by Ken on 2015/12/15.
 */
public class CityAdapter2 extends AbsBaseAdapter2<CityEntity>{

    public CityAdapter2(Context context) {
        super(context, R.layout.item_label, R.layout.item_city);
    }

    @Override
    public void bindDatas(ViewHolder viewHolder, CityEntity data) {
        if(data.getType() == 0){//标签
            TextView tv_label = (TextView) viewHolder.getView(R.id.tv_label);
            tv_label.setText(data.getCityname());
        } else {//城市
            TextView tv_city = (TextView) viewHolder.getView(R.id.tv_city);
            tv_city.setText(data.getCityname());
        }
    }

    /**
     * 通过标签名，得到与该标签对应的item下标
     * @param label
     * @return
     */
    public int eqIndex(String label){
        int index = -1;
        for(int i = 0; i < datas.size(); i++){
            if(datas.get(i).eq(label)){
                index = i;
                break;
            }
        }
        return index;
    }

    @Override
    public boolean isEnabled(int position) {
        if(getItemViewType(position) == 0){
            return false;
        }
        return true;
    }
}
