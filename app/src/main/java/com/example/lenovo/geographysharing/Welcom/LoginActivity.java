package com.example.lenovo.geographysharing.Welcom;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lenovo.geographysharing.Element.User;
import com.example.lenovo.geographysharing.Home.HomeActivity;
import com.example.lenovo.geographysharing.R;
import com.example.lenovo.geographysharing.code.requireCode;

/**
 * 登陆页面
 */

public class LoginActivity extends Activity implements View.OnClickListener {
    private EditText userName;
    private EditText password;
    private static CheckBox auto_login;
    private CheckBox rem_pw;
    private String userNameValue, passwordValue;
    private static SharedPreferences sp;
    Context context;
    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initializeComponent();
        context = this;
    }

    private void initializeComponent() {

//        //标题栏
//        ActionBar actionBar = getSupportActionBar();
//        //隐藏标题栏
//        if (actionBar != null) {
//            actionBar.hide();
//        }
        //隐藏状态栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //控件实例化

        userName = (EditText) findViewById(R.id.login_username);
        password = (EditText) findViewById(R.id.login_password);
        auto_login = (CheckBox) findViewById(R.id.cbZiDongDengLu);
        rem_pw = (CheckBox) findViewById(R.id.cbJiYi);
        sp = this
                .getSharedPreferences("userInfo", Context.MODE_PRIVATE);


        if (sp.getBoolean("ISCHECK", false)) {
            //设置默认是记录密码状态
            rem_pw.setChecked(true);
            userName.setText(sp.getString("USER_NAME", ""));
            password.setText(sp.getString("PASSWORD", ""));
            //判断自动登陆多选框状态
            if (sp.getBoolean("AUTO_ISCHECK", false)) {
                //设置默认是自动登录状态
                auto_login.setChecked(true);
                //跳转界面

            }
            if (sp.getBoolean("ISCHECK", true)) {
                userName.setText(sp.getString("USER_NAME", ""));
                password.setText(sp.getString("PASSWORD", ""));
            }
            if (sp.getBoolean("AUTO_ISCHECK", true)) {
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                String keepedName = sp.getString("USER_NAME", "");
                intent.putExtra("userPhone", keepedName);
                LoginActivity.this.startActivity(intent);
                startActivity(intent);
            }
        }

        setOnclickListenerAutoLogin();


        Button login = (Button) findViewById(R.id.login_button);
        Button forget = (Button) findViewById(R.id.login_forget);
        Button create = (Button) findViewById(R.id.login_create);
        //控件的监听绑定
        login.setOnClickListener(this);
        forget.setOnClickListener(this);
        create.setOnClickListener(this);


        Intent intent = getIntent();
        userName.setText(intent.getStringExtra("userName"));
        password.setText(intent.getStringExtra("password"));


    }

    private void setOnclickListenerAutoLogin() {
        //监听记住密码多选框按钮事件
        rem_pw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (rem_pw.isChecked()) {
                    sp.edit().putBoolean("ISCHECK", true).commit();

                } else {
                    sp.edit().putBoolean("ISCHECK", false).commit();

                }

            }
        });

        //监听自动登录多选框事件
        auto_login.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (auto_login.isChecked()) {
                    sp.edit().putBoolean("AUTO_ISCHECK", true).commit();

                } else {
                    sp.edit().putBoolean("AUTO_ISCHECK", false).commit();
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.login_button && checkForm()) {
            final Handler handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if (user == null) {
                        Toast.makeText(context, "用户不存在+++++", Toast.LENGTH_SHORT).show();
                    } else if (userName.getText().toString().equals("")) {
                        Toast.makeText(context, "请输入手机号码", Toast.LENGTH_SHORT).show();
                    } else if (password.getText().toString().equals("")) {
                        Toast.makeText(context, "请输入密码", Toast.LENGTH_SHORT).show();
                    } else if (user.getPhone().equals(userName.getText().toString()) && user.getPassword().equals(password.getText().toString())) {
                        if (rem_pw.isChecked()) {
                            //记住用户名、密码、
                            userNameValue = user.getPhone();
                            passwordValue = user.getPassword();
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("USER_NAME", userNameValue);
                            editor.putString("PASSWORD", passwordValue);
                            editor.commit();
                        }
                        Toast.makeText(context, "登陆成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        intent.putExtra("userPhone", userName.getText().toString());
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(context, "手机号或密码错误", Toast.LENGTH_SHORT).show();
                    }
                }
            };
            new Thread(new Runnable() {
                @Override
                public void run() {
                    user = User.findUser(userName.getText().toString());
                    Message m = handler.obtainMessage();
                    handler.sendMessage(m);
                }
            }).start();
        } else if (v.getId() == R.id.login_forget) {
            //Toast.makeText(this, "忘记密码", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, requireCode.class);
            startActivity(intent);
        } else if (v.getId() == R.id.login_create) {
            //Toast.makeText(this, "注册新用户", Toast.LENGTH_SHORT);
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        }
    }

    private boolean checkForm() {
        String name = userName.getText().toString();
        String pass = password.getText().toString();

        boolean isPass = true;

        if (name.isEmpty()) {
            userName.setError("用户名不能为空");
            isPass = false;
        } else {
            userName.setError(null);
        }

        if (pass.isEmpty() || pass.length() < 6) {
            password.setError("密码输入错误");
            isPass = false;
        } else {
            password.setError(null);
        }

        return isPass;

    }

    public static void launchLoginActivity(Context context) {
        sp.edit().putBoolean("AUTO_ISCHECK", false).commit();
        sp.edit().putBoolean("ISCHECK", false).commit();
        auto_login.setChecked(false);
        Intent intent = new Intent(context, LoginActivity.class);
        //防止多次实例调用
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

}
