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

    private EquipmentIssue(int id, User issuer, Thing product, String add_time, Boolean is_delete) {
        super(id, issuer, product, add_time,is_delete);
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
                            json.getString("add_time"),
                            json.getBoolean("is_delete")
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
        JSONArray array= JsonDataUtil.getJSONArray(JsonDataUtil.RESOURCE_URL+"equipment_issue/?issuer="+phone,true);
        return getEquipmentIssueJSONArray(array);
    }
    public static List<EquipmentIssue> findEquipmentIssues(String phone, int page){
        try {
            JSONArray array= JsonDataUtil.getJSONArray(String.format(JsonDataUtil.RESOURCE_URL+"equipment_issue/?issuer=%s&page=%d",phone,page));
            return getEquipmentIssueJSONArray(array);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 通过ID查发布信息
     * @param id 发布ID
     * @return
     */
    public static EquipmentIssue findEquipmentIssues(int id) throws JSONException {
        EquipmentIssue issue=null;
        JSONObject json= JsonDataUtil.getJSONObject(JsonDataUtil.RESOURCE_URL+"equipment_issue/?id="+id,false);
        issue=new EquipmentIssue(
                json.getInt("id"),
                User.findUser(json.getJSONObject("issuer")),
                Equipment.findEquipment(json.getJSONObject("product")),
                json.getString("add_time"),
                json.getBoolean("is_delete")
        );
        return issue;
    }
    /**
     * 通过产品ID查发布信息
     * @param id 产品ID
     * @return
     */
    public static EquipmentIssue findEquipmentIssueByProduct(int id) throws JSONException {
        EquipmentIssue issue=null;
        JSONObject json= JsonDataUtil.getJSONObject(JsonDataUtil.RESOURCE_URL+"equipment_issue/?product="+id,false);

        issue=new EquipmentIssue(
                json.getInt("id"),
                User.findUser(json.getJSONObject("issuer")),
                Equipment.findEquipment(json.getJSONObject("product")),
                json.getString("add_time"),
                json.getBoolean("is_delete")
        );
        return issue;
    }
    /**
     * 添加设备发布记录
     * @param issuer 发布者
     * @param product 产品
     * @return
     */
    public static boolean addEquipmentIssue( int issuer, int product) {
        String params ;
        params= String.format("{\n" +
                "    \"issuer\": %d,\n" +
                "    \"product\": %d,"+
                "    \"is_delete\": false\n"+
                "}",issuer,product);
        return JsonDataUtil.postJSONObject(JsonDataUtil.RESOURCE_URL+"equipment_issue/", params);
    }
}
