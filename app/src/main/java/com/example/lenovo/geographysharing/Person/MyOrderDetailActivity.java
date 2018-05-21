package com.example.lenovo.geographysharing.Person;


import android.app.Activity;
import android.content.Intent;

import com.example.lenovo.geographysharing.BaseClass.BaseActivity;
import com.example.lenovo.geographysharing.R;

/**
 * Created by Lisa on 2018/4/22.
 */

public class MyOrderDetailActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.order_detail;
    }

    @Override
    protected void initView() {

    }
    public static void launchorderDetailActivity(Activity activity){
        Intent intent = new Intent(activity,MyOrderDetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        activity.startActivity(intent);
    }
    @Override
    protected void initData() {

    }
}
