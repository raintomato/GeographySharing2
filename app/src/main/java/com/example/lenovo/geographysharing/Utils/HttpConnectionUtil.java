package com.example.lenovo.geographysharing.Utils;

/**
 * Created by Administrator on 2018/6/20.
 */
import android.util.Log;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import static com.example.lenovo.geographysharing.Utils.JsonDataUtil.dealResponseResult;

public class HttpConnectionUtil {

    static String BOUNDARY = java.util.UUID.randomUUID().toString();
    static String PREFIX = "--", LINEND = "\r\n";
    static String MULTIPART_FROM_DATA = "multipart/form-data";
    static String CHARSET = "UTF-8";

    /**
     * 上传图片
     * @param urlStr 链接地址
     * @param paramMap 数据
     * @param pictureFile 图片路径
     * @return
     * @throws Exception
     */
    public static int doPostPicture(String urlStr, Map<String,Object> paramMap, File pictureFile )
            throws Exception {
        int isOK=-1;
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setDoInput(true);// 允许输入
        conn.setDoOutput(true);// 允许输出
        conn.setUseCaches(false);
        conn.setReadTimeout(10 * 1000); // 缓存的最长时间
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization",LoginUserRegisterUtil.TOKEN);
        conn.setRequestProperty("Charset", "UTF-8");
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA + ";boundary=" + BOUNDARY);


        DataOutputStream os =  new DataOutputStream(conn.getOutputStream());

        StringBuilder sb = new StringBuilder(); //用StringBuilder拼接报文，用于上传图片数据
        sb.append(PREFIX);
        sb.append(BOUNDARY);
        sb.append(LINEND);
        sb.append("Content-Disposition: form-data; name=\"image\"; filename=\"" + pictureFile.getName() + "\"" + LINEND);
        sb.append("Content-Type: image/jpg; charset=" + CHARSET + LINEND);
        sb.append(LINEND);
        os.write(sb.toString().getBytes());
        InputStream is = new FileInputStream(pictureFile);

        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = is.read(buffer)) != -1) {
            os.write(buffer, 0, len); //写入图片数据
        }
        is.close();
        os.write(LINEND.getBytes());

        StringBuilder text = new StringBuilder();
        for(Map.Entry<String,Object> entry : paramMap.entrySet()) { //在for循环中拼接报文，上传文本数据
            if(entry.getValue()!=null) {
                text.append("--");
                text.append(BOUNDARY);
                text.append("\r\n");
                text.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"\r\n\r\n");
                text.append(entry.getValue());
                text.append("\r\n");
            }
        }
        os.write(text.toString().getBytes("utf-8")); //写入文本数据

        // 请求结束标志
        byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
        os.write(end_data);
        os.flush();
        os.close();

        // 得到响应码
        int res = conn.getResponseCode();
        if(res==HttpURLConnection.HTTP_CREATED){
            String jsonString=dealResponseResult(conn.getInputStream());
            JSONObject jsonObject=new JSONObject(jsonString);
            isOK=jsonObject.getInt("id");
        }
        Log.i("doPostPicture", res+"/"+conn.getResponseMessage());
        conn.disconnect();
        return isOK;
    }

    /**
     * 修改图片
     * @param urlStr 链接地址
     * @param paramMap 数据
     * @param pictureFile 图片路径
     * @return
     * @throws Exception
     */
    public static int doPutPicture(String urlStr, Map<String,Object> paramMap, File pictureFile )
            throws Exception {
        int isOK=-1;
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setDoInput(true);// 允许输入
        conn.setDoOutput(true);// 允许输出
        conn.setUseCaches(true);
        conn.setReadTimeout(10 * 1000); // 缓存的最长时间
        conn.setRequestMethod("PUT");
        conn.setRequestProperty("Authorization",LoginUserRegisterUtil.TOKEN);
        conn.setRequestProperty("Charset", "UTF-8");
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA + ";boundary=" + BOUNDARY);


        DataOutputStream os =  new DataOutputStream(conn.getOutputStream());

        StringBuilder sb = new StringBuilder(); //用StringBuilder拼接报文，用于上传图片数据
        sb.append(PREFIX);
        sb.append(BOUNDARY);
        sb.append(LINEND);
        sb.append("Content-Disposition: form-data; name=\"image\"; filename=\"" + pictureFile.getName() + "\"" + LINEND);
        Log.i("", "doPutPicture: "+pictureFile.getName());
        sb.append("Content-Type: image/jpg; charset=" + CHARSET + LINEND);
        sb.append(LINEND);
        os.write(sb.toString().getBytes());
        InputStream is = new FileInputStream(pictureFile);

        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = is.read(buffer)) != -1) {
            os.write(buffer, 0, len); //写入图片数据
        }
        is.close();
        os.write(LINEND.getBytes());

        StringBuilder text = new StringBuilder();
        for(Map.Entry<String,Object> entry : paramMap.entrySet()) { //在for循环中拼接报文，上传文本数据
            if(entry.getValue()!=null) {
                text.append("--");
                text.append(BOUNDARY);
                text.append("\r\n");
                text.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"\r\n\r\n");


                text.append(entry.getValue());
                text.append("\r\n");
            }
        }
        os.write(text.toString().getBytes("utf-8")); //写入文本数据

        Log.i("doPutPicture: ",text.toString() );
        // 请求结束标志
        byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
        os.write(end_data);
        os.flush();

        // 得到响应码
        int res = conn.getResponseCode();
        if(res==HttpURLConnection.HTTP_OK){
            String jsonString=dealResponseResult(conn.getInputStream());
            JSONObject jsonObject=new JSONObject(jsonString);
            isOK=jsonObject.getInt("id");
            Log.i("doPostPicture（HTTP_OK）", res+"/"+conn.getResponseMessage());
        }
        Log.i("doPostPicture（98K）", res+"/"+conn.getResponseMessage());
        os.close();
        conn.disconnect();
        return isOK;
    }


}