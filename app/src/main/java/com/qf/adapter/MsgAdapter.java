package com.qf.adapter;

import android.content.Context;
import android.widget.TextView;

import com.qf.custem.CacheImageView;
import com.qf.lookhousedemo1513.R;
import com.qf.model.MsgEntity;

/**
 * Created by Ken on 2015/12/15.
 */
public class MsgAdapter extends AbsBaseAdapter2<MsgEntity>{

    public MsgAdapter(Context context) {
        super(context, R.layout.item_homemsg, R.layout.item_homemsg2);
    }

    @Override
    public void bindDatas(ViewHolder viewHolder, MsgEntity data) {
        if(data.getType() == 0) {
            CacheImageView iv = (CacheImageView) viewHolder.getView(R.id.iv_msg);
            iv.setUrl(data.getThumbnail());
            TextView tv = (TextView) viewHolder.getView(R.id.tv_msg);
            tv.setText(data.getTitle());
            TextView tv_c = (TextView) viewHolder.getView(R.id.tv_cont);
            tv_c.setText(data.getSummary());
        } else {
            CacheImageView iv = (CacheImageView) viewHolder.getView(R.id.iv_msg);
            iv.setUrl(data.getGroupthumbnail());
            TextView tv = (TextView) viewHolder.getView(R.id.tv_msg);
            tv.setText(data.getTitle());
        }
    }
}
