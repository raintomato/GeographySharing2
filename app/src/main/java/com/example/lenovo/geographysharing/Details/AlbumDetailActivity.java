package com.example.lenovo.geographysharing.Details;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.lenovo.geographysharing.BaseClass.BaseActivity;
import com.example.lenovo.geographysharing.Element.Equipment;
import com.example.lenovo.geographysharing.Element.EquipmentCollection;
import com.example.lenovo.geographysharing.Home.HomeActivity;
import com.example.lenovo.geographysharing.Pay.OrderSubmitActivity;
import com.example.lenovo.geographysharing.Person.MyCertificateActivity;
import com.example.lenovo.geographysharing.R;
import com.example.lenovo.geographysharing.Utils.LoginUserRegisterUtil;
import com.example.lenovo.geographysharing.Utils.TimeUtil;
import com.hejunlin.superindicatorlibray.CircleIndicator;
import com.hejunlin.superindicatorlibray.LoopViewPager;

import org.json.JSONException;

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
    private TextView product_comment = null;
    //收藏按钮和支付按钮

    //7-14
    EquipmentCollection equipmentCollection;

    private static final String EQUIPMENT = "equipment";
    private static String ISFAV = "isfav";
    private Equipment equipment;
    private int equipmentCollectionId;
    private boolean isMy = false;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_album_detail;
    }

    @Override
    protected void initView() {
        //展示textview的绑定11个；
        textName = bindViewId(R.id.product_name);
        //  textPrice = bindViewId(R.id.product_price);
        textValue = bindViewId(R.id.product_value);
        textBuyTime = bindViewId(R.id.product_buytime);
        //textExpireTime = bindViewId(R.id.product_expiretime);
        textFreeTime = bindViewId(R.id.product_freetime);
        textProperty = bindViewId(R.id.product_property);
        textPhoneNumber = bindViewId(R.id.tx_user_phone_number);
        textQQ = bindViewId(R.id.tx_user_qq);
        textWeChat = bindViewId(R.id.tx_user_weChat);
        textEmail = bindViewId(R.id.tx_user_email);
        product_comment = bindViewId(R.id.product_comment);
        tvIsFav = bindViewId(R.id.tv_is_fav);
        tvBuyNow = (TextView) findViewById(R.id.tv_buy_now);

        textBuyTime.setVisibility(View.GONE);

        Intent intent = this.getIntent();
        if (intent != null) {
            equipment = (Equipment) intent.getSerializableExtra(EQUIPMENT);
//            try {
//                isMy = intent.getBooleanExtra(ISFAV, false);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        }

//        if (isMy) {
//            tvIsFav.setVisibility(View.GONE);
//        }

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (equipmentCollection != null) {
                    tvIsFav.setText("已收藏");
                    equipmentCollection=null;
                } else {
                    tvIsFav.setText("未收藏");
                    equipmentCollection = null;
                }

            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                equipmentCollection = EquipmentCollection.findEquipmentCollection(LoginUserRegisterUtil.user.getPhone(), equipment.getId());
                Log.i("", "run: "+equipment.getId());
                Message message = handler.obtainMessage();
                handler.sendMessage(message);
            }
        }).start();

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
                new AlertDialog.Builder(AlbumDetailActivity.this).setMessage("立即拨打客服电话咨询").setPositiveButton("前往拨打", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        Uri data = Uri.parse("tel:" + "010-57233523");
                        intent.setData(data);
                        startActivity(intent);
                        // MyCertificateActivity.launchMyCertificateActivity(AlbumDetailActivity.this);
                    }
                }).setNegativeButton("暂时不", null).show();

               // OrderSubmitActivity.launchOrderSubmitActivity(AlbumDetailActivity.this, equipment);
            }
        });

        tvIsFav.setOnClickListener(new View.OnClickListener() {
            Calendar now = Calendar.getInstance();
            final String nowDate = now.get(Calendar.YEAR) + "年" + (now.get(Calendar.MONTH) + 1) + "月" + now.get(Calendar.DAY_OF_MONTH) + "日";

            @Override
            public void onClick(View view) {
                final Handler handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        if (equipmentCollection != null) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    EquipmentCollection.cancelCollection(equipmentCollection.getId());
                                    equipmentCollection=null;
                                }
                            }).start();

                            tvIsFav.setText("未收藏");
                        } else {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    EquipmentCollection.addEquipmentCollection(TimeUtil.checkTime(nowDate), LoginUserRegisterUtil.user.getId(), equipment.getId());
                                    Log.i("", "run: "+equipment.getId());
                                }
                            }).start();

                            tvIsFav.setText("已收藏");
                            equipmentCollection=null;
                        }
                    }
                };

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            equipmentCollection = EquipmentCollection.findEquipmentCollection(LoginUserRegisterUtil.user.getPhone(), equipment.getId());
                            Log.i("", equipmentCollection.getId()+"run: "+equipment.getId());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Message message = handler.obtainMessage();
                        handler.sendMessage(message);
                    }
                }).start();


