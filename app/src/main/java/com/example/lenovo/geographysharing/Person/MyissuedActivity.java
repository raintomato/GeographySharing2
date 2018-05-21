package com.example.lenovo.geographysharing.Person;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;

import com.example.lenovo.geographysharing.BaseClass.BaseActivity;
import com.example.lenovo.geographysharing.BaseClass.BaseFragment;
import com.example.lenovo.geographysharing.R;
import com.example.lenovo.geographysharing.others.FragmentManagerWrapper;

/**
 * Created by abc on 2018/4/21.
 */

public class MyissuedActivity extends BaseActivity {

    private FragmentManager mFragmentManager;
    private BaseFragment mCurrentFragment;


    private String titleName = null;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_issued;
    }

    @Override
    protected void initView() {
        titleName = "我的发布";
        setSupportActionBar();//表示当前页面支持ActionBar
        setSupportArrowActionBar(true);
        setTitle(titleName);
        //初始化fragment
        initFragemnt();

    }

    //初始化Fragment
    private void initFragemnt() {

        mFragmentManager = getSupportFragmentManager();
        //初始化传入的homefragment
        mCurrentFragment = FragmentManagerWrapper.getInstance().createFragment(MyIssuedFragment.class,true);
        //fragmentmanager 事务的回滚
        mFragmentManager.beginTransaction().add(R.id.my_issued_content,mCurrentFragment).commit();
    }



    public static void launchMyIssueActivity(Context context) {
        Intent intent = new Intent(context, MyissuedActivity.class);
        //防止多次实例调用
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
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
}
