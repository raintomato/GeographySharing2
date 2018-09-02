package com.example.lenovo.geographysharing.Welcom;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.lenovo.geographysharing.R;
import com.example.lenovo.geographysharing.Utils.LoginUserRegisterUtil;

/**
 * 动画倒计时页面
 */

public class SpalshActivity extends Activity {

    private SharedPreferences mSharedPreference;
    private static final int GO_HOME = 1;
    private static final int GO_GUIDE = 2;
    private static final int ENTEER_DURATION = 2000;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GO_GUIDE:
                    startGuideActivity();
                    break;
                case GO_HOME:
                    startHomeActivity();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("SpalshActivity", LoginUserRegisterUtil.TOKEN);
        mSharedPreference = getSharedPreferences("config", MODE_PRIVATE);
        init();
    }

    private void init() {
        boolean isFirstIn = mSharedPreference.getBoolean("mIsFirstIn",true);
        if (isFirstIn) {
            mHandler.sendEmptyMessageDelayed(GO_GUIDE,ENTEER_DURATION);
        } else  {
            mHandler.sendEmptyMessageDelayed(GO_HOME,ENTEER_DURATION);
        }
    }

    private void startHomeActivity() {
        Intent intent = new Intent(SpalshActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void startGuideActivity() {
        Intent intent = new Intent(SpalshActivity.this, GuideActivity.class);
        startActivity(intent);
        finish();
    }
}
