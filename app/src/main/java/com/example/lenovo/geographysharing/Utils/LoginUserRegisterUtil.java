package com.example.lenovo.geographysharing.Utils;

import android.content.SharedPreferences;
import android.graphics.Bitmap;

import com.example.lenovo.geographysharing.Element.User;
import com.example.lenovo.geographysharing.Welcom.LoginActivity;

/**
 * Created by Administrator on 2018/4/23.
 */

public class LoginUserRegisterUtil {
    public static String TOKEN="";
    public static boolean IS_LOGIN=false;
    public static Long loginDate=0L;
    public static User user=null;
    public static Bitmap photo = null;
    /**
     * 上述变量需要保存在缓存中
     */
    private LoginUserRegisterUtil(){}
    static {
        if(loginDate- System.currentTimeMillis()>=3600*24*30){
            IS_LOGIN=false;
        }
    }
}
