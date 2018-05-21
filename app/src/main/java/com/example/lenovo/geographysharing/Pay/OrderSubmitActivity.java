package com.example.lenovo.geographysharing.Pay;

import android.app.AlertDialog;
import android.content.Context;
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

import com.example.lenovo.geographysharing.Pay.util.OrderInfoUtil2_0;

import com.alipay.sdk.app.EnvUtils;
import com.alipay.sdk.app.PayTask;
import com.example.lenovo.geographysharing.BaseClass.BaseActivity;
import com.example.lenovo.geographysharing.R;

import java.util.Map;

/**
 * Created by xue on 2018/4/22.
 */

public class OrderSubmitActivity extends BaseActivity implements View.OnClickListener {
    /** 支付宝支付业务，入参app_id */
    public static final String APPID="2016091500518482";

    /** 商户私钥，pkcs8格式 */
    /** 如下私钥，RSA2_PRIVATE 或者 RSA_PRIVATE 只需要填入一个 */
    /** 如果商户两个都设置了，优先使用 RSA2_PRIVATE */
    /** RSA2_PRIVATE 可以保证商户交易在更加安全的环境下进行，建议使用 RSA2_PRIVATE */
    /** 获取 RSA2_PRIVATE，建议使用支付宝提供的公私钥生成工具生成， */
    /** 工具地址：https://doc.open.alipay.com/docs/doc.htm?treeId=291&articleId=106097&docType=1 */
    public static final String RSA2_PRIVATE="MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCKK3sL9colvsZmiIniejeNSgg9VF/WLdXc0RYVLvnmRNE+SH/21BVHIWf52ZEqb8eVphcnmL/nJGP7UVlwhdtbZNYMmr3Qn56CeX43l2PPZ/HTm9wNr2eYIaUKGZOrZN2Rzx2lHkpqYfyw3JvQj5VNw4iU3P529ij2pu05Xm3xXg1vZv3eK5quIZr5ADvekH5moPT7ydg8s/iIcmjuGuQvWT5vHaDeTuWPfZ2GZ66A8TVEDFu9/+m/iP9oGADL+sgLuRfsGeyzY9KHc0/5SDcpkMuqxYEba0LJVjTdt+jKIgp430CDFQXbaIySTw7yF5oOWtD9KAS8yT4AjIcS/iVJAgMBAAECggEAGjH+JRlXVuqhYMUYJyj2qUHU6BO8C7Yq6q91WVTLqh138i/6mshAtHif5t2ZyQriMvwHPcmW8HKUxgv5Qf0OxngKoN8hh5ELkz8Hb/CyypqxXWYaRTnxEQWn7Y9F0OPRohg7TZe9kge7yag5nkPqkZC1r0tqrxEiWQjyxsKhY3YjELNapJ7x0ojSOI9psEdB2Q0EUb1tm1wfCjRkbjMih7RFo8oDmNdTZx71oBgp2KlcPUcs+WSbaqmePOBCnLKS01BrXfz+6E/XJVDzpq1JGl8K4Y0n0aQefQcJP+m1kwLQU5NXQSsJ5t6WGk4VmV//Jzrc5+Zdbb/OJcEuYjSY5QKBgQC/eNFlqrgxyEbKzBZILR5qwR98d9fmdv5uvj0RxvMoTNc55+8cnBsRAeUQxUnDrNOoZx5MlW8Bpyu2tcTs1g23r5SXSgnoI+EJtZesP6iTjZdsgCJ/2Ty6KVmHawPK20rKFKUSEkAdsT/7aSqjz68KM5ys+SY6Kxym1SGiH7A+ywKBgQC4vApzgaZqSf7lKaU71TGqzxYuGaK3YtE9664q559ifS7v0fWZ+lFpfMvmNtRnoNlvzHrMH4eZA06IqSADG5XroEDMPW1QK2cv0GSiOoqwk3Y5eGTgmyD+k+HDdCrTMFTtOi6gU7u6UtHopVuORlQx1eFEXkQQ/Wfb5El+Nnj1uwKBgHAqXsCoGe9P2+jBVexCQidXqxJHQnsIClTlrPDVE2zwVsTdUf34lQKJkZ8Zc/TlMbjdb6p9f+IABGOsaAg1/SM2AF4ASX1sR1BgyNFnGWIW+DXfelZa9HA5+PCTiE012cI4BdtoRZcfVtP477m22FupkIbkDbjVuJsjD+jhl2WxAoGABm61xnPbQZQ2BfC/SigQRKxVT2GhItWUB53vQJs0F4/YVI1xCd2HfC7WQrviBYG6UIREOltWs2NEFsoczT7mp3+7AeGZrtL0EtwkxOlY7vETisjAanm8WmD32YBwBU6HhoMiIH8J6GD94iXv3DJiuCGEaIcHw7evKB742CY5bAUCgYEAssYAcqgZUntoM+GQ9L6RGMYso+8zR4wJWlwMrXHxQUeXO5pUlMV70avnXQVcP4b0kKQk35OVGluIsAPLjDY6ZSRod5bdetaF8V5cTZH+pqTmj2he8H/GaJBUlEHcmskXWdLIIAhgiqpvF1AIodryau+WFlNmROQcxQwWcwQKUbQ=";
    public static final String RSA_PRIVATE="";

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;

    private AlertDialog mDialog = null;
    private Context mContext = null;

    private int pay_way = 1;

    private ImageView imgAliPay = null;
    private ImageView imgWeChatPay = null;

    Button btnReduce=null;
    Button btnAdd=null;

    private String amount="1";  //产品的总数量
    private double price=2000;  //产品单价
    private EditText etAmount=null;
    private double priceAll=price;

