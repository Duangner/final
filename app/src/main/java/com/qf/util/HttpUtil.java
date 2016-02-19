package com.qf.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Ken on 2015/12/14.
 */
public class HttpUtil {

    private static byte[] getBytesByUrl(String url){
        byte [] bytes = null;
        try {
            URL ul = new URL(url);//得到一个URL对象
            HttpURLConnection conn = (HttpURLConnection) ul.openConnection();
            conn.setDoInput(true);//是否从请求中获取数据
            conn.setReadTimeout(5000);//设置读超时
            conn.setRequestMethod("GET");//设置请求类型为get请求

            InputStream in = conn.getInputStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int len;
            byte[] bs = new byte[1024 * 10];
            while((len = in.read(bs)) != -1){
                out.write(bs, 0, len);
            }
            bytes = out.toByteArray();
            in.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bytes;
    }

    /**
     * 根据URL获得字符串
     * @param url
     * @return
     */
    public static String getStringByURL(String url) {
        byte[] bs = getBytesByUrl(url);
        if(bs != null){
            try {
                return new String(bs, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 根据URL获得图片
     * @param url
     * @return
     */
    public static Bitmap getImageByURL(String url) {
        byte[] bs = getBytesByUrl(url);
        if(bs != null){
            return BitmapFactory.decodeByteArray(bs, 0, bs.length);
        }
        return null;
    }
}
