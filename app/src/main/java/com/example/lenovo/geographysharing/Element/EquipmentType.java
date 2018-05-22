package com.example.lenovo.geographysharing.Element;

import com.example.lenovo.geographysharing.Element.BaseElement.Type;
import com.example.lenovo.geographysharing.Utils.JsonDataUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/21.
 */

public class EquipmentType extends Type {
    private EquipmentType(int id, String type_name, Integer type_grade,
                          boolean is_tab, String add_time, Integer parent_grade) {
        super(id, type_name, type_grade, is_tab, add_time, parent_grade);
    }

    /**
     * 获取设备类型的JSONArray对象
     * @param array 设备JSONArray对象
     * @return 设备类型对象集合
     */
    private static List<EquipmentType> getEquipmentTypeJSONArray(JSONArray array){
        List<EquipmentType> list=new ArrayList<EquipmentType>();
        if(array.length()<=0) {
            return null;
        }else {
            for (int i=0;i<array.length();i++) {
                try {
                    JSONObject json =array.getJSONObject(i);
                    int parent=-1;
                    if(!json.get("parent_grade").toString().equals("null")) {
                        parent=Integer.parseInt(json.get("parent_grade").toString());
                    }
                    list.add(new EquipmentType(
                            json.getInt("id"),
                            json.getString("type_name"),
                            json.getInt("type_grade"),
                            json.getBoolean("is_tab"),
                            json.getString("add_time"),
                            parent
                    ));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return list;
        }
    }

    /**
     * 获取二级分类
     * @param typeName 父类名称
     * @return 设备类型对象集合
     */
    public static List<EquipmentType> findSecondEquipmentTypes(String typeName) {
        JSONArray array= JsonDataUtil.getJSONArray("http://123.207.155.75/equipment_types/?format=json&type_grade=2&parent_grade="+typeName);
        return getEquipmentTypeJSONArray(array);
    }

    /**
     * 获取一级分类
     * @return 设备类型对象集合
     */
    public static List<EquipmentType> findFirstEquipmentTypes(){
        JSONArray array= JsonDataUtil.getJSONArray("http://123.207.155.75/equipment_types/?format=json&type_grade=1");
        return getEquipmentTypeJSONArray(array);
    }

    /**
     * 获取JOSNObject设备类型
     * @param json JSONObject对象
     * @return 设备类型对象
     */
    public static EquipmentType findEquipmentType(JSONObject json){
        EquipmentType type=null;
        try {
            int parent=-1;
            if(!json.get("parent_grade").toString().equals("null")) {
                parent=Integer.parseInt(json.get("parent_grade").toString());
            }
            type=new EquipmentType(
                    json.getInt("id"),
                    json.getString("type_name"),
                    json.getInt("type_grade"),
                    json.getBoolean("is_tab"),
                    json.getString("add_time"),
                    parent
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return type;
    }
}
