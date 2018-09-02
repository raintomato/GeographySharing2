package com.example.lenovo.geographysharing.Person;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lenovo.geographysharing.BaseClass.BaseActivity;
import com.example.lenovo.geographysharing.Element.Equipment;
import com.example.lenovo.geographysharing.Element.Order;
import com.example.lenovo.geographysharing.R;
import com.example.lenovo.geographysharing.Utils.JsonDataUtil;
import com.example.lenovo.geographysharing.Utils.LoginUserRegisterUtil;

/**
 * Created by Lisa on 2018/4/22.
 */

public class MyOrderDetailActivity extends BaseActivity {

    private static final String ORDER = "ORDER";
    private static final String Equipment = "EQUIPMENT";
    private ImageView image_order_pic;
    private TextView product_name;
    private TextView product_value;
    private TextView product_freetime;
    private TextView status;
    private TextView order_num;
    private TextView pay_account;
    private TextView pay_date;
    private TextView pay_way;
    private TextView address;
    private TextView remark;
    private Order order;
    private Equipment equipment;
    Bitmap tempBitmap = null;

    @Override
    protected int getLayoutId() {
        return R.layout.order_detail;
    }

    @Override
    protected void initView() {

        setSupportActionBar();//表示当前页面支持ActionBar
        setSupportArrowActionBar(true);
        setTitle("详情页面");
        product_name = bindViewId(R.id.product_name);
        product_value = bindViewId(R.id.product_value);
        product_freetime = bindViewId(R.id.product_freetime);
        status = bindViewId(R.id.status);
        order_num = bindViewId(R.id.order_num);
        pay_account = bindViewId(R.id.pay_account);
        pay_date = bindViewId(R.id.pay_date);
        pay_way = bindViewId(R.id.pay_way);
        address = bindViewId(R.id.address);
        remark = bindViewId(R.id.remark);
        image_order_pic = bindViewId(R.id.image_order_pic);
        Intent intent = this.getIntent();
        if (intent != null) {
            order = (Order) intent.getSerializableExtra(ORDER);
        }
        product_name.setText(((Equipment) (order.getProduct())).getName());
        product_value.setText(order.getTotal_amount().toString() + "元");
        product_freetime.setText(((Equipment) (order.getProduct())).getStart().substring(0, 10) + " 至 " + ((Equipment) (order.getProduct())).getEnd().substring(0, 10));
        status.setText(order.getStatus() ? "已支付" : "未支付");
        order_num.setText(order.getOut_trade_no());
        pay_account.setText(LoginUserRegisterUtil.user.getAlipay());
        pay_date.setText(order.getUpdate_time().substring(0, 10) + "  " + order.getUpdate_time().substring(11));
        pay_way.setText(order.getStatus() ? "支付宝" : "未支付");
        address.setText(order.getTaxpayer_address());
        remark.setText(order.getComment());
        image_order_pic.setImageResource(R.drawable.loading);

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                image_order_pic.setImageBitmap(tempBitmap);
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    tempBitmap = JsonDataUtil.getImage(((Equipment)(order.getProduct())).getPicture().getImageURL());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Message message = handler.obtainMessage();
                handler.sendMessage(message);

            }
        }).start();


    }

    //处理左上角返回箭头
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public static void launchorderDetailActivity(Activity activity, Order order) {
        Intent intent = new Intent(activity, MyOrderDetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        Bundle bundle = new Bundle();
        bundle.putSerializable(ORDER, order);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }

    @Override
    protected void initData() {

    }
}
