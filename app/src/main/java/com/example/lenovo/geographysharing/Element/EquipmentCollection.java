package com.example.lenovo.geographysharing.Element;

import com.example.lenovo.geographysharing.Element.BaseElement.Collection;
import com.example.lenovo.geographysharing.Element.BaseElement.Thing;
import com.example.lenovo.geographysharing.Utils.JsonDataUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/20.
 */

public class EquipmentCollection extends Collection {
    private EquipmentCollection(int id, Thing product, User collector, String add_time) {
        super(id, product, collector, add_time);
    }

    private static List<EquipmentCollection> getEquipmentCollectionJSONArray(JSONArray array){
        List<EquipmentCollection> list=new ArrayList<EquipmentCollection>();
        if(array==null)
            return null;
        if(array.length()<=0) {
            return null;
        }else {
            for (int i=0;i<array.length();i++) {
                try {
                    JSONObject json =array.getJSONObject(i);
                    list.add(new EquipmentCollection(
                            json.getInt("id"),
                            Equipment.findEquipment(json.getJSONObject("product")),
                            User.findUser(json.getJSONObject("collector")),
                            json.getString("add_time")
                    ));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return list;
        }
    }

    /**
     * 通过用户手机号获取收藏某用户设备
     * @param phone 用户手机号
     * @return 用户收藏的所有设备
     */
    public static List<EquipmentCollection> findEquipmentCollections(String phone){
        JSONArray array= JsonDataUtil.getJSONArray(JsonDataUtil.RESOURCE_URL+"equipment_collection/?format=json&collector="+phone,false);
        return getEquipmentCollectionJSONArray(array);
    }

    /**
     * 通过收藏id获取收藏
     * @param id
     * @return
     * @throws JSONException
     */
    public static EquipmentCollection findEquipmentCollection(int id) throws JSONException {
        EquipmentCollection collection=null;
        JSONObject json= JsonDataUtil.getJSONObject(JsonDataUtil.RESOURCE_URL+"equipment_collection/?format=json&id="+id,false);
        collection=new EquipmentCollection(
                json.getInt("id"),
                Equipment.findEquipment(json.getJSONObject("product")),
                User.findUser(json.getJSONObject("collector")),
                json.getString("add_time")
        );
        return collection;
    }
    public static EquipmentCollection findEquipmentCollection(String phone,int id) {
        EquipmentCollection collection=null;
        JSONObject json= JsonDataUtil.getJSONObject(String.format((JsonDataUtil.RESOURCE_URL+"equipment_collection/?format=json&collector=%s&product_id=%d"),phone,id),false);
        try {
            collection=new EquipmentCollection(
                    json.getInt("id"),
                    Equipment.findEquipment(json.getJSONObject("product")),
                    User.findUser(json.getJSONObject("collector")),
                    json.getString("add_time")
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return collection;
    }
    /**
     * 添加设备收藏
     * @param add_time 添加时间
     * @param collector 收藏者
     * @param product 产品
     * @return 是否添加成功
     */
    public static boolean addEquipmentCollection(String add_time,int collector,int product) {
        String params ;
        params=String.format("{\n" +
                "       \"add_time\": \"%s\",\n" +
                "        \"collector\": %d,\n" +
                "        \"product\": %d"+
                "}",add_time,collector,product);
        return JsonDataUtil.postJSONObject(JsonDataUtil.RESOURCE_URL+"equipment_collection/", params);
    }

    /**
     * 通过id删除收藏
     * @param id 收藏id
     * @return 是否成功
     */
    public static boolean cancelCollection(int id){
        return JsonDataUtil.deleteJSONObject(JsonDataUtil.RESOURCE_URL+"equipment_collection/"+id+"/");
    }

    /**
     * 判断某用户是否收藏某设备
     * @param phone 用户电话
     * @param id 设备id
     * @return 是否收藏
     */
    public static boolean userIsCollect(String phone,int id){
        List <EquipmentCollection> collections=findEquipmentCollections(phone);
        for(EquipmentCollection collection:collections){
            if(((Equipment)(collection.getProduct())).getId()==id){
                return true;
            }
        }
        return false;
    }
}
