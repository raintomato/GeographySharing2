package com.example.lenovo.geographysharing.Person;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;

import com.example.lenovo.geographysharing.BaseClass.BaseActivity;
import com.example.lenovo.geographysharing.BaseClass.BaseFragment;
import com.example.lenovo.geographysharing.Element.Order;
import com.example.lenovo.geographysharing.R;
import com.example.lenovo.geographysharing.others.FragmentManagerWrapper;

/**
 * Created by abc on 2018/4/18.
 */

public class PersonHomeActivity extends BaseActivity {
    private FragmentManager mFragmentManager = null;
    private BaseFragment mCurrentFragment = null;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_person_home;
    }

    @Override
    protected void initView() {
        //设置头部标题
        setSupportActionBar();//表示当前页面支持ActionBar
        setSupportArrowActionBar(true);
        setTitle("个人中心");
        //初始化fragment
        initFragemnt();

    }
    //初始化Fragment
    private void initFragemnt() {
        mFragmentManager = getSupportFragmentManager();
        //初始化传入的homefragment
        mCurrentFragment = FragmentManagerWrapper.getInstance().createFragment(PersonHomefragment.class, true);
        //fragmentmanager 事务的回滚
        mFragmentManager.beginTransaction().add(R.id.person_main_content, mCurrentFragment).commit();
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

    }


//    public static void launchPersonHomeActivity(Activity activity){
//        Intent intent = new Intent(activity,MyOrderDetailActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        activity.startActivity(intent);
//    }
}
