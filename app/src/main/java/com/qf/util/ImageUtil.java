package com.qf.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * 图片缓存到本地
 */
public class ImageUtil {

    /**
     * 目标路径
     */
    private static final String IMAGE_PATH = Environment.getExternalStorageDirectory() + "/project_image/images";

    /**
     * 判断拓展卡是否装载
     * @return
     */
    public static boolean isMounted(){
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }


    /**
     * 保存图片到拓展卡
     * @param url
     */
    public static void saveImage(String url, Bitmap bitmap){
        if(!isMounted())
            return;

        File file = new File(IMAGE_PATH);
        if (!file.exists()){
            file.mkdirs();
        }

        try {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(new File(file, "" + url.hashCode())));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取图片
     * @param url
     * @return
     */
    public static Bitmap getImage(String url){
        if(!isMounted())
            return null;

        File file = new File(IMAGE_PATH, "" + url.hashCode());
        if(file.exists()){
            return BitmapFactory.decodeFile(file.getAbsolutePath());
        }

        return null;
    }
}
