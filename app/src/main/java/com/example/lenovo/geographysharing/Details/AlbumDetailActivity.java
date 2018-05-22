package com.example.lenovo.geographysharing.Details;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.geographysharing.BaseClass.BaseActivity;
import com.example.lenovo.geographysharing.Element.Equipment;
import com.example.lenovo.geographysharing.Element.EquipmentCollection;
import com.example.lenovo.geographysharing.Pay.OrderSubmitActivity;
import com.example.lenovo.geographysharing.R;
import com.example.lenovo.geographysharing.Utils.LoginUserRegisterUtil;
import com.example.lenovo.geographysharing.Utils.TimeUtil;
import com.hejunlin.superindicatorlibray.CircleIndicator;
import com.hejunlin.superindicatorlibray.LoopViewPager;

import org.json.JSONException;
import org.w3c.dom.Text;

import java.util.Calendar;

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
    private TextView tvBuyNow = null;
    //收藏按钮和支付按钮


    private static final String EQUIPMENT = "equipment";
    private static String ISFAV = "isfav";
    private Equipment equipment;
    boolean isFav = false;
    boolean threadIsFav =false;


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
        tvBuyNow = (TextView) findViewById(R.id.tv_buy_now);


        Intent intent = this.getIntent();
        if (intent != null) {
//            lunbotuCount = intent.getIntExtra(EQUIPMENT_SIZE, 0);
//            equipID = intent.getIntExtra(EQUIPMENT_ID,0);
            equipment = (Equipment) intent.getSerializableExtra(EQUIPMENT);
        }



        //请求网络，判断商品是否被当前用户收藏
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                isFav =  EquipmentCollection.userIsCollect(LoginUserRegisterUtil.user.getPhone(), equipment.getId());
        }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (isFav) {

            tvIsFav.setText("已收藏");
        } else {
            tvIsFav.setText("未收藏");
        }

        //轮播图以及小圆点
        LoopViewPager viewPager = bindViewId(R.id.details_viewpager);
        CircleIndicator circleIndicator = bindViewId(R.id.details_indicator);

        viewPager.setAdapter(new DetailsPicAdapter(this, equipment));

        viewPager.setLooperPic(false);//设置是否自动轮播
        circleIndicator.setViewPager(viewPager);
//        Equipment equipment = new Equipment();
//        String titleName = equipment.getEquipName();
        setSupportActionBar();//表示当前页面支持ActionBar
        setSupportArrowActionBar(true);
        setTitle("详情页面");


        tvBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AlbumDetailActivity.this, OrderSubmitActivity.class);
                startActivity(intent);
            }
        });

        tvIsFav.setOnClickListener(new View.OnClickListener() {
            Calendar now = Calendar.getInstance();
            final String nowDate = now.get(Calendar.YEAR) + "年" + (now.get(Calendar.MONTH) + 1) + "月" + now.get(Calendar.DAY_OF_MONTH) + "日";
            int equipmentCollectionId;

            @Override
            public void onClick(View view) {
                final Handler handler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        if (threadIsFav){
                            tvIsFav.setText("已收藏");
                        }else {
                            tvIsFav.setText("未收藏");
                        }
                    }
                };


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (EquipmentCollection.userIsCollect(LoginUserRegisterUtil.user.getPhone(), equipment.getId())) {
                            Thread thread0 = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    equipmentCollectionId = EquipmentCollection.findEquipmentCollection(LoginUserRegisterUtil.user.getPhone(), equipment.getId()).getId();
                                }
                            });
                            thread0.start();
                            try {
                                thread0.join();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            Thread thread1 = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    EquipmentCollection.cancelCollection(equipmentCollectionId);
                                }
                            });
                            thread1.start();
                            try {
                                thread1.join();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            threadIsFav = false;
//                            tvIsFav.setText("未收藏");
                        } else {
                            Thread thread2 = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    EquipmentCollection.addEquipmentCollection(TimeUtil.checkTime(nowDate), LoginUserRegisterUtil.user.getId(), equipment.getId());
//                                    tvIsFav.setText("已收藏");
                                    threadIsFav = true;
                                }
                            });
                            thread2.start();
                            try {
                                thread2.join();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }

                        Message m = handler.obtainMessage();
                        handler.sendMessage(m);
                    }
                }).start();
            }

        });


    }

    @Override
    protected void initData() {
        textName.setText(equipment.getName());
        textPrice.setText(equipment.getPrice().toString());
        textValue.setText(equipment.getExpense().toString());
        textBuyTime.setText(equipment.getDate().substring(0, 10));
        textExpireTime.setText(equipment.getOver().toString().substring(0, 10));
        textFreeTime.setText(equipment.getStart().toString().substring(0, 10) + " —— " + equipment.getEnd().toString().substring(0, 10));
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

    public static void launchAlbumDetailActivity(Activity activity, Equipment equipment, int isfav) {
        Intent intent = new Intent(activity, AlbumDetailActivity.class);
        //防止多次实例调用
        /**
         * 传一个对象
         */
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        Bundle bundle = new Bundle();
        bundle.putSerializable(EQUIPMENT, equipment);
        intent.putExtras(bundle);
        intent.putExtra("ISFAV", isfav);
        activity.startActivity(intent);
    }


}
