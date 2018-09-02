package com.example.lenovo.geographysharing.Element;

import com.example.lenovo.geographysharing.Element.BaseElement.Type;
import com.example.lenovo.geographysharing.Utils.JsonDataUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EquipmentType extends Type {

    private EquipmentType(int id, String type_name, Integer type_grade,
                          boolean is_tab, String add_time, Integer parent_grade,List<String> sub_type) {
        super(id, type_name, type_grade, is_tab, add_time, parent_grade,sub_type);
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
                    JSONArray arrays=json.getJSONArray("sub_equip");
                    List<String> sub_types=new ArrayList<>();
                    for (int k=0;k<arrays.length();k++){
                        sub_types.add(arrays.getJSONObject(k).getString("type_name"));
                    }
                    list.add(new EquipmentType(
                            json.getInt("id"),
                            json.getString("type_name"),
                            json.getInt("type_grade"),
                            json.getBoolean("is_tab"),
                            json.getString("add_time"),
                            parent,
                            sub_types
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
    private static List<EquipmentType> getSecondEquipmentTypes(String typeName) {
        JSONArray array= JsonDataUtil.getJSONArray(JsonDataUtil.RESOURCE_URL+"equipment_types/?format=json&type_name="+typeName,false);

        return getEquipmentTypeJSONArray(array);
    }

    public static String findBigTypeNameById(int id) {
        JSONObject json = JsonDataUtil.getJSONObject(JsonDataUtil.RESOURCE_URL + "equipment_types/?id=" + id);
        if (json != null) {
            try {
                return json.getString("type_name");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
            return null;
    }

    /**
     * 获取二级分类
     * @param typeName 父类名称
     * @return 设备类型对象集合
     */
    public static EquipmentType findSecondEquipmentTypes(String typeName) {
        return getSecondEquipmentTypes(typeName).get(0);
    }

    /**
     * 获取一级分类
     * @return 设备类型对象集合
     */
    public static List<EquipmentType> findFirstEquipmentTypes(){

        JSONArray array= JsonDataUtil.getJSONArray(JsonDataUtil.RESOURCE_URL+"equipment_types/?format=json&type_grade=1");
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
            JSONArray arrays=json.getJSONArray("sub_equip");
            List<String> sub_types=new ArrayList<>();
            for (int k=0;k<arrays.length();k++){
                sub_types.add(arrays.getJSONObject(k).getString("type_name"));
            }
            type=new EquipmentType(
                    json.getInt("id"),
                    json.getString("type_name"),
                    json.getInt("type_grade"),
                    json.getBoolean("is_tab"),
                    json.getString("add_time"),
                    parent,sub_types
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return type;
    }
}
