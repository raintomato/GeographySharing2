package com.example.lenovo.geographysharing.Element;

import com.example.lenovo.geographysharing.Utils.HttpConnectionUtil;
import com.example.lenovo.geographysharing.Utils.JsonDataUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/5/12.
 */

public class Image implements Serializable {
    private int id;
    private List<Image> sub_image;
    private int image_grade;
    private String image;
    private Integer parent_grade;



    public int getId() {
        return id;
    }

    public String getImageURL() {
        return image;
    }

    public void setImageURL(String image) {
        this.image = image;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Image> getSub_image() {
        return sub_image;
    }

    public void setSub_image(List<Image> sub_image) {
        this.sub_image = sub_image;
    }

    public int getImage_grade() {
        return image_grade;
    }

    public void setImage_grade(int image_grade) {
        this.image_grade = image_grade;
    }

    public int getParent_grade() {
        return parent_grade;
    }

    public void setParent_grade(int parent_grade) {
        this.parent_grade = parent_grade;
    }

    private Image(int id, List<Image> sub_image, int image_grade, final String image, int parent_grade) throws InterruptedException {
        this.id = id;
        this.sub_image = sub_image;
        this.image_grade = image_grade;
        this.image = image;
        this.parent_grade = parent_grade;
    }

    private Image(int parent_grade, int image_grade, final String image, int id) throws InterruptedException {
        this.parent_grade = parent_grade;
        this.image_grade = image_grade;
        this.image = image;
        this.id = id;
    }

    /**
     * 包含图片对象
     *
     * @param json JSON对象
     * @return 图片集
     */
    public static Image getImages(JSONObject json) {
        Image image = null;
        try {
            /**
             private int id;
             private Image sub_image;
             private int image_grade;
             private String image;
             private int parent_grade;
             */
            image = new Image(
                    json.getInt("id"),
                    getSubImages(json.getJSONArray("sub_image")),
                    json.getInt("image_grade"),
                    json.getString("image"),
                    json.optInt("parent_grade")
            );
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return image;
    }


    /**
     * 获得子图片集
     *
     * @param array JSONArray对象
     * @return 子图片集
     */
    private static List<Image> getSubImages(JSONArray array) {
        ArrayList<Image> list = new ArrayList<Image>();
        if (array.length() <= 0) {
            return null;
        } else {
            for (int i = 0; i < array.length(); i++) {
                try {
                    JSONObject json = array.getJSONObject(i);
                    /**
                     int parent_grade, int image_grade, final String image, int id
                     */

                    list.add(new Image(
                            json.optInt("parent_grade"),
                            json.getInt("image_grade"),
                            json.getString("image"),
                            json.getInt("id")
                    ));
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return list;
        }
    }

    /**
     * 可调用方法
     * 构造图片对象
     * @param id 图片id
     * @return 图片对象
     */
    public static Image findImages(int id) {
        JSONObject json = JsonDataUtil.getJSONObject(String.format(JsonDataUtil.RESOURCE_URL+"image/?format=json&id=%d", id),false);
        return getImages(json);
    }

    public static int uploadImage(int image_grade,Integer parent_id,String path){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("image_grade", image_grade);
        paramMap.put("parent_grade", parent_id);
        File pictureFile = new File(path);
        int success=-1;
        try {
            success=HttpConnectionUtil.doPostPicture(JsonDataUtil.RESOURCE_URL+"image/", paramMap, pictureFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }

}