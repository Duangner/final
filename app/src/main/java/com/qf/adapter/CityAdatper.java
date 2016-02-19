package com.qf.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qf.lookhousedemo1513.R;
import com.qf.model.CityEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ken on 2015/12/14.
 */
public class CityAdatper extends BaseAdapter{

    private Context context;
    private List<CityEntity> datas;

    public CityAdatper(Context context){
        this.context = context;
        datas = new ArrayList<>();
    }

    public void setDatas(List<CityEntity> datas){
        this.datas = datas;
        this.notifyDataSetChanged();
    }

    public void addDatas(List<CityEntity> datas){
        this.datas.addAll(datas);
        this.notifyDataSetChanged();
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

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return datas.get(position).getType();
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView != null){
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            viewHolder = new ViewHolder();
            if(getItemViewType(position) == 0){
                convertView = LayoutInflater.from(context).inflate(R.layout.item_label, null);
                viewHolder.tv = (TextView) convertView.findViewById(R.id.tv_label);
            } else if(getItemViewType(position) == 1){
                convertView = LayoutInflater.from(context).inflate(R.layout.item_city, null);
                viewHolder.tv = (TextView) convertView.findViewById(R.id.tv_city);
            }
            convertView.setTag(viewHolder);
        }

        viewHolder.tv.setText(datas.get(position).getCityname());
        return convertView;
    }

    class ViewHolder{
        TextView tv;
    }
}
