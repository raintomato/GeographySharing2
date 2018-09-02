package com.example.lenovo.geographysharing.Details;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lenovo.geographysharing.BaseClass.BaseActivity;
import com.example.lenovo.geographysharing.Element.Demand;
import com.example.lenovo.geographysharing.Element.Software;
import com.example.lenovo.geographysharing.R;
import com.example.lenovo.geographysharing.Utils.LoginUserRegisterUtil;

/**
 * Created by shdeng on 2018-7-8.
 */

public class AlbumDetailSoftActivity  extends BaseActivity {

    private static final String SOFT = "soft";
    private Software software;
    private TextView demandTitle = null;
    private TextView demandPrice = null;
    private TextView demandParameter = null;
//    private TextView demandAddress = null;
    private TextView soft_platml = null;
//    private TextView demandUserPhone = null;
    private TextView soft_describe = null;
    ImageView soft_pic = null;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_album_detail_soft;
    }

    @Override
    protected void initView() {

        setSupportActionBar();//表示当前页面支持ActionBar
        setSupportArrowActionBar(true);
        setTitle("详情页面");

        Intent intent = this.getIntent();
        if (intent != null) {
            software = (Software) intent.getSerializableExtra(SOFT);
        }
        demandTitle = bindViewId(R.id.soft_name);//名字
        demandPrice = bindViewId(R.id.soft_price);//价格
        demandParameter = bindViewId(R.id.soft_property);//备注
//        demandAddress = bindViewId(R.id.tv_soft_place);//地点
    //    demandUserPhone = bindViewId(R.id.demand_user_phone);//任命
        soft_platml = bindViewId(R.id.soft_platml);//平台
        soft_describe = bindViewId(R.id.soft_describe);//备注
        soft_pic = bindViewId(R.id.soft_pic);
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

    @Override
    protected void initData() {
//        demandTitle = bindViewId(R.id.soft_name);//名字
//        demandPrice = bindViewId(R.id.soft_price);//价格
//        demandParameter = bindViewId(R.id.soft_property);//备注
//        demandAddress = bindViewId(R.id.tv_soft_place);//地点
//        demandUserPhone = bindViewId(R.id.demand_user_phone);//任命
//        soft_platml = bindViewId(R.id.soft_platml);//平台
//        soft_describe = bindViewId(R.id.soft_describe);//备注
        if(software!=null){
            demandTitle.setText(software.getName());
            demandPrice.setText(software.getPrice().toString());
            demandParameter.setText(software.getComment());
            // demandUserPhone.setText(software.getOwner().getPhone());
            soft_platml.setText(software.getOs());
            soft_describe.setText(software.getDescribe());
            soft_pic.setImageResource(R.drawable.sw);
        }
    }
    public static void launchAlbumDetailSoftActivity(Activity activity) {
        Intent intent = new Intent(activity, AlbumDetailRequestActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        activity.startActivity(intent);
    }

    public static void launchAlbumDetailSoftActivity(Activity activity, Software demand) {
        Intent intent = new Intent(activity, AlbumDetailSoftActivity.class);
        //防止多次实例调用
        /**
         * 传一个对象
         */
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        Bundle bundle = new Bundle();
        bundle.putSerializable(SOFT, demand);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }

}