//                if (isFav) {
//                    tvIsFav.setText("未收藏");
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        EquipmentCollection.cancelCollection(equipmentCollectionId);
//                    }
//                }).start();
//                } else {
//                    tvIsFav.setText("已收藏");
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            EquipmentCollection.addEquipmentCollection(TimeUtil.checkTime(nowDate), LoginUserRegisterUtil.user.getId(), equipment.getId());
//                        }
//                    }).start();
//                }
            }

//                final Handler handler2=new Handler(){
//                    @Override
//                    public void handleMessage(Message msg) {
//                        super.handleMessage(msg);
//                        if(isOk){
//                            tvIsFav.setText("已收藏");
//                        }else {
//                            tvIsFav.setText("未收藏");
//                        }
//                    }
//                };
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        isOk= EquipmentCollection.userIsCollect(LoginUserRegisterUtil.user.getPhone(),equipment.getId());
//                        Message message=handler2.obtainMessage();
//                        handler2.sendMessage(message);
//                    }
//                }).start();
//                final Handler handler = new Handler(){
//                    @Override
//                    public void handleMessage(Message msg) {
//                        super.handleMessage(msg);
//                        if (threadIsFav){
//                            tvIsFav.setText("已收藏");
//                        }else {
//                            tvIsFav.setText("未收藏");
//                        }
//                    }
//                };
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (EquipmentCollection.userIsCollect(LoginUserRegisterUtil.user.getPhone(), equipment.getId())) {
//
//
//                                    Thread thread1 = new Thread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            EquipmentCollection.cancelCollection(equipmentCollectionId);
//                                            threadIsFav = false;
//                                        }
//                                    });
//                                    thread1.start();
//
//                            Thread thread0 = new Thread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    equipmentCollectionId = EquipmentCollection.findEquipmentCollection(LoginUserRegisterUtil.user.getPhone(), equipment.getId()).getId();
//                                }
//                            });
//                            thread0.start();
//                        } else {
//                            Thread thread2 = new Thread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    EquipmentCollection.addEquipmentCollection(TimeUtil.checkTime(nowDate), LoginUserRegisterUtil.user.getId(), equipment.getId());
//                                    threadIsFav = true;
//                                }
//                            });
//                            thread2.start();
//
//                        }
//                        Message m = handler.obtainMessage();
//                        handler.sendMessage(m);
//                    }
//                }).start();
//            }

        });

    }

    @Override
    protected void initData() {
        textName.setText(equipment.getName());
        textValue.setText(equipment.getPrice().toString() + " 元/天");
        textBuyTime.setText(equipment.getDate().substring(0, 10));
        // textExpireTime.setText(equipment.getOver().toString().substring(0, 10));
        textFreeTime.setText(equipment.getStart().toString().substring(0, 10) + " 至 " + equipment.getEnd().toString().substring(0, 10));
        textProperty.setText(equipment.getParameter());
        product_comment.setText(equipment.getComment().toString());
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

    public static void launchAlbumDetailActivity(Activity activity, Equipment equipment) {
        Intent intent = new Intent(activity, AlbumDetailActivity.class);
        //防止多次实例调用
        /**
         * 传一个对象
         */
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        Bundle bundle = new Bundle();
        bundle.putSerializable(EQUIPMENT, equipment);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }

}
