package com.example.lenovo.geographysharing.Element;

import com.example.lenovo.geographysharing.Utils.JsonDataUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/21.
 */

public class Demand implements Serializable {
    private Integer demand_id;
    private User demander;
    private String type;
    private String title;
    private String content;
    private String time;
    private String money;

    private Demand(Integer demand_id, User demander, String type,
                   String title, String content, String time, String money) {
        this.demand_id = demand_id;
        this.demander = demander;
        this.type = type;
        this.title = title;
        this.content = content;
        this.time = time;
        this.money = money;

    }

    public Integer getDemand_id() {
        return demand_id;
    }

    public void setDemand_id(Integer demand_id) {
        this.demand_id = demand_id;
    }

    public User getDemander() {
        return demander;
    }

    public void setDemander(User demander) {
        this.demander = demander;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    /**
     * 解析需求JSONArray
     * @param array JSONArray对象
     * @return 需求对象集合
     */
    private static List<Demand> getDemandFromJSONArray(JSONArray array){
        List<Demand> list=new ArrayList<Demand>();
        if(array.length()<=0) {
            return null;
        }else {
            for (int i=0;i<array.length();i++) {
                try {
                    JSONObject json =array.getJSONObject(i);
                    //id, type, owner, name, date, over, expense, start, end, price, picture, parameter, place, comment
                    list.add(new Demand(
                            json.getInt("demand_id"),
                            User.findUser(json.getJSONObject("demander")),
                            json.getString("type"),
                            json.getString("title"),
                            json.getString("content"),
                            json.getString("time"),
                            json.getString("money")
                    ));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return list;
        }
    }

    /**
     * 按页查找需求
     * @param page 页码
     * @return 需求对象集合
     */
    @Deprecated
    public static List<Demand> findDemands(int page){
        JSONArray array = JsonDataUtil.getJSONArray(JsonDataUtil.RESOURCE_URL+"demand/?page=" + page);
        return getDemandFromJSONArray(array);
    }

    /**
     * 关键字查找
     * @param keyWord 关键字
     * @param page 页码
     * @return 需求对象集合
     */
    @Deprecated
    public static List<Demand> findDemands(String keyWord, int page){
        JSONArray array = JsonDataUtil.getJSONArray(JsonDataUtil.RESOURCE_URL+"demand/?page=" + page);
        return getDemandFromJSONArray(array);
    }

    /**
     * 通过用户手机号获取用户发布的需求
     * @param phone 手机号
     * @return 需求列表
     */
    public static List<Demand> findDemands(String phone){
        JSONArray array = JsonDataUtil.getJSONArray(JsonDataUtil.RESOURCE_URL+"demand/?demander=" + phone,false);
        return getDemandFromJSONArray(array);
    }

    /**
     * 发布需求
     * @param demand_id 需求id
     * @param type 需求类型
     * @param title 需求标题
     * @param content 需求内容
     * @param time 需求时间
     * @param money 提供资金
     * @param demander 需求发布者
     * @return 发布是否成功
     */
    public static boolean addDemand(int demand_id,String type,String title,String content,String time,String money,int demander){
        String params ;
        params=String.format("{\n" +
                "   \"demand_id\": %d,\n" +
                "    \"type\": \"%s\",\n" +
                "    \"title\": \"%s\",\n" +
                "    \"content\": \"%s\",\n" +
                "    \"time\": \"%s\",\n" +
                "    \"money\": \"%s\",\n" +
                "    \"demander\": %d"+
                "}",demand_id,type,title,content,time,money,demander);
        return JsonDataUtil.postJSONObject(JsonDataUtil.RESOURCE_URL+"demand/", params);
    }
}