    private TextView tvPay=null;
    private TextView tvAll=null;




    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //数量减一
            case R.id.btn_order_reduce:
                amount=String.valueOf(Integer.parseInt(etAmount.getText().toString())-1);
                etAmount.setText(amount);
                break;
            //数量加一
            case R.id.btn_order_add:
                amount=String.valueOf(Integer.parseInt(etAmount.getText().toString())+1);
                etAmount.setText(amount);
                break;
            case R.id.tv_order_pay:
                beginDialog();
                break;
            //选择支付宝支付
            case R.id.btn_alipay:
                pay_way=1;
                imgAliPay.setImageResource(R.drawable.pay_ali);
                imgWeChatPay.setImageResource(R.drawable.pay_wechat2);
                break;
            //选择微信支付
            case R.id.btn_wechatpay:
                pay_way=2;
                imgWeChatPay.setImageResource(R.drawable.pay_wechat);
                imgAliPay.setImageResource(R.drawable.pay_ali2);
                break;
            //确认支付
            case R.id.btn_pay_confirm:
                if(pay_way==1){
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

    protected void initView(){
        btnReduce=(Button)findViewById(R.id.btn_order_reduce);
        btnReduce.setEnabled(false);
        btnAdd=(Button)findViewById(R.id.btn_order_add);
        etAmount=(EditText)findViewById(R.id.et_order_amount);
        tvPay=(TextView) findViewById(R.id.tv_order_pay);
        tvAll=(TextView) findViewById(R.id.tv_order_all);

        btnReduce.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        etAmount.setText(amount);
        etAmount.addTextChangedListener(amountWatcher);
        tvAll.addTextChangedListener(priceAllWatcher);
        tvPay.setOnClickListener(this);
        tvAll.setText(priceAll+"");
    }

    private TextWatcher amountWatcher = new TextWatcher(){
        @Override
        public void afterTextChanged(Editable editable) {
            try{
                //输入框中不为空时
                if(!("".equals(etAmount.getText().toString()))){
                    tvAll.setText(price*Integer.parseInt(etAmount.getText().toString())+"");
                } else{
                    tvAll.setText("0");
                }
            } catch(Exception e){
                Toast.makeText(OrderSubmitActivity.this, "请输入数字", Toast.LENGTH_SHORT).show();
                etAmount.setText("1");
            }finally {

            }
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if("1".equals(etAmount.getText().toString())){
                btnReduce.setEnabled(false);
            }else {
                btnReduce.setEnabled(true);
            }
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if("1".equals(etAmount.getText().toString())){
                btnReduce.setEnabled(false);
            }else {
                btnReduce.setEnabled(true);
            }
        }
    };

    private TextWatcher priceAllWatcher = new TextWatcher(){
        @Override
        public void afterTextChanged(Editable editable) {
            priceAll=Double.parseDouble(tvAll.getText().toString());

//            Toast.makeText(MainActivity.this, priceAll+"", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }
    };


    //显示支付的弹出框
    public void beginDialog(){
        mDialog = new AlertDialog.Builder(OrderSubmitActivity.this).create();
        mDialog.show();
        final Window window = mDialog.getWindow();
        if (window != null) {
            window.setContentView(R.layout.order_dialog_pay_panel);
            window.setGravity(Gravity.BOTTOM);
//            window.setWindowAnimations(R.style.anim_panel_up_from_bottom);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            //设置属性
            final WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            window.setAttributes(params);

            imgAliPay = (ImageView)window.findViewById(R.id.btn_alipay);
            imgWeChatPay = (ImageView)window.findViewById(R.id.btn_wechatpay);

            window.findViewById(R.id.btn_alipay).setOnClickListener(this);
            window.findViewById(R.id.btn_wechatpay).setOnClickListener(this);
            window.findViewById(R.id.btn_pay_confirm).setOnClickListener(this);
            window.findViewById(R.id.btn_pay_cancel).setOnClickListener(this);

            Button btnConfirm=(Button)window.findViewById(R.id.btn_pay_confirm);
            btnConfirm.setText("确认支付 ￥"+priceAll);
        }
    }

    //进行支付宝支付
    public void aliPay(){
        if (TextUtils.isEmpty(APPID) || (TextUtils.isEmpty(RSA2_PRIVATE) && TextUtils.isEmpty(RSA_PRIVATE))) {
            Toast.makeText(OrderSubmitActivity.this, "需要配置APPID | RSA_PRIVATE", Toast.LENGTH_SHORT).show();
        } else{
            /**
             * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
             * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
             * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
             *
             * orderInfo的获取必须来自服务端；
             */
            boolean rsa2 = (RSA2_PRIVATE.length()>0);
            Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, rsa2,priceAll);
            String orderParam = OrderInfoUtil2_0.buildOrderParam(params);

            String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
            String sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2);
            final String orderInfo = orderParam + "&" + sign;

            Runnable payRunnable = new Runnable() {
                @Override
                public void run() {
                    PayTask alipay = new PayTask(OrderSubmitActivity.this);
                    Map<String, String> result = alipay.payV2(orderInfo, true);

                    Message msg = new Message();
                    msg.what = SDK_PAY_FLAG;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                }
            };

            Thread payThread = new Thread(payRunnable);
            payThread.start();
        }
    }

    //进行微信支付
    public void weChatPay(){
        Toast.makeText(OrderSubmitActivity.this, "微信支付", Toast.LENGTH_SHORT).show();
    }

    private Handler mHandler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case SDK_PAY_FLAG:{
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult(); // 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus，为9000则代表支付成功
                    if(TextUtils.equals(resultStatus,"9000")){
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(OrderSubmitActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else{
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(OrderSubmitActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                default:{
                    break;
                }

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
}
