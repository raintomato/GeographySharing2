package com.example.lenovo.geographysharing.Element;

import com.example.lenovo.geographysharing.Element.BaseElement.Issue;
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

public class EquipmentIssue extends Issue {

    private EquipmentIssue(int id, User issuer, Thing product, String add_time) {
        super(id, issuer, product, add_time);
    }
    private static List<EquipmentIssue> getEquipmentIssueJSONArray(JSONArray array){
        List<EquipmentIssue> list=new ArrayList<EquipmentIssue>();
        if(array.length()<=0) {
            return null;
        }else {
            for (int i=0;i<array.length();i++) {
                try {
                    JSONObject json =array.getJSONObject(i);
                    list.add(new EquipmentIssue(
                            json.getInt("id"),
                            User.findUser(json.getJSONObject("issuer")),
                            Equipment.findEquipment(json.getJSONObject("product")),
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
     * 查询某个用户发布的技术
     * @param phone 用户电话号码
     * @return
     */
    public static List<EquipmentIssue> findEquipmentIssues(String phone){
        JSONArray array= JsonDataUtil.getJSONArray(JsonDataUtil.RESOURCE_URL+"equipment_issue/?issuer="+phone,false);
        return getEquipmentIssueJSONArray(array);
    }

    /**
     * 添加设备发布记录
     * @param add_time 添加时间
     * @param issuer 发布者
     * @param product 产品
     * @return
     */
    public static boolean addEquipmentIssue(String add_time,int issuer,int product) {
        String params ;
        params=String.format("{\n" +
                "   \"add_time\": \"%s\",\n" +
                "    \"issuer\": %d,\n" +
                "    \"product\": %d"+
                "}",add_time,issuer,product);
        return JsonDataUtil.postJSONObject(JsonDataUtil.RESOURCE_URL+"equipment_issue/", params);
    }
}
