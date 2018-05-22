package com.example.lenovo.geographysharing.Element;

/**
 * Created by Administrator on 2018/4/19.
 */

import android.graphics.Bitmap;

import com.example.lenovo.geographysharing.Utils.JsonDataUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;


/**
 * 用户类
 * 用于将数据库模型转化为实体类的封装
 */
public class User implements Serializable {
    private Integer id;
    private String nick_name;
    private String password;
    private String phone;
    private boolean complete;
    private String user_name;
    private String identity;
    private String address;
    private String image;
    private String mobile;
    private String email;
    private  String alipay;
    private String qq;
    private String wechat;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImageURL() {
        return image;
    }


    public void setImageURL(String image) {
        this.image = image;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAlipay() {
        return alipay;
    }

    public void setAlipay(String alipay) {
        this.alipay = alipay;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    private User(Integer id, String nick_name, String password, String phone, boolean complete,
                String user_name, String identity, String address, final String image, String mobile,
                String email, String alipay, String qq, String wechat) throws InterruptedException {
        this.id = id;
        this.nick_name = nick_name;
        this.password = password;
        this.phone = phone;
        this.complete = complete;
        this.user_name = user_name;
        this.identity = identity;
        this.address = address;
        this.image = image;
        this.mobile = mobile;
        this.email = email;
        this.alipay = alipay;
        this.qq = qq;
        this.wechat = wechat;
    }

    /**
     * 按用户手机号查找用户信息
     * @param phone 用户手机号码
     * @return 用户对象信息
     */
    public static User findUser(String phone) {
        JSONObject json = JsonDataUtil.getJSONObject(JsonDataUtil.RESOURCE_URL+"users/?phone=" + phone);
        return findUser(json);
    }

    /**
     * 按JSONObject获取用户信息
     * @param json JSONObject对象
     * @return 用户对象信息
     */
    public static User findUser(JSONObject json) {
        if (json == null)
            return null;
        try {
            return new User(
                    json.getInt("id"),
                    json.getString("nick_name"),
                    json.getString("password"),
                    json.getString("phone"),
                    json.getBoolean("complete"),
                    json.getString("user_name"),
                    json.getString("identity"),
                    json.getString("address"),
                    json.getString("image"),
                    json.getString("mobile"),
                    json.getString("email"),
                    json.getString("alipay"),
                    json.getString("qq"),
                    json.getString("wechat")
            );
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 注册用户
     * @param userName 昵称
     * @param password 密码
     * @param mobile 电话号码
     * @return 注册是否成功
     */
    public static boolean register(String userName, String password, String mobile) {
        String params = String.format("{\n" +
                        "           \"nick_name\": \"%s\",\n" +
                        "            \"password\": \"%s\",\n" +
                        "            \"phone\": \"%s\",\n" +
                        "            \"complete\": false,\n" +
                        "            \"user_name\": \"\",\n" +
                        "            \"identity\": \"\",\n" +
                        "            \"address\": \"\",\n" +
                        "            \"image\": null,\n" +
                        "            \"mobile\": \"\",\n" +
                        "            \"email\": \"\",\n" +
                        "            \"alipay\": \"\",\n" +
                        "            \"qq\": \"\",\n" +
                        "            \"wechat\": \"\"\n"+
                "}", userName, password, mobile);
        return JsonDataUtil.postJSONObject(JsonDataUtil.RESOURCE_URL+"users/", params);
    }

    /**
     * 通过手机号修改密码
     * @param mobile 手机号码
     * @param password 密码
     * @return 是否成功
     */
    public static boolean changePassword(String mobile,String password){
        User user=User.findUser(mobile);
        if (user==null)
            return false;
        String params = String.format("{\n" +
                "           \"nick_name\": \"%s\",\n" +
                "            \"password\": \"%s\",\n" +
                "            \"phone\": \"%s\",\n" +
                "            \"complete\": %b,\n" +
                "            \"user_name\": \"%s\",\n" +
                "            \"identity\": \"%s\",\n" +
                "            \"address\": \"%s\",\n"  +
                "            \"mobile\": \"%s\",\n" +
                "            \"email\": \"%s\",\n" +
                "            \"alipay\": \"%s\",\n" +
                "            \"qq\": \"%s\",\n" +
                "            \"wechat\": \"%s\""+
                "}", user.nick_name,password, mobile,user.complete,user.user_name,
                user.identity,user.address,user.mobile,
                user.email,user.alipay,user.qq,user.wechat);
        return JsonDataUtil.putJSONObject(JsonDataUtil.RESOURCE_URL+"users/"+mobile+"/", params);
    }

    /**
     * 用户认证
     * @param mobile 手机号
     * @param user_name 用户名
     * @param identity 身份证
     * @return 是否成功
     */
    public static boolean completeUserInfo(String mobile,String user_name,String identity){
        User user=User.findUser(mobile);
        if (user==null)
            return false;
        String params = String.format("{\n" +
                        "           \"nick_name\": \"%s\",\n" +
                        "            \"password\": \"%s\",\n" +
                        "            \"phone\": \"%s\",\n" +
                        "            \"complete\": %b,\n" +
                        "            \"user_name\": \"%s\",\n" +
                        "            \"identity\": \"%s\",\n" +
                        "            \"address\": \"%s\",\n"  +
                        "            \"mobile\": \"%s\",\n" +
                        "            \"email\": \"%s\",\n" +
                        "            \"alipay\": \"%s\",\n" +
                        "            \"qq\": \"%s\",\n" +
                        "            \"wechat\": \"%s\""+
                        "}", user.nick_name,user.password, mobile,user.complete,user_name,
                identity,user.address,user.mobile,
                user.email,user.alipay,user.qq,user.wechat);
        return JsonDataUtil.putJSONObject(JsonDataUtil.RESOURCE_URL+"users/"+mobile+"/", params);
    }

    /**
     * 修改可选信息
     * @param mobile 手机号(用来指定用户)
     * @param address 地址
     * @param link_mobile 联系电话
     * @param email 邮箱
     * @param alipay 阿里云账号
     * @param qq QQ号码
     * @param wechat 微信
     * @return 是否添加成功
     */
    public static boolean updateSelectUserInfo(String mobile,String address,String link_mobile,String email,String alipay,String qq,String wechat){
        User user=User.findUser(mobile);
        if (user==null)
            return false;
        String params = String.format("{\n" +
                        "           \"nick_name\": \"%s\",\n" +
                        "            \"password\": \"%s\",\n" +
                        "            \"phone\": \"%s\",\n" +
                        "            \"complete\": %b,\n" +
                        "            \"user_name\": \"%s\",\n" +
                        "            \"identity\": \"%s\",\n" +
                        "            \"address\": \"%s\",\n"  +
                        "            \"mobile\": \"%s\",\n" +
                        "            \"email\": \"%s\",\n" +
                        "            \"alipay\": \"%s\",\n" +
                        "            \"qq\": \"%s\",\n" +
                        "            \"wechat\": \"%s\""+
                        "}", user.nick_name,user.password, mobile,user.complete,user.user_name,
                user.identity,address,mobile,
                email,alipay,qq,wechat);
        return JsonDataUtil.putJSONObject(JsonDataUtil.RESOURCE_URL+"users/"+mobile+"/", params);
    }
}