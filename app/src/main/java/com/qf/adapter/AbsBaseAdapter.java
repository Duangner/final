package com.qf.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * BaseAdapter的封装类
 */
public abstract class AbsBaseAdapter<T> extends BaseAdapter{

    private Context context;
    private List<T> datas;//数据源的类型无法确定，所以用泛型来表示
    private int resid;//无法确定item布局文件，所以通过外界传进来

    public AbsBaseAdapter(Context context, int resid){
        this.context = context;
        this.resid = resid;
        this.datas = new ArrayList<>();
    }

    public void setDatas(List<T> datas){
        this.datas = datas;
        this.notifyDataSetChanged();
    }

    public void addDatas(List<T> datas){
        this.datas.addAll(datas);
        this.notifyDataSetChanged();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView != null){
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(context).inflate(resid, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }

        //从viewholder中获取控件赋值
        bindDatas(viewHolder, datas.get(position));

        return convertView;
    }

    /**
     * 提供一个抽象方法，用于子适配器来进行数据绑定
     * @param viewHolder
     * @param data
     */
    public abstract void bindDatas(ViewHolder viewHolder, T data);


    /**
     * ViewHolder的作用，缓存item布局中的控件对象，以免下一次再fingViewById
     */
    class ViewHolder{
        //使用一个Map集合，用来存放需要使用到的控件对象，key值即为控件ID
        private Map<Integer, View> mapCache = new HashMap<>();
        //item的布局对象
        private View layoutView;

        public ViewHolder(View layoutView){
            this.layoutView = layoutView;
        }

        //提供一个获得布局中子控件的方法，参数为控件id
        public View getView(int id){
            if(mapCache.containsKey(id)){//查看该控件是否存在于map集合中
                return mapCache.get(id);//如果存在直接从map中获得该控件，并返回
            } else {
                View view = layoutView.findViewById(id);//如果不存在，那么需要从布局对象中，findViewById
                mapCache.put(id, view);//缓存进map集合中
                return view;
            }
        }
    }
}
