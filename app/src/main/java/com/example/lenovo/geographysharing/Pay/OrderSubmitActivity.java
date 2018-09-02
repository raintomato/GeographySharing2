package com.example.lenovo.geographysharing.Pay;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.geographysharing.Details.AlbumDetailActivity;
import com.example.lenovo.geographysharing.Element.Equipment;
import com.example.lenovo.geographysharing.Element.Image;
import com.example.lenovo.geographysharing.Element.Order;

import com.alipay.sdk.app.EnvUtils;
import com.alipay.sdk.app.PayTask;
import com.example.lenovo.geographysharing.BaseClass.BaseActivity;
import com.example.lenovo.geographysharing.Element.User;
import com.example.lenovo.geographysharing.R;
import com.example.lenovo.geographysharing.Utils.JsonDataUtil;
import com.example.lenovo.geographysharing.Utils.LoginUserRegisterUtil;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by xue on 2018/4/22.
 */

public class OrderSubmitActivity extends BaseActivity implements View.OnClickListener {

    private AlertDialog mDialog = null;
    private Context mContext = null;

    private int pay_way = 1;

    private ImageView imgAliPay = null;
    private ImageView imgWeChatPay = null;

    Button btnReduce = null;
    Button btnAdd = null;

    private String amount = "1";  //产品的总数量
    private double price = 0;  //产品单价
    private EditText etAmount = null;
    private double priceAll = 0;

    private TextView tvPay = null;
    private TextView tvAll = null;

    private Equipment equipment = null;

    EditText etMore = null;

    //发票相关信息
    EditText etOrgName = null;
    EditText etOrgNumber = null;
    EditText etOrgPhone = null;
    EditText etOrgAddress = null;
    EditText etOrgPersonName = null;
    Double poundage = 0.2;  //之后还有计算平台扣除的


    private static final String EQUIPMENT = "equipment";


