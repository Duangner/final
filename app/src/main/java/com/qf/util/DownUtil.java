package com.qf.util;

import android.graphics.Bitmap;
import android.os.Handler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Ken on 2015/12/14.
 */
public class DownUtil {

    private static ExecutorService executorService = Executors.newFixedThreadPool(5);
    private static Handler handler = new Handler();

    /**
     * 下载JSON
     * @param url
     * @param onDownCompeletListern
     */
    public static void downString(final String url, final OnDownCompeletListern onDownCompeletListern){
        if(url != null){
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    final String json = HttpUtil.getStringByURL(url);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if(onDownCompeletListern != null){
                                onDownCompeletListern.downSucc(url, json);
                            }
                        }
                    });
                }
            });
        }
    }

    /**
     * 下载图片
     * @param url
     */
    public static void downBitmap(final String url, final OnDownCompeletListern onDownCompeletListern){
        if(url != null){
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    final Bitmap bitmap = HttpUtil.getImageByURL(url);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if(onDownCompeletListern != null){
                                onDownCompeletListern.downSucc(url, bitmap);
                            }
                        }
                    });
                }
            });
        }
    }

    public interface OnDownCompeletListern{
        void downSucc(String url, Object obj);
    }
}
