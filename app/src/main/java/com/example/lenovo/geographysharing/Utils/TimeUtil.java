package com.example.lenovo.geographysharing.Utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by shdeng on 2018-5-19.
 */

public class TimeUtil {
    public static String checkTime(String time){
        Log.i(time,"0");
        String result=time.replace("年","-");
        Log.i(result,"1");
        result=result.replace("月","-");
        Log.i(result,"2");
        result=result.replace("日","");
        Log.i(result,"3");
        String string=result+"T"+new SimpleDateFormat("HH:mm:ss").format(new Date(System.currentTimeMillis()));
        Log.i(string,"4");
        return string;
    }
}
