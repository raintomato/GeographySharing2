package com.example.lenovo.geographysharing.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by 甄焰鑫 on 2018/4/19.
 * 此类为获取网络json数据的工具类
 */
public class JsonDataUtil {

    public static final String RESOURCE_URL = "http://59.110.172.7:8000/";

    private JsonDataUtil() {
    }

    /**
     * 获取JSON数据
     */
    private static String findJsonData(String urlString) {
        URL url;
        try {
            url = new URL(urlString);
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setDoInput(true);
            urlConn.setRequestProperty("Authorization",LoginUserRegisterUtil.TOKEN);

            InputStreamReader in = new InputStreamReader(urlConn.getInputStream());
            BufferedReader buffer = new BufferedReader(in);
            String inputLine = null;
            String result = "";
            while ((inputLine = buffer.readLine()) != null) {
                result += inputLine + "\n";
            }
            in.close();
            urlConn.disconnect();
            Log.i("JSON", "findJsonData: "+result);
            return result;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 单个查询
     *
     * @param urlString url地址字符串
     * @return 返回JSONobject对象
     */
    public static JSONObject getJSONObject(String urlString) {
        try {
            String JSONString = findJsonData(urlString);
            JSONObject jsonObject = new JSONObject(JSONString);
            if (jsonObject == null)
                return null;
            JSONArray result = new JSONArray(jsonObject.getString("results"));
            if (result.length() > 0) {
                JSONObject data = result.getJSONObject(0);
                return data;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 单个查询
     *
     * @param urlString url地址字符串
     * @return 返回JSONobject对象
     */
    public static JSONObject getJSONObject(String urlString, boolean hasPage) {
        try {
            JSONArray result = null;
            if (hasPage) {
                String JSONString = findJsonData(urlString);
                JSONObject jsonObject = new JSONObject(JSONString);
                result = new JSONArray(jsonObject.getString("results"));
                Log.i("has", "getJSONObject:True ");
            } else {
                result = new JSONArray(findJsonData(urlString));
                Log.i("has", "getJSONObject:False ");
            }
            if (result.length() > 0) {
                JSONObject data = result.getJSONObject(0);
                Log.i("JSON", data.toString());
                return data;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject getJSONObjectForSoftwore(String urlString, boolean hasPage) {
        try {
            JSONArray result = null;
            if (hasPage) {
                String JSONString = findJsonData(urlString);
                JSONObject jsonObject = new JSONObject(JSONString);
                result = new JSONArray(jsonObject.getString("results"));
                Log.i("has", "getJSONObject:True ");
            } else {
                return new JSONObject(findJsonData(urlString));
            }
            if (result.length() > 0) {
                JSONObject data = result.getJSONObject(0);
                Log.i("JSON", data.toString());
                return data;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**7-5
     * 非JSON数组型单个查询
     *
     * @param urlString url地址字符串
     * @return 返回JSONobject对象
     */
    public static JSONObject getJSONObjectNotArrayChange(String urlString) {
        try {
            return new JSONObject(findJsonData(urlString));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 多个查询
     *
     * @param urlString url地址字符串
     * @return 返回JSONobject对象
     */
    public static JSONArray getJSONArray(String urlString) {
        try {
            String JSONString = findJsonData(urlString);
            JSONObject jsonObject = new JSONObject(JSONString);
            JSONArray result = new JSONArray(jsonObject.getString("results"));
            return result;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 多个查询
     *
     * @param urlString url地址字符串
     * @param hasPage   是否分页
     * @return 返回JSONobject对象
     */
    public static JSONArray getJSONArray(String urlString, boolean hasPage) {
        try {
            JSONArray result = null;
            String JSONString = findJsonData(urlString);
            if (hasPage) {
                JSONObject jsonObject = new JSONObject(JSONString);
                result = new JSONArray(jsonObject.getString("results"));
            } else {
                result = new JSONArray(JSONString);
            }
            return result;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取网络图片
     *
     * @param url 图片URL
     * @return 相应图片的bitmap
     */
    public static Bitmap getImage(String url) {
        Bitmap bitmap = null;
        try {
            byte[] data = getImageData(url);
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length); //生成位图
        } catch (IOException e) {
        }
        if (bitmap != null)
        return CompressImageUtil.compressForInternetImage(bitmap);
        else
            return null;
    }

    /**
     * 获取图片数据
     *
     * @param path 路径
     * @return 图片二进制流
     * @throws IOException IO流异常
     */
    private static byte[] getImageData(String path) throws IOException {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");  //设置请求方法为GET
        conn.setReadTimeout(5 * 1000);  //设置请求过时时间为5秒
        InputStream inputStream = conn.getInputStream();  //通过输入流获得图片数据
        byte[] data = readInputStream(inputStream);   //获得图片的二进制数据
        return data;
    }

    /**
     * 二进制读取数据流
     *
     * @param inputStream 输入流
     * @return 二进制数据
     * @throws IOException IO刘异常
     */
    private static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();

    }
/*****************************************华丽丽的分割线*****************************************/
    /**
     * POSRT数据
     *
     * @param urlString URL地址字符串
     * @param param     JSON字符串参数
     * @return 上传是否成功
     */
    public static boolean postJSONObject(String urlString, String param) {
        String target = urlString;
        URL url;
        try {
            url = new URL(target);
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setRequestMethod("POST");
            urlConn.setDoInput(true);
            urlConn.setDoOutput(true);
            urlConn.setUseCaches(false);
            urlConn.setInstanceFollowRedirects(true);

            urlConn.setRequestProperty("Authorization",LoginUserRegisterUtil.TOKEN);
            urlConn.setRequestProperty("Content-Type", "application/json");
            OutputStreamWriter out = new OutputStreamWriter(urlConn.getOutputStream());
            out.write(param);
            out.flush();
            out.close();
            Log.i(String.valueOf(urlConn.getResponseCode() + "/" + urlConn.getResponseMessage()), "postJSONObject: ");
            if (urlConn.getResponseCode() == HttpURLConnection.HTTP_CREATED||urlConn.getResponseCode()== HttpURLConnection.HTTP_OK) {
                return true;
            }
            Log.i(String.valueOf(urlConn.getResponseCode() + "/" + urlConn.getResponseMessage()), "postJSONObject: ");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * PUT数据
     *
     * @param urlString URL地址字符串
     * @param param     JSON字符串参数
     * @return 上传是否成功
     */
    public static boolean putJSONObject(String urlString, String param) {
        String target = urlString;
        URL url;
        try {
            url = new URL(target);
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setRequestMethod("PUT");
            urlConn.setDoInput(true);
            urlConn.setDoOutput(true);
            urlConn.setUseCaches(false);
            urlConn.setUseCaches(false);
            urlConn.setInstanceFollowRedirects(true);

            urlConn.setRequestProperty("Authorization",LoginUserRegisterUtil.TOKEN);
            urlConn.setRequestProperty("Content-Type", "application/json");
            OutputStreamWriter out = new OutputStreamWriter(urlConn.getOutputStream());
            out.write(param);
            out.flush();
            out.close();
            if (urlConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                return true;
            }
            Log.i(String.valueOf(urlConn.getResponseCode() + "/" + urlConn.getResponseMessage()), "putJSONObject: ");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * DELETE数据
     *
     * @param urlString URL地址字符串
     * @return 上传是否成功
     */
    public static boolean deleteJSONObject(String urlString) {
        String target = urlString;
        URL url;
        try {
            url = new URL(target);
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setRequestMethod("DELETE");
            urlConn.setRequestProperty("Authorization",LoginUserRegisterUtil.TOKEN);
            urlConn.setDoInput(true);
            urlConn.setDoOutput(true);
            urlConn.setUseCaches(false);
            urlConn.setUseCaches(false);
            urlConn.setInstanceFollowRedirects(true);
            if (urlConn.getResponseCode() == HttpURLConnection.HTTP_NO_CONTENT) {
                return true;
            }
            Log.i(String.valueOf(urlConn.getResponseCode() + "/" + urlConn.getResponseMessage()), "deleteJSONObject: ");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 提交订单数据
     *
     * @param urlString URL地址字符串
     * @param param     JSON字符串参数
     *  @param back     返回值键名
     * @return 上传是否成功
     */
    public static String postOrderJSONObjectReturnString(String urlString, String param, String back) {
        String target = urlString;
        URL url;
        try {
            url = new URL(target);
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setRequestMethod("POST");
            urlConn.setDoInput(true);
            urlConn.setDoOutput(true);
            urlConn.setUseCaches(false);
            urlConn.setUseCaches(false);
            urlConn.setInstanceFollowRedirects(true);
            urlConn.setRequestProperty("Authorization",LoginUserRegisterUtil.TOKEN);
            urlConn.setRequestProperty("Content-Type", "application/json");
            OutputStreamWriter out = new OutputStreamWriter(urlConn.getOutputStream());
            out.write(param);
            out.flush();
            out.close();
            if (urlConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String jsonString=dealResponseResult(urlConn.getInputStream());
                JSONObject json = new JSONObject(jsonString);
                return json.getString(back);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 处理返回字符串
     * @param inputStream
     * @return
     */
    public static String dealResponseResult(InputStream inputStream) {
        String resultData = null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len = 0;
        try {
            while ((len = inputStream.read(data)) != -1) {
                byteArrayOutputStream.write(data, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        resultData = new String(byteArrayOutputStream.toByteArray());
        return resultData;
    }
}


