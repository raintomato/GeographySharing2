package com.example.lenovo.geographysharing.Details;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.geographysharing.BaseClass.BaseActivity;
import com.example.lenovo.geographysharing.Element.Equipment;
import com.example.lenovo.geographysharing.Element.EquipmentCollection;
import com.example.lenovo.geographysharing.Pay.OrderSubmitActivity;
import com.example.lenovo.geographysharing.R;
import com.hejunlin.superindicatorlibray.CircleIndicator;
import com.hejunlin.superindicatorlibray.LoopViewPager;

import org.w3c.dom.Text;

public class AlbumDetailActivity extends BaseActivity {

    private TextView textName = null;
    private TextView textPrice = null;
    private TextView textValue = null;
    private TextView textBuyTime = null;
    private TextView textExpireTime = null;
    private TextView textFreeTime = null;
    private TextView textProperty = null;
    private TextView textPhoneNumber = null;
    private TextView textQQ = null;
    private TextView textWeChat = null;
    private TextView textEmail = null;
    private TextView tvIsFav = null;



    private static final String EQUIPMENT = "equipment";
    private static String ISFAV = "isfav";
    private Equipment equipment;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_album_detail;
    }

    @Override
    protected void initView() {
        //展示textview的绑定11个；
        textName = bindViewId(R.id.product_name);
        textPrice = bindViewId(R.id.product_price);
        textValue = bindViewId(R.id.product_value);
        textBuyTime = bindViewId(R.id.product_buytime);
        textExpireTime = bindViewId(R.id.product_expiretime);
        textFreeTime = bindViewId(R.id.product_freetime);
        textProperty = bindViewId(R.id.product_property);
        textPhoneNumber = bindViewId(R.id.tx_user_phone_number);
        textQQ = bindViewId(R.id.tx_user_qq);
        textWeChat = bindViewId(R.id.tx_user_weChat);
        textEmail = bindViewId(R.id.tx_user_email);

        tvIsFav = bindViewId(R.id.tv_is_fav);


        Intent intent = this.getIntent();
        if (intent != null) {
//            lunbotuCount = intent.getIntExtra(EQUIPMENT_SIZE, 0);
//            equipID = intent.getIntExtra(EQUIPMENT_ID,0);
            equipment = (Equipment)intent.getSerializableExtra(EQUIPMENT);
        }

        if (intent.getIntExtra(ISFAV,0) == 1)
        {
            tvIsFav.setText("已收藏");
        }else {
            tvIsFav.setText("未收藏");
        }
        //轮播图以及小圆点
        LoopViewPager viewPager = bindViewId(R.id.details_viewpager);
        CircleIndicator circleIndicator = bindViewId(R.id.details_indicator);

        viewPager.setAdapter(new DetailsPicAdapter(this,equipment));

        viewPager.setLooperPic(false);//设置是否自动轮播
        circleIndicator.setViewPager(viewPager);
//        Equipment equipment = new Equipment();
//        String titleName = equipment.getEquipName();
        setSupportActionBar();//表示当前页面支持ActionBar
        setSupportArrowActionBar(true);
        setTitle("详情页面");
        TextView tvBuyNow = (TextView) findViewById(R.id.tv_buy_now);

        tvBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AlbumDetailActivity.this, OrderSubmitActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initData() {
        textName.setText(equipment.getName());
        textPrice.setText(equipment.getPrice().toString());
        textValue.setText(equipment.getExpense().toString());
        textBuyTime.setText(equipment.getDate().substring(0,10));
        textExpireTime.setText(equipment.getOver().toString().substring(0,10));
        textFreeTime.setText(equipment.getStart().toString().substring(0,10)+" —— "+equipment.getEnd().toString().substring(0,10));
        textProperty.setText(equipment.getParameter());
        textPhoneNumber.setText(equipment.getOwner().getPhone());
        textQQ.setText(equipment.getOwner().getQq());
        textWeChat.setText(equipment.getOwner().getWechat());
        textEmail.setText(equipment.getOwner().getEmail());

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

//    public static void launchAlbumDetailActivity(Activity activity,int imageSize, int equipID) {
//        Intent intent = new Intent(activity, AlbumDetailActivity.class);
//        //防止多次实例调用
//        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        intent.putExtra(EQUIPMENT_SIZE, imageSize);
//        intent.putExtra(EQUIPMENT_ID, equipID);
//        activity.startActivity(intent);
//    }

    public static void launchAlbumDetailActivity(Activity activity, Equipment equipment,int isfav) {
        Intent intent = new Intent(activity, AlbumDetailActivity.class);
        //防止多次实例调用
        /**
         * 传一个对象
         */
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        Bundle bundle = new Bundle();
        bundle.putSerializable(EQUIPMENT,equipment);
        intent.putExtras(bundle);
        intent.putExtra("ISFAV",isfav);
        activity.startActivity(intent);
    }



}
