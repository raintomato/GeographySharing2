package com.example.lenovo.geographysharing.Element;

/**
 * Created by Administrator on 2018/4/19.
 */

import android.util.Log;

import com.example.lenovo.geographysharing.Utils.HttpConnectionUtil;
import com.example.lenovo.geographysharing.Utils.JsonDataUtil;
import com.example.lenovo.geographysharing.Utils.LoginUserRegisterUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * 用户类
 * 用于将数据库模型转化为实体类的封装
 */
public class User implements Serializable {
    private Integer id;
    private String nick_name;
    private String password;
    private String last_login;
    private String phone;
    private boolean complete;
    private String user_name;
    private String identity;
    private String address;
    private String image;
    private String mobile;
    private String email;
    private String alipay;
    private String qq;
    private String wechat;



    public String getDate_joined() {
        return date_joined;
    }

    public void setDate_joined(String date_joined) {
        this.date_joined = date_joined;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    private String date_joined;




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

    public String getLast_login() {
        return last_login;
    }

    public void setLast_login(String last_login) {
        this.last_login = last_login;
    }

    private User(Integer id, String nick_name, String password, String phone, boolean complete,
                 String user_name, String identity, String address, final String image, String mobile,
                 String email, String alipay, String qq, String wechat
                 ) throws InterruptedException {
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
     * 按用户ID查找用户信息
     * @param id 用户ID
     * @return 用户对象信息
     */
    @Deprecated
    public static User findUser(int id) {
        JSONObject json = JsonDataUtil.getJSONObject(JsonDataUtil.RESOURCE_URL+"users/?id=" + id);
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
                "           \"username\": \"%s\",\n" +
                "           \"nick_name\": \"%s\",\n" +
                "            \"password\": \"%s\",\n" +
                "            \"phone\": \"%s\",\n" +
                "            \"complete\": false,\n" +
                "            \"user_name\": \"未填写\",\n" +
                "            \"identity\": \"未填写\",\n" +
                "            \"address\": \"未填写\",\n" +
                "            \"image\": null,\n" +
                "            \"mobile\": \"未填写\",\n" +
                "            \"email\": \"未填写\",\n" +
                "            \"alipay\": \"未填写\",\n" +
                "            \"qq\": \"未填写\",\n" +
                "            \"wechat\": \"未填写\"\n"+
                "}", mobile,userName, password, mobile);
        Log.i("register:", params);
        return JsonDataUtil.postJSONObject(JsonDataUtil.RESOURCE_URL+"users/", params);
    }

    /**
     * 通过手机号修改密码
     * @param mobile 手机号码
     * @param password 密码
     * @return 是否成功
     */
    public static boolean changePassword(String mobile, String password){
        User user=User.findUser(mobile);
        if (user==null)
            return false;
        String params = String.format("{\n" +
                        "           \"username\": \"%s\","+
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
                        "            \"wechat\": \"%s\","+
                        "            \"image\":null"+
                        "}",mobile, user.nick_name,password, mobile,user.complete,user.user_name,
                user.identity,user.address,user.mobile,
                user.email,user.alipay,user.qq,user.wechat);
        Log.i("ss", "changePassword: +"+params);
        return JsonDataUtil.putJSONObject(JsonDataUtil.RESOURCE_URL+"users/"+mobile+"/", params);
    }

    private static boolean checkUser(String username,String password){
        String params;
        params=String.format("{\"username\":\"%s\",\"password\":\"%s\"}",username,password);
        return JsonDataUtil.postJSONObject(JsonDataUtil.RESOURCE_URL+"api/auth/",params);
    }
    /**
     * 用户认证
     * @param mobile 手机号
     * @param user_name 用户名
     * @param identity 身份证
     * @return 是否成功
     */
    public static boolean completeUserInfo(String mobile,String password, String user_name, String identity,boolean isSuccess){
        Log.i("e33", "completeUserInfo: "+"我用到了");
        User user=User.findUser(mobile);
        if(!isSuccess){
            return false;
        }
        if (user==null) {
            return false;
        }
        if(!checkUser(mobile,password)){

            return false;
        }
        String params = String.format("{\n" +
                        "           \"username\": \"%s\","+
                        "           \"nick_name\": \"%s\",\n" +
                        "            \"password\": \"%s\",\n" +
                        "            \"phone\": \"%s\",\n" +
                        "            \"complete\": true,\n" +
                        "            \"user_name\": \"%s\",\n" +
                        "            \"identity\": \"%s\",\n" +
                        "            \"address\": \"%s\",\n"  +
                        "            \"mobile\": \"%s\",\n" +
                        "            \"email\": \"%s\",\n" +
                        "            \"alipay\": \"%s\",\n" +
                        "            \"qq\": \"%s\",\n" +
                        "            \"wechat\": \"%s\""+
                        "}",mobile, user.nick_name,password, mobile,user_name,
                identity,user.address,user.mobile,
                user.email,user.alipay,user.qq,user.wechat);
        LoginUserRegisterUtil.user.setComplete(true);
        Log.i("23232"+"completeUserInfo:", params);
        return JsonDataUtil.putJSONObject(JsonDataUtil.RESOURCE_URL+"users/"+mobile+"/", params);
    }

//    /**
//     * 修改可选信息
//     * @param mobile 手机号(用来指定用户)
//     * @param address 地址
//     * @param link_mobile 联系电话
//     * @param email 邮箱
//     * @param alipay 阿里云账号
//     * @param qq QQ号码
//     * @param wechat 微信
//     * @return 是否添加成功
//     */
//    public static boolean updateSelectUserInfo(String mobile, String address, String link_mobile, String email, String alipay, String qq, String wechat,String password){
//        User user=User.findUser(mobile);
//
//        if (user==null) {
//            return false;
//        }
//        if(!checkUser(mobile,password)){
//
//            return false;
//        }
//        if (user==null)
//            return false;
//        String params = String.format("{\n" +
//                "\"username\": \"%s\","+
//                        "           \"nick_name\": \"%s\",\n" +
//                        "            \"password\": \"%s\",\n" +
//                        "            \"phone\": \"%s\",\n" +
//                        "            \"complete\": %b,\n" +
//                        "            \"user_name\": \"%s\",\n" +
//                        "            \"identity\": \"%s\",\n" +
//                        "            \"address\": \"%s\",\n"  +
//                        "            \"mobile\": \"%s\",\n" +
//                        "            \"email\": \"%s\",\n" +
//                        "            \"alipay\": \"%s\",\n" +
//                        "            \"qq\": \"%s\",\n" +
//                        "            \"wechat\": \"%s\""+
//                        "}", mobile,user.nick_name,user.password, mobile,user.complete,user.user_name,
//                user.identity,address,link_mobile,
//                email,alipay,qq,wechat);
//        Log.i("updateSelectUserInfo:", params);
//        return JsonDataUtil.putJSONObject(JsonDataUtil.RESOURCE_URL+"users/"+mobile+"/", params);
//    }
    /**
     * 修改可选信息
     * @param mobile 手机号(用来指定用户)
     * @param address 地址
     * @param link_mobile 联系电话
     * @param email 邮箱
     * @param nick_name 昵称
     * @param qq QQ号码
     * @param alipay 支付宝
     * @return 是否添加成功
     */
    public static boolean updateSelectUserInfo(String mobile, String address, String link_mobile, String email, String nick_name, String qq, String alipay,String password,String path){
        User user=User.findUser(mobile);

        if (user==null) {
            return false;
        }
//        if(!checkUser(mobile,password)){
//            return false;
//        }
        if (user==null)
            return false;

        Map<String, Object> paramMap = new HashMap<String, Object>();
//        paramMap.put("password", user.password);
//        paramMap.put("username", mobile);
//        paramMap.put("nick_name", user.nick_name);
//        paramMap.put("phone", mobile);
//        paramMap.put("complete", user.complete);
//        paramMap.put("user_name", user.user_name);
//        paramMap.put("identity", user.identity);
//        paramMap.put("address", address);
//        paramMap.put("mobile", link_mobile);
//        paramMap.put("email", email);
//        paramMap.put("qq", qq);
//        paramMap.put("wechat",wechat);
//        paramMap.put("alipay", alipay);
//        paramMap.put("add_time","2018-01-01T1:1:1");

        paramMap.put("username", user.phone);
        paramMap.put("password", password);
        paramMap.put("nick_name", nick_name);
        paramMap.put("user_name", user.user_name);
        paramMap.put("identity", user.identity);
        paramMap.put("address", address);
        paramMap.put("mobile", link_mobile);
        paramMap.put("email", email);
        paramMap.put("alipay", alipay);
        paramMap.put("qq", qq);
        paramMap.put("complete",user.complete);
        paramMap.put("wechat", user.wechat);
        if(user.isComplete()) {
            paramMap.put("complete", "on");
        }
        File pictureFile = new File(path);
        Log.i("sss", "updateSelectUserInfo: "+user.complete);
        boolean success=false;
        try {
            HttpConnectionUtil.doPutPicture(JsonDataUtil.RESOURCE_URL+"users/"+user.getPhone()+"/", paramMap, pictureFile);
            success=true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }


    /**
     * 获取登录验证
     * @param username 用户名
     * @param password 密码
     * @return 验证结果
     */
    private static String getToken(String username,String password){
        String params;
        params=String.format("{\"username\":\"%s\",\"password\":\"%s\"}",username,password);
        return JsonDataUtil.postOrderJSONObjectReturnString(JsonDataUtil.RESOURCE_URL+"api/auth/",params,"token");
    }

    /**
     * 登录
     * @param username 用户名
     * @param password 密码
     * @return 登录结果
     */
    public static boolean login(String username,String password){
        String token=getToken(username,password);
        if(token!=null){
            LoginUserRegisterUtil.TOKEN="JWT "+token;
            LoginUserRegisterUtil.user=findUser(username);
            LoginUserRegisterUtil.loginDate=System.currentTimeMillis();
            LoginUserRegisterUtil.IS_LOGIN=true;
            //在此处加缓存
            return true;
        }
        return false;
    }
}