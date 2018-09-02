package com.example.lenovo.geographysharing.Details;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lenovo.geographysharing.BaseClass.BaseActivity;
import com.example.lenovo.geographysharing.Element.Software;
import com.example.lenovo.geographysharing.Element.Technology;
import com.example.lenovo.geographysharing.R;
import com.example.lenovo.geographysharing.Utils.LoginUserRegisterUtil;
import com.example.lenovo.geographysharing.Utils.ToastUitls;

/**
 * Created by shdeng on 2018-7-8.
 */

public class AlbumDetailPeopleActivity extends BaseActivity {


    private static final String TECH = "tech";
    private Technology technology;
    private TextView demandTitle = null;
    private TextView demandPrice = null;
    private TextView demandParameter = null;
    private TextView tech_describle = null;
    ImageView tech_image =null;





    @Override
    protected int getLayoutId() {
        return R.layout.activity_album_detail_peopel;
    }

    @Override
    protected void initView() {

        setSupportActionBar();//表示当前页面支持ActionBar
        setSupportArrowActionBar(true);
        setTitle("详情页面");
        Intent intent = this.getIntent();
        if (intent != null) {
            technology = (Technology) intent.getSerializableExtra(TECH);
        }
        demandTitle = bindViewId(R.id.soft_name);//名称
        demandPrice = bindViewId(R.id.soft_price);//价格
        demandParameter = bindViewId(R.id.soft_property);//备注
//        demandAddress = bindViewId(R.id.tv_demand_place);
//        demandUserPhone = bindViewId(R.id.demand_user_phone);
        tech_describle = bindViewId(R.id.soft_describe);//具体技术
        tech_image = bindViewId(R.id.tech_pic);
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

//        demandTitle = bindViewId(R.id.soft_name);//名称
//        demandPrice = bindViewId(R.id.soft_price);//价格
//        demandParameter = bindViewId(R.id.soft_property);//备注
//        demandAddress = bindViewId(R.id.tv_demand_place);
//        demandUserPhone = bindViewId(R.id.demand_user_phone);
//        tech_describle = bindViewId(R.id.soft_describe);//具体技术
        if(technology!=null){
            demandTitle.setText(technology.getName());
            demandPrice.setText(technology.getPrice().toString());
            demandParameter.setText(technology.getComment());
//        demandAddress.setText(technology.getOwner().getAddress());
//        demandUserPhone.setText(technology.getOwner().getPhone());
            tech_describle.setText(technology.getInfo());
            tech_image.setImageResource(R.drawable.techpeople);
        }else {
            ToastUitls.showShortToast(AlbumDetailPeopleActivity.this,"网络错误");
        }

    }
    public static void launchAlbumDetailSoftActivity(Activity activity) {
        Intent intent = new Intent(activity, AlbumDetailRequestActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        activity.startActivity(intent);
    }

    public static void launchAlbumDetailPeoPleActivity(Activity activity, Technology demand) {
        Intent intent = new Intent(activity, AlbumDetailPeopleActivity.class);
        //防止多次实例调用
        /**
         * 传一个对象
         */
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        Bundle bundle = new Bundle();
        bundle.putSerializable(TECH, demand);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }

}