    protected void initView() {
        //取出从商品详情界面传送过来的equipment对象
        Intent intent = this.getIntent();
        if (intent != null) {
            equipment = (Equipment) intent.getSerializableExtra(EQUIPMENT);
        }
        price = equipment.getPrice();  //设置加签
        priceAll = price;

        btnReduce = (Button) findViewById(R.id.btn_order_reduce);
        btnReduce.setEnabled(false);
        btnAdd = (Button) findViewById(R.id.btn_order_add);
        etAmount = (EditText) findViewById(R.id.et_order_amount);
        tvPay = (TextView) findViewById(R.id.tv_order_pay);
        tvAll = (TextView) findViewById(R.id.tv_order_all);

        //发票相关
        etOrgName = (EditText) findViewById(R.id.et_order_org_name);
        etOrgNumber = (EditText) findViewById(R.id.et_order_invoice_number);
        etOrgPhone = (EditText) findViewById(R.id.et_order_invoice_person_name);
        etOrgAddress = (EditText) findViewById(R.id.et_order_invoice_phone);
        etOrgPersonName = (EditText) findViewById(R.id.et_order_invoice_address);
        etMore = (EditText) findViewById(R.id.et_order_leave);

        btnReduce.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        etAmount.setText(amount);
        etAmount.addTextChangedListener(amountWatcher);
        tvAll.addTextChangedListener(priceAllWatcher);
        tvPay.setOnClickListener(this);
        tvAll.setText(priceAll + "");

        //将数据显示在界面上
        showDate();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //数量减一
            case R.id.btn_order_reduce:
                amount = String.valueOf(Integer.parseInt(etAmount.getText().toString()) - 1);
                etAmount.setText(amount);
                break;
            //数量加一
            case R.id.btn_order_add:
                amount = String.valueOf(Integer.parseInt(etAmount.getText().toString()) + 1);
                etAmount.setText(amount);
                break;
            case R.id.tv_order_pay:
                beginDialog();
                break;
            //选择支付宝支付
            case R.id.btn_alipay:
                pay_way = 1;
                imgAliPay.setImageResource(R.drawable.pay_ali);
                imgWeChatPay.setImageResource(R.drawable.pay_wechat2);
                break;
            //选择微信支付
            case R.id.btn_wechatpay:
                pay_way = 2;
                imgWeChatPay.setImageResource(R.drawable.pay_wechat);
                imgAliPay.setImageResource(R.drawable.pay_ali2);
                break;
            //确认支付
            case R.id.btn_pay_confirm:
                if (pay_way == 1) {
                    aliPay();
                    mDialog.dismiss();
                } else {
                    weChatPay();
                    mDialog.dismiss();
                }
                break;
            //取消支付
            case R.id.btn_pay_cancel:
                mDialog.dismiss();
                break;
            default:
                break;
        }
    }



    //购买数量的EditText的监听
    private TextWatcher amountWatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable editable) {
            try {
                //输入框中不为空时
                if (!("".equals(etAmount.getText().toString()))) {
                    //使用BigDecimal精确计算乘法
                    BigDecimal b1 = new BigDecimal(Double.toString(price));
                    BigDecimal b2 = new BigDecimal(etAmount.getText().toString());
                    //精度，保留两位小数
                    java.text.DecimalFormat myFormat=new java.text.DecimalFormat("0.00");
                    tvAll.setText( myFormat.format(b1.multiply(b2).doubleValue()) );
                } else {
                    tvAll.setText("0");
                }
            } catch (Exception e) {
                Toast.makeText(OrderSubmitActivity.this, "请输入数字", Toast.LENGTH_SHORT).show();
                etAmount.setText("1");
            }
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if ("1".equals(etAmount.getText().toString())) {
                btnReduce.setEnabled(false);
            } else {
                btnReduce.setEnabled(true);
            }
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if ("1".equals(etAmount.getText().toString())) {
                btnReduce.setEnabled(false);
            } else {
                btnReduce.setEnabled(true);
            }
        }
    };

    //下方底框的总价的TextView的监听
    private TextWatcher priceAllWatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable editable) {
             priceAll = Double.parseDouble(tvAll.getText().toString());
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }
    };


    //显示支付的弹出框
    public void beginDialog() {
        mDialog = new AlertDialog.Builder(OrderSubmitActivity.this).create();
        mDialog.show();
        final Window window = mDialog.getWindow();
        if (window != null) {
            window.setContentView(R.layout.order_dialog_pay_panel);
            window.setGravity(Gravity.BOTTOM);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            //设置属性
            final WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            window.setAttributes(params);

            imgAliPay = (ImageView) window.findViewById(R.id.btn_alipay);
            imgWeChatPay = (ImageView) window.findViewById(R.id.btn_wechatpay);
            imgWeChatPay.setVisibility(View.GONE);

            window.findViewById(R.id.btn_alipay).setOnClickListener(this);
            window.findViewById(R.id.btn_wechatpay).setOnClickListener(this);
            window.findViewById(R.id.btn_pay_confirm).setOnClickListener(this);
            window.findViewById(R.id.btn_pay_cancel).setOnClickListener(this);

            Button btnConfirm = (Button) window.findViewById(R.id.btn_pay_confirm);
            btnConfirm.setText("确认支付 ￥" + priceAll);
        }
    }

    //进行支付宝支付
    public void aliPay() {
        //异步调用支付宝接口
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                //获取当前时间
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String createTime = sdf.format(date);
                createTime = createTime.replace(' ', 'T');  //转化为Python要求的格式
                //获取订单相关信息//
                //1、订单标题
                String subject = equipment.getName();
                //2、订单号在后台生成
                //3、销售产品码，定值"QUICK_MSECURITY_PAY"
                //4、订单总金额，priceAll
                //5、支付宝系统中的交易流水号，后台获取
                //6、业务
                //7、支付完成时间，后台完成
                //8、支付完成状态，false，后台处理
                //9、创建时间，createTime
                //10、更新时间，开始为createTime
                //11、单位名称
                String orgName = etOrgName.getText().toString();
                //12、纳税人识别号
                String orgNumber = etOrgNumber.getText().toString();
                //13、纳税人姓名
                String orgPersonName = etOrgPersonName.getText().toString();
                //14、纳税人手机
                String orgPhone = etOrgPhone.getText().toString();
                //15、纳税人地址
                String orgAddress = etOrgAddress.getText().toString();
                //16、产品Id
                Integer product_code = equipment.getId();
                //17、收款人
                Integer payee = equipment.getOwner().getId();
                //18、支付者
                Integer payer = LoginUserRegisterUtil.user.getId();
                //19、产品类型
                String type = "设备";
                //20、收费比率

                /*
                7-16
                 */
                 Thread thread = new Thread(new Runnable() {
                     @Override
                     public void run() {
                         try{
                             poundage = Order.getProportion("设备");
                         }catch (Exception e){
                             e.printStackTrace();
                         }
                     }
                 });
                thread.start();
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //21、实际到账
                //使用BigDecimal精确计算乘法
                BigDecimal b1 = new BigDecimal(Double.toString(priceAll));  //原来的总价
                BigDecimal b2 = new BigDecimal(Double.toString(poundage));  //平台扣除的比率
                BigDecimal b3 = new BigDecimal("1");
                //计算平台扣除
                poundage = b1.multiply(b2).doubleValue();
                //计算实际到账，精度，保留两位小数
                b2 = b3.subtract(b2);
                Double receipt = b1.multiply(b2).doubleValue();
                //22、备注
                String orderMore = etMore.getText().toString();

                //从服务器获取订单信息，含加签
                final String orderInfo = Order.addOrder(subject,"none","QUICK_MSECURITY_PAY",
                        priceAll, "none","无",createTime,false,createTime,createTime,orgName,
                        orgNumber,orgPersonName,orgPhone,orgAddress,product_code,
                        payee,payer,"设备",poundage,receipt,orderMore);
                String s = orderInfo;
                int a=1;
                int b=2;
//                final String orderInfo = Order.addOrder(subject, "ok", "OK", priceAll, "OK", "OK", "OK", createTime, false, createTime, createTime);
                //支付结果
                Map<String, String> result;
                //调用支付宝接口
                if (orderInfo != null) {
                    PayTask alipay = new PayTask(OrderSubmitActivity.this);
                    result = alipay.payV2(orderInfo, true);
                    Message msg = new Message();
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                }
            }
        };
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    //进行微信支付
    public void weChatPay() {
        Toast.makeText(OrderSubmitActivity.this, "微信支付", Toast.LENGTH_SHORT).show();
    }

    //支付宝支付结果的验证
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            @SuppressWarnings("unchecked")
            PayResult payResult = new PayResult((Map<String, String>) msg.obj);
            /**
             对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
             */
            String resultInfo = payResult.getResult(); // 同步返回需要验证的信息
            String resultStatus = payResult.getResultStatus();
            // 判断resultStatus，为9000则代表支付成功
            if (TextUtils.equals(resultStatus, "9000")) {
                // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                Toast.makeText(OrderSubmitActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
            } else {
                // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                Toast.makeText(OrderSubmitActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.order_submit;
    }


    @Override
    protected void initData() {

    }

    public static void launchOrderSubmitActivity(Activity activity, Equipment equipment) {
        Intent intent = new Intent(activity, OrderSubmitActivity.class);
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

    public void showDate() {
        TextView tvUserName = (TextView) findViewById(R.id.order_text_consignee_name);  //用户姓名
        TextView tvPhone = (TextView) findViewById(R.id.order_text_consignee_phone);  //电话
        TextView tvAddress = (TextView) findViewById(R.id.order_text_consignee_address);  //地址
        final ImageView imgEquipment = (ImageView) findViewById(R.id.equip_image);  //设备图片
        TextView tvEquipName = (TextView) findViewById(R.id.equip_name);  //地址
        TextView tvEquipPrice = (TextView) findViewById(R.id.equip_price);  //价钱
        tvUserName.setText(LoginUserRegisterUtil.user.getUser_name());
        tvPhone.setText(LoginUserRegisterUtil.user.getPhone());
        tvAddress.setText(LoginUserRegisterUtil.user.getAddress());
        tvEquipName.setText(equipment.getName());
        tvEquipPrice.setText(equipment.getPrice() + "元/每天");
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                imgEquipment.setImageBitmap(JsonDataUtil.getImage(equipment.getPicture().getImageURL()));//加载 此处应加到本地 再设置
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

}
