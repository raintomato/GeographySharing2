package com.example.lenovo.geographysharing.Element;

import com.example.lenovo.geographysharing.Utils.JsonDataUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2018/4/20.
 */

public class VerifyCode {
    private Integer id;
    private String mobile;
    private String code;
    private String add_time;

    private VerifyCode(Integer id, String mobile, String code, String add_time) {
        this.id = id;
        this.mobile = mobile;
        this.code = code;
        this.add_time = add_time;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    /**
     * 查找用户最近一条验证码信息
     * @param phone 电话号码
     * @return 验证码对象信息
     */
    public static VerifyCode findVerifyCode(String phone){
        JSONObject json= JsonDataUtil.getJSONObject("http://59.110.172.7:8000/verifyCodes/?ordering=-add_time&mobile="+phone);
        if(json==null)
            return null;
        try {
            return new VerifyCode(
                    json.getInt("id"),
                    json.getString("mobile"),
                    json.getString("code"),
                    json.getString("add_time")
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 向后台发送一条验证码
     * @param mobile 电话号码
     * @param code 验证码
     * @return 是否发送成功
     */
    public static boolean sendVerifyCode(String mobile,String code){
        if(mobile.equals(""))
            return false;
        String params=String.format("{\n" +
                "    \"code\": \"%s\",\n" +
                "    \"mobile\": \"%s\",\n" +
                "    \"add_time\": \"%s\"\n" +
                "}",code,mobile,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis())));
        return JsonDataUtil.postJSONObject("http://59.110.172.7:8000/verifyCodes/",params);
    }
}
