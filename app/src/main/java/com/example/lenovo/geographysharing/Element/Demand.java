package com.example.lenovo.geographysharing.Element;

import android.util.Log;

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
    private Integer id;
    private User demander;
    private String type;
    private String title;
    private String content;
    private String time;
    private String money;
    private Boolean is_delete;

    public Boolean getIs_delete() {
        return is_delete;
    }

    public void setIs_delete(Boolean is_delete) {
        this.is_delete = is_delete;
    }

    private Demand(Integer id, User demander, String type,
                   String title, String content, String time, String money, Boolean is_delete) {
        this.id = id;
        this.demander = demander;
        this.type = type;
        this.title = title;
        this.content = content;
        this.time = time;
        this.money = money;
        this.is_delete = is_delete;

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
        if(array==null){
            return null;
        }
        if(array.length()<=0) {
            return null;
        }else {
            for (int i=0;i<array.length();i++) {
                try {
                    JSONObject json =array.getJSONObject(i);
                    //id, type, owner, name, date, over, expense, start, end, price, picture, parameter, place, comment
                    list.add(new Demand(
                            json.getInt("id"),
                            User.findUser(json.getJSONObject("demander")),
                            json.getString("type"),
                            json.getString("title"),
                            json.getString("content"),
                            json.getString("time"),
                            json.getString("money"),
                            json.getBoolean("is_delete")
                    ));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return list;
        }
    }

    public static Demand findDemand(int id) throws JSONException {
        JSONObject json=JsonDataUtil.getJSONObject(JsonDataUtil.RESOURCE_URL+"demand/?id="+id);
        Demand demand=new Demand(
                json.getInt("id"),
                User.findUser(json.getJSONObject("demander")),
                json.getString("type"),
                json.getString("title"),
                json.getString("content"),
                json.getString("time"),
                json.getString("money"),
                json.getBoolean("is_delete")
        );
        return demand;
    }
    /**
     * 按页查找需求
     * @param page 页码
     * @return 需求对象集合
     */
    public static List<Demand> findDemands(int page){
        JSONArray array = JsonDataUtil.getJSONArray(JsonDataUtil.RESOURCE_URL+"demand/?ordering=-time&is_delete=true&page=" + page);
        return getDemandFromJSONArray(array);
    }


    /**
     * 关键字查找
     * @param keyWord 关键字
     * @param page 页码
     * @return 需求对象集合
     */
    @Deprecated
    public static List<Demand> findDemand(String keyWord, int page){
        JSONArray array = JsonDataUtil.getJSONArray(JsonDataUtil.RESOURCE_URL+"demand/?page=" + page, false);
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

    public static List<Demand> findDemands(String phone,int page){
        JSONArray array = JsonDataUtil.getJSONArray(String.format(JsonDataUtil.RESOURCE_URL+"demand/?demander=%s&page=%d",phone,page));
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
    public static boolean addDemand(int demand_id, String type, String title, String content, String time, String money, int demander,String place){
        String params ;
        params= String.format("{\n" +
                "   \"id\": %d,\n" +
                "    \"type\": \"%s\",\n" +
                "    \"title\": \"%s\",\n" +
                "    \"content\": \"%s\",\n" +
                "    \"time\": \"%s\",\n" +
                "    \"money\": \"%s\",\n" +
                "    \"demander\": %d,\n"+
                "    \"is_delete\": false,\n"+
                "    \"place\":\"%s\""+
                "}",demand_id,type,title,content,time,money,demander,place);
        Log.i("demand",params);
        return JsonDataUtil.postJSONObject(JsonDataUtil.RESOURCE_URL+"demand/", params);
    }
    /**
     * 修改需求信息
     * @param demand_id 需求id
     * @param type 需求类型
     * @param title 需求标题
     * @param content 需求内容
     * @param time 需求时间
     * @param money 提供资金
     * @param demander 需求发布者
     * @return 是否成功
     */
    public static boolean updateDemand(int demand_id, String type, String title, String content, String time, String money, int demander){
        String params ;
        params= String.format("{\n" +
                "   \"id\": %d,\n" +
                "    \"type\": \"%s\",\n" +
                "    \"title\": \"%s\",\n" +
                "    \"content\": \"%s\",\n" +
                "    \"time\": \"%s\",\n" +
                "    \"money\": \"%s\",\n" +
                "    \"demander\": %d,\n"+
                "    \"is_delete\": false,\n"+
                "}",demand_id,type,title,content,time,money,demander);
        return JsonDataUtil.putJSONObject(JsonDataUtil.RESOURCE_URL+"demand/"+demand_id+"/", params);
    }
    /**
     * 删除需求信息
     * @return 是否成功
     */
    public static boolean deleteDemand(int demand_id){
        return JsonDataUtil.deleteJSONObject(JsonDataUtil.RESOURCE_URL+"demand/"+demand_id+"/");
    }
}
