package com.qf.custem;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.LruCache;
import android.widget.ImageView;

import com.qf.lookhousedemo1513.R;
import com.qf.util.DownUtil;
import com.qf.util.ImageUtil;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

/**
 * 带缓存的ImageView
 */
public class CacheImageView extends ImageView implements DownUtil.OnDownCompeletListern {

    private int defaultImage;

    public CacheImageView(Context context) {
        super(context);
    }

    public CacheImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paresAttr(attrs);
    }

    /**
     * 解析自定义属性
     * @param attrs
     */
    private void paresAttr(AttributeSet attrs) {
        TypedArray typedArray = getResources().obtainAttributes(attrs, R.styleable.CacheImageView);
        defaultImage = typedArray.getResourceId(R.styleable.CacheImageView_defaultImage, R.drawable.ktsf_list_item_bg);
    }


    /**
     * 定义一个一级缓存
     */
    public static LruCache<String, Bitmap> lruCache = new LruCache<String, Bitmap>((int) (Runtime.getRuntime().maxMemory() / 8)){
        /**
         * 用于计算缓存对象的大小
         * @param key
         * @param value
         * @return
         */

        @Override
        protected int sizeOf(String key, Bitmap value) {
            return value.getRowBytes() * value.getHeight();
        }

        /**
         * 被移除的Bitmap调用该方法
         * @param evicted
         * @param key
         * @param oldValue
         * @param newValue
         */
        @Override
        protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
            if(evicted){
                //从lruCache里移除，放入软引用的缓存中
                softCache.put(key, new SoftReference<Bitmap>(oldValue));
            }
        }
    };

    private static Map<String, SoftReference<Bitmap>> softCache = new HashMap<String, SoftReference<Bitmap>>();

    /**
     * 下载图片，放到该ImageView中
     * @param url
     */
    public void setUrl(String url){
        //设置默认图片
        this.setImageResource(defaultImage);

        this.setTag(url);

        Bitmap bitmap = getCache(url);//从缓存中获取
        if(bitmap == null){
            DownUtil.downBitmap(url, this);
        } else {
            this.setImageBitmap(bitmap);
        }
    }


    /**
     * 放入缓存
     * @param url
     * @param bitmap
     */
    private static void putCache(String url, Bitmap bitmap){
        lruCache.put(url, bitmap);
        ImageUtil.saveImage(url, bitmap);
    }

    /**
     * 从缓存中查找
     * @param url
     */
    private static Bitmap getCache(String url) {
        Bitmap bitmap = lruCache.get(url);
        if(bitmap == null){
            //从软引用集合中查找
            if(softCache.containsKey(url)){
                SoftReference<Bitmap> softReference = softCache.get(url);
                if(softReference != null){
                    bitmap = softReference.get();
                }
            }

            if(bitmap == null){
                //从磁盘中查找吧
                bitmap = ImageUtil.getImage(url);
            }

            if(bitmap != null){
                //移回lrucache中
                lruCache.put(url, bitmap);
            }
        }

        return bitmap;
    }


    /**
     * 图片下载回调方法
     * @param url
     * @param obj
     */
    @Override
    public void downSucc(String url, Object obj) {
        if(obj != null){
            Bitmap bitmap = (Bitmap) obj;
            if(this.getTag().equals(url)){
                setImageBitmap(bitmap);
            }

            //放入缓存
            putCache(url, bitmap);
        }
    }
}
