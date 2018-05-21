package com.example.lenovo.geographysharing.EntityClass;

/**
 * Created by Lisa on 2018/4/21.
 */

public class orderItem {
    private String shop_name;//商店名
    private String good_status;//商品状态
    private int image_id;//商品图片
    private String good_name;//商品地址
    private String good_price;//商品价格
    private String good_num; //数量
    private String total_price;//总价

    //构造函数，注意传参
    public orderItem(String shop_name, String good_status, int image_id, String good_name, String good_price, String good_num, String total_price) {
        this.shop_name = shop_name;
        this.good_status = good_status;
        this.good_name = good_name;
        this.good_price = good_price;
        this.good_num = good_num;
        this.total_price = total_price;
        this.image_id = image_id;
    }

    //设置商店名称
    public void setshopName(String name) {
        shop_name = name;
    }

    //设置商品状态
    public void setgoodStatus(String status) {
        good_status = status;
    }

    //设置商品名称
    public void setgoodName(String goodname) {
        good_name = goodname;
    }

    //设置商品单价
    public void setgoodPrice(String price) {
        good_price = price;
    }

    //设置商品数量
    public void setNum(String num) {
        good_num = num;
    }

    //设置商品总价
    public void settotalPrice(String totalPrice) {
        total_price = totalPrice;
    }

    //设置设备的图片
    public void setImageid(int imageid) {
        image_id = imageid;
    }

    //获取商店名
    public String getshopName() {
        return shop_name;
    }

    public String getgoodStatus() {
        return good_status;
    }

    public String getgoodName() {
        return good_name;
    }

    public String getgoodPrice() {
        return good_price;
    }

    public String getgoodNum() {
        return good_num;
    }
    public String gettotalPrice() {
        return total_price;
    }
    public int getImageId() {
        return image_id;
    }
}
