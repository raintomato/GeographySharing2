package com.example.lenovo.geographysharing.Details;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.lenovo.geographysharing.BaseClass.BaseActivity;
import com.example.lenovo.geographysharing.Element.Demand;
import com.example.lenovo.geographysharing.Element.Equipment;
import com.example.lenovo.geographysharing.R;
import com.example.lenovo.geographysharing.Utils.LoginUserRegisterUtil;

public class AlbumDetailRequestActivity extends BaseActivity {
    private static final String DEMADN = "demand";
    private Demand demand;
    private TextView demandTitle = null;
    private TextView demandPrice = null;
    private TextView demandParameter = null;
    private TextView demandType = null;
//    private TextView demandAddress = null;
//    private TextView demandUserPhone = null;





    @Override
    protected int getLayoutId() {
        return R.layout.activity_album_detail_request;
    }

    @Override
    protected void initView() {

        setSupportActionBar();//表示当前页面支持ActionBar
        setSupportArrowActionBar(true);
        setTitle("详情页面");


        Intent intent = this.getIntent();
        if (intent != null) {
            demand = (Demand) intent.getSerializableExtra(DEMADN);
        }
        demandTitle = bindViewId(R.id.tv_demand_title);
        demandPrice = bindViewId(R.id.tv_demand_price);
        demandParameter = bindViewId(R.id.tv_demand_parameter);
        demandType = bindViewId(R.id.titl_type);
//        demandAddress = bindViewId(R.id.tv_demand_place);
//        demandUserPhone = bindViewId(R.id.demand_user_phone);
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

        demandTitle.setText(demand.getTitle());
        demandPrice.setText(demand.getMoney());
        demandParameter.setText(demand.getContent());
        demandType.setText(demand.getType());
//        demandAddress = bindViewId(R.id.tv_demand_place);
       // demandUserPhone.setText(LoginUserRegisterUtil.user.getMobile());


    }
    public static void launchAlbumDetailRequestActivity(Activity activity) {
        Intent intent = new Intent(activity, AlbumDetailRequestActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        activity.startActivity(intent);
    }

    public static void launchAlbumDetailActivity(Activity activity, Demand demand) {
        Intent intent = new Intent(activity, AlbumDetailRequestActivity.class);
        //防止多次实例调用
        /**
         * 传一个对象
         */
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        Bundle bundle = new Bundle();
        bundle.putSerializable(DEMADN, demand);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }


}
