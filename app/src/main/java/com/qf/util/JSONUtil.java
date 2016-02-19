package com.qf.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qf.model.CityEntity;
import com.qf.model.HeadEntity;
import com.qf.model.MsgEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ken on 2015/12/14.
 */
public class JSONUtil {

    private static String[] labels = {"hotcities","A","B","C","D","E","F","G","H","I","J","K","L","M",
            "N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};

    /**
     * 解析城市JSON
     * List<CityEntity> -->
     * CityEntity - type = 0, cityname = A
     * CityEntity - type = 1, cityname = 鞍山
     * CityEntity - type = 1, cityname = 安阳 。。。
     * CityEntity - type = 0, cityname = B
     * CityEntity - type = 1, cityname = 北京
     *
     * @param json
     * @return
     */
    public static List<CityEntity> getCitysByJSON(String json){
        if(json != null){
            List<CityEntity> cityEntities = new ArrayList<>();
            try {
                JSONObject jsonObject = new JSONObject(json);
                if(jsonObject.getInt("retcode") == 0){
                    jsonObject = jsonObject.getJSONObject("cities");
                    for(int i = 0; i < labels.length; i++){
                        JSONArray jsonArray = jsonObject.optJSONArray(labels[i]);
                        if(jsonArray != null) {
                            if(i == 0){
                                CityEntity cityEntity = new CityEntity("热门城市", 0);
                                cityEntities.add(cityEntity);
                            } else {
                                CityEntity cityEntity = new CityEntity(labels[i], 0);
                                cityEntities.add(cityEntity);
                            }


                            TypeToken<List<CityEntity>> tt = new TypeToken<List<CityEntity>>() {
                            };
                            List<CityEntity> datas = new Gson().fromJson(jsonArray.toString(), tt.getType());
                            cityEntities.addAll(datas);
                        }

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return cityEntities;
        }

        return null;
    }

    /**
     * 解析json获得首页List数据
     * @param json
     * @return
     */
    public static List<MsgEntity> getMsgsByJSON(String json){
        List<MsgEntity> datas = null;
        if(json != null){
            try {
                JSONObject jsonObject = new JSONObject(json);
                if(jsonObject.getInt("retcode")==0){
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    TypeToken<List<MsgEntity>> tt = new TypeToken<List<MsgEntity>>(){};
                    datas = new Gson().fromJson(jsonArray.toString(), tt.getType());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return datas;
    }

    /**
     * 解析头部数据
     * @param json
     * @return
     */
    public static List<HeadEntity> getHeadsByJSON(String json){
        List<HeadEntity> datas = null;
        if(json != null){
            try {
                JSONObject jsonObject = new JSONObject(json);
                if(jsonObject.getInt("retcode")==0){
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    TypeToken<List<HeadEntity>> tt = new TypeToken<List<HeadEntity>>(){};
                    datas = new Gson().fromJson(jsonArray.toString(), tt.getType());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return datas;
    }
}
