package com.example.lenovo.geographysharing.Welcom;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lenovo.geographysharing.Element.User;
import com.example.lenovo.geographysharing.Element.VerifyCode;
import com.example.lenovo.geographysharing.R;
import com.mob.MobSDK;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * 注册页面
 */

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText RegisterName;
    private EditText Password1;
    private EditText Password2;
    private EditText Phone;
    private EditText code;
    private Button getCode;
    private Button register;
    VerifyCode verifyCode;
    User user;
    Context context;
    boolean success=false,sendOk=false;
    int i=60;           //短信验证提示时间为60秒


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        setContentView(R.layout.activity_register);
        //启动短信验证sdk
        MobSDK.init(this,"252b1b295dd62","f9f0d0f01ba2dae86076a8ec32a8a7ae");

        //控件绑定
        RegisterName = (EditText) findViewById(R.id.register_name);
        Password1 = (EditText) findViewById(R.id.register_password1);
        Password2 = (EditText) findViewById(R.id.register_password2);
        Phone = (EditText) findViewById(R.id.phone_number);
        getCode=(Button)findViewById(R.id.get_code);   //获取验证码
        code=(EditText)findViewById(R.id.phone_code);
        register=(Button)findViewById(R.id.create_register_button);
        register.setOnClickListener(this);
        getCode.setOnClickListener(this);

        final EventHandler eventHandler = new EventHandler(){
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);
            }
        };
        SMSSDK.registerEventHandler(eventHandler); // 注册回调监听接口
    }

    Handler handler=new Handler(){
        public void handleMessage(Message msg) {
            if (msg.what == -1) {
                getCode.setText(i+"秒");  //为啥非要加个文字
            } else if (msg.what == -2) {
                getCode.setText("重新发送");
                getCode.setClickable(true); // 设置可点击
                i = 60;
            } else {
                int event = msg.arg1;
                int result = msg.arg2;
                Object data = msg.obj;
                if (result == SMSSDK.RESULT_COMPLETE) {
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        //Toast.makeText(getApplicationContext(), "注册成功",
                          //      Toast.LENGTH_SHORT).show();


                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        //Toast.makeText(getApplicationContext(), "验证码已经发送",
                          //      Toast.LENGTH_SHORT).show();
                    } else {
                        ((Throwable) data).printStackTrace();
                    }
                    success=true;
                }else if(result== SMSSDK.RESULT_ERROR){//验证码错误
                    //Toast.makeText(getApplicationContext(), "验证码有误",
                      //      Toast.LENGTH_SHORT).show();
                    //可能存在bug
                }
            }

        }

    };
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.create_register_button && checkForm()) {
            //Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
           // Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            //startActivity(intent);
            //判断验证码是否正确(可以添加一个进度条)
            SMSSDK.submitVerificationCode("86",Phone.getText().toString(),code.getText().toString());
            final Handler handler=new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if(RegisterName.getText().toString().equals("")){
                        Toast.makeText(context, "请填写用户名", Toast.LENGTH_SHORT).show();
                    }else if(!Password1.getText().toString().equals(Password2.getText().toString())) {
                        Toast.makeText(context, "两次密码不一致", Toast.LENGTH_SHORT).show();
                    } else if(Password1.getText().toString().equals("")||Password2.getText().toString().equals("")) {
                        Toast.makeText(context, "密码填写不能为空", Toast.LENGTH_SHORT).show();
                    } else if(Phone.getText().toString().equals("")) {
                        Toast.makeText(context, "请填写手机号码", Toast.LENGTH_SHORT).show();
                    }else if(user!=null){
                        Toast.makeText(context, "用户已存在", Toast.LENGTH_SHORT).show();
                        user=null;
                    }else if(code.getText().equals("")){
                        Toast.makeText(context, "请填写验证码", Toast.LENGTH_SHORT).show();
                    }else if(success) {
                        //界面跳转
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        intent.putExtra("userName",Phone.getText().toString());
                        intent.putExtra("password",Password1.getText().toString());
                        startActivity(intent);
                        Toast.makeText(context, "注册成功", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(context, "注册失败", Toast.LENGTH_SHORT).show();
                    }
                }
            };
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if(Phone.getText().toString()!="")
                        user= User.findUser(Phone.getText().toString());
                    if(RegisterName.getText().toString().equals("")){
                        //Toast.makeText(context, "请填写用户名", Toast.LENGTH_SHORT).show();
                    }else if(!Password1.getText().toString().equals(Password2.getText().toString())) {
                        //Toast.makeText(context, "两次密码不一致", Toast.LENGTH_SHORT).show();
                    } else if(Password1.getText().toString().equals("")||Password2.getText().toString().equals("")) {
                        //Toast.makeText(context, "密码填写不能为空", Toast.LENGTH_SHORT).show();
                    } else if(Phone.getText().toString().equals("")) {
                        //Toast.makeText(context, "请填写手机号码", Toast.LENGTH_SHORT).show();
                    }else if(user!=null){
                        //Toast.makeText(context, "用户已存在", Toast.LENGTH_SHORT).show();
                    }else if(code.getText().equals("")){
                        //Toast.makeText(context, "请填写验证码", Toast.LENGTH_SHORT).show();
                    }else if(success) {
                        success= User.register(RegisterName.getText().toString(),Password1.getText().toString(),Phone.getText().toString());
                    }
                    Message m=handler.obtainMessage();
                    handler.sendMessage(m);
                }
            }).start();
        }
        if(v.getId() == R.id.get_code){
            //获取验证码
            final Handler handler=new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if(RegisterName.getText().toString().equals("")){
                        Toast.makeText(context, "请填写用户名", Toast.LENGTH_SHORT).show();
                    }else if(!Password1.getText().toString().equals(Password2.getText().toString())) {
                        Toast.makeText(context, "两次密码不一致", Toast.LENGTH_SHORT).show();
                    } else if(Password1.getText().toString().equals("")||Password2.getText().toString().equals("")) {
                        Toast.makeText(context, "密码填写不能为空", Toast.LENGTH_SHORT).show();
                    } else if(Phone.getText().toString().equals("")) {
                        Toast.makeText(context, "请填写手机号码", Toast.LENGTH_SHORT).show();
                    }else if(user!=null){
                        Toast.makeText(context, "用户已存在", Toast.LENGTH_SHORT).show();
                        user=null;
                    }else if(sendOk){
                        sendCode();
                        Toast.makeText(context, "验证码发送成功", Toast.LENGTH_SHORT).show();
                        sendOk=false;
                    }
                }
            };
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if(Phone.getText().toString()!="")
                        user= User.findUser(Phone.getText().toString());
                    if(RegisterName.getText().toString().equals("")){
                        //Toast.makeText(context, "请填写用户名", Toast.LENGTH_SHORT).show();
                    }else if(!Password1.getText().toString().equals(Password2.getText().toString())) {
                        //Toast.makeText(context, "两次密码不一致", Toast.LENGTH_SHORT).show();
                    } else if(Password1.getText().toString().equals("")||Password2.getText().toString().equals("")) {
                        //Toast.makeText(context, "密码填写不能为空", Toast.LENGTH_SHORT).show();
                    } else if(Phone.getText().toString().equals("")) {
                        //Toast.makeText(context, "请填写手机号码", Toast.LENGTH_SHORT).show();
                    }else if(user!=null){
                        //Toast.makeText(context, "用户已存在", Toast.LENGTH_SHORT).show();
                    }else{
                        sendOk=true;
                        verifyCode= VerifyCode.findVerifyCode(Phone.getText().toString());
                    }
                    Message m=handler.obtainMessage();
                    handler.sendMessage(m);
                }
            }).start();
        }
    }

    //发送验证码封装成方法
    public void sendCode() {
        SMSSDK.getVerificationCode("86",Phone.getText().toString()); // 调用sdk发送短信验证
        getCode.setClickable(false);     //按钮不可点击
        getCode.setText(i + "秒");
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (i = 60; i > 0; i--) {
                    handler.sendEmptyMessage(-1);
                    if (i <= 0) {
                        break;
                    }
                    try {
                        Thread.sleep(1000);// 线程休眠实现读秒功能
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                handler.sendEmptyMessage(-2);// 在60秒后重新显示为重新发送
            }
        }).start();
    }

    protected void onDestroy() {
        SMSSDK.unregisterAllEventHandler();
        super.onDestroy();
    }
    private boolean checkForm(){
        final String name = RegisterName.getText().toString();
        final String pass1 = Password1.getText().toString();
        final String pass2 = Password2.getText().toString();
        final String phone = Phone.getText().toString();

        boolean isPass = true;

        if (name.isEmpty()){
            RegisterName.setError("请输入姓名!");
            isPass =false;
        }else{
            RegisterName.setError(null);
        }

        if (pass1.isEmpty() || pass1.length()<6){
            Password1.setError("输入密码大于6位!");
            isPass =false;
        }else{
            Password1.setError(null);
        }

        if (pass2.isEmpty()||pass2.length()<6||!(pass2.equals(pass1))){
            Password2.setError("两次密码输入不一致!");
            isPass =false;
        }else{
            Password2.setError(null);
        }

        if (phone.isEmpty()||phone.length()!=11){
            Phone.setError("手机号码输入错误!");
            isPass =false;
        }else{
            Phone.setError(null);
        }

        return isPass;

    }
}
