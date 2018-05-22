package com.example.lenovo.geographysharing.Element;

import com.example.lenovo.geographysharing.Element.BaseElement.Thing;
import com.example.lenovo.geographysharing.Element.BaseElement.Type;
import com.example.lenovo.geographysharing.Utils.JsonDataUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/20.
 */

public class Equipment extends Thing implements Serializable {
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOver() {
        return over;
    }

    public void setOver(String over) {
        this.over = over;
    }

    public Double getExpense() {
        return expense;
    }

    public void setExpense(Double expense) {
        this.expense = expense;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Image getPicture() {
        return picture;
    }

    public void setPicture(Image picture) {
        this.picture = picture;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    private Integer id;
    private Type type;
    private User owner;
    private String name;
    private String date;
    private String over;
    private Double expense;
    private String start;
    private String end;
    private Double price;
    private Image picture;
    private String parameter;
    private String place;
    private String comment;

    private Equipment() {
        super();
    }

    private Equipment(Integer id, Type type, User owner, String name, String date, String over, Double expense, String start, String end, Double price, Image picture, String parameter, String place, String comment) {
        this.id = id;
        this.type = type;
        this.owner = owner;
        this.name = name;
        this.date = date;
        this.over = over;
        this.expense = expense;
        this.start = start;
        this.end = end;
        this.price = price;
        this.picture = picture;
        this.parameter = parameter;
        this.place = place;
        this.comment = comment;
    }

    /**
     * 解析JSONArray对象
     * @param array 设备JSONArray对象
     * @return 设备对象集合
     */
    private static List<Equipment> getEquipmentFromJSONArray(JSONArray array){
        List<Equipment> list=new ArrayList<Equipment>();
        if(array.length()<=0) {
            return null;
        }else {
            for (int i=0;i<array.length();i++) {
                try {
                    JSONObject json =array.getJSONObject(i);
                    //id, type, owner, name, date, over, expense, start, end, price, picture, parameter, place, comment
                    list.add(new Equipment(
                            /**
                             this.id = id;
                             this.type = type;
                             this.owner = owner;
                             this.name = name;
                             this.date = date;
                             this.over = over;
                             this.expense = expense;
                             this.start = start;
                             this.end = end;
                             this.price = price;
                             this.picture = picture;
                             this.parameter = parameter;
                             this.place = place;
                             this.comment = comment;
                             */
                            json.getInt("equip_id"),
                            EquipmentType.findEquipmentType(json.getJSONObject("equip_type")),
                            User.findUser(json.getJSONObject("equip_owner")),
                            json.getString("equip_name"),
                            json.getString("produce_date"),
                            json.getString("equip_over"),
                            json.getDouble("equip_expense"),
                            json.getString("equip_start"),
                            json.getString("equip_end"),
                            json.getDouble("equip_price"),
                            Image.getImages(json.getJSONObject("equip_picture")),
                            json.getString("equip_parameter"),
                            json.getString("equip_place"),
                            json.getString("equip_comment")
                    ));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return list;
        }
    }

    /**
     * 按页码查找
     * @param page 页码
     * @return 设备集合对象
     */
    public static List<Equipment> findEquipments(int page){
        JSONArray array= JsonDataUtil.getJSONArray(String.format(JsonDataUtil.RESOURCE_URL+"equipments/?format=json&page=%d",page));
        return getEquipmentFromJSONArray(array);
    }

    /**
     * 关键字查找
     * @param keyWord 关键字
     * @param page 页码
     * @return 设备对象集合
     */
    public static List<Equipment> findEquipments(String keyWord, int page){
        JSONArray array= JsonDataUtil.getJSONArray(String.format(JsonDataUtil.RESOURCE_URL+"equipments/?search=%s&page=%d",keyWord,page));
        return getEquipmentFromJSONArray(array);
    }

    /**
     * 所属用户查找
     * @param ownerPhone 所属用户电话
     * @param page 页码
     * @return 设备集合对象
     */
    public static List<Equipment> findUserEquipments(String ownerPhone , int page){
        JSONArray array= JsonDataUtil.getJSONArray(String.format(JsonDataUtil.RESOURCE_URL+"equipments/?phone=%s&page=%d",ownerPhone,page));
        return getEquipmentFromJSONArray(array);
    }

    /**
     * 获取设备对象
     * @param json JSONObject对象
     * @return 设备对象
     */
    public static Equipment findEquipment(JSONObject json){
        Equipment equipment=null;
        try {
            equipment=new Equipment(
                    json.getInt("equip_id"),
                    EquipmentType.findEquipmentType(json.getJSONObject("equip_type")),
                    User.findUser(json.getJSONObject("equip_owner")),
                    json.getString("equip_name"),
                    json.getString("produce_date"),
                    json.getString("equip_over"),
                    json.getDouble("equip_expense"),
                    json.getString("equip_start"),
                    json.getString("equip_end"),
                    json.getDouble("equip_price"),
                    Image.getImages(json.getJSONObject("equip_picture")),
                    json.getString("equip_parameter"),
                    json.getString("equip_place"),
                    json.getString("equip_comment")
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return equipment;
    }
    /**
     * 通过id获取设备对象
     * @param id 设备id
     * @return 设备对象
     */
    public static Equipment findEquipment(int id){
        JSONObject json=JsonDataUtil.getJSONObject(JsonDataUtil.RESOURCE_URL+"equipments/?format=json&equip_id="+id);
        return findEquipment(json);
    }

    /**
     * 发布设备
     * @param equip_name 设备名称
     * @param produce_date 生产日期
     * @param equip_over 有效期
     * @param equip_expense 设备价值
     * @param equip_start 空闲开始日期
     * @param equip_end 空闲结束日期
     * @param equip_price 设备价格
     * @param equip_parameter 设备参数
     * @param equip_place 设备所在地点
     * @param equip_comment 设备备注
     * @param equip_type 设备类型
     * @param equip_owner 设备发布者
     * @param equip_picture 设备图片
     * @return 设备添加结果
     */
    public static boolean addEquipment(String equip_name, String produce_date, String equip_over,
                                       Double equip_expense,String equip_start,String equip_end,Double equip_price,String equip_parameter,String equip_place,
                                       String equip_comment,Integer equip_type,Integer equip_owner,Integer equip_picture) {
        String params ;
        params=String.format("{\n" +
                "   \"equip_name\": \"%s\",\n" +
                "    \"is_checked\": false,\n" +
                "    \"produce_date\": \"%s\",\n" +
                "    \"equip_over\": \"%s\",\n" +
                "    \"equip_expense\": %s,\n" +
                "    \"equip_start\": \"%s\",\n" +
                "    \"equip_end\": \"%s\",\n" +
                "    \"equip_price\": \"%s\",\n" +
                "    \"equip_parameter\": \"%s\",\n" +
                "    \"equip_place\": \"%s\",\n" +
                "    \"equip_comment\": \"%s\",\n" +
                "    \"equip_type\": %d,\n" +
                "    \"equip_owner\": %d,\n" +
                "    \"equip_picture\": %d\n" +
                "}",equip_name,produce_date,equip_over,equip_expense.toString(),equip_start,equip_end,equip_price.toString(),equip_parameter,equip_place,equip_comment,equip_type,equip_owner,equip_picture);
        return JsonDataUtil.postJSONObject(JsonDataUtil.RESOURCE_URL+"equipments/", params);
    }
    /**
     * 修改设备信息
     * @param equip_name 设备名称
     * @param produce_date 生产日期
     * @param equip_over 设备有效期
     * @param equip_expense 设备价值
     * @param equip_start 空闲开始日期
     * @param equip_end 空闲结束日期
     * @param equip_price 设备价格
     * @param equip_parameter 设备参数
     * @param equip_place 设备所在地点
     * @param equip_comment 设备备注
     * @param equip_type 设备类型
     * @param equip_owner 设备发布者
     * @param equip_picture 设备图片
     * @return 设备添加结果
     */
    public static boolean updateEquipment(int equip_id,String equip_name, String produce_date, String equip_over,
                                       Double equip_expense,String equip_start,String equip_end,Double equip_price,String equip_parameter,String equip_place,
                                       String equip_comment,Integer equip_type,Integer equip_owner,Integer equip_picture){
        String params ;
        Equipment equipment=Equipment.findEquipment(equip_id);
        if (equipment==null)
            return false;
        params=String.format("{\n" +
                "   \"equip_name\": \"%s\",\n" +
                "    \"produce_date\": \"%s\",\n" +
                "    \"equip_over\": \"%s\",\n" +
                "    \"equip_expense\": %s,\n" +
                "    \"equip_start\": \"%s\",\n" +
                "    \"equip_end\": \"%s\",\n" +
                "    \"equip_price\": \"%s\",\n" +
                "    \"equip_parameter\": \"%s\",\n" +
                "    \"equip_place\": \"%s\",\n" +
                "    \"equip_comment\": \"%s\",\n" +
                "    \"equip_type\": %d,\n" +
                "    \"equip_owner\": %d,\n" +
                "    \"equip_picture\": %d\n" +
                "}",equip_name,produce_date,equip_over,equip_expense.toString(),equip_start,equip_end,equip_price.toString(),equip_parameter,equip_place,equip_comment,equip_type,equip_owner,equip_picture);
        return JsonDataUtil.putJSONObject(JsonDataUtil.RESOURCE_URL+"equipments/"+equip_id+"/", params);
    }

    public int getImageCount()
    {
        return picture.getSub_image().size();

    }

}
