package com.example.lenovo.geographysharing.Person;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.lenovo.geographysharing.BaseClass.BaseActivity;
import com.example.lenovo.geographysharing.BaseClass.BaseFragment;
import com.example.lenovo.geographysharing.R;
import com.example.lenovo.geographysharing.delete.adapter.MyIssueAdapter;
import com.example.lenovo.geographysharing.others.FragmentManagerWrapper;

/**
 * Created by abc on 2018/4/21.
 */

public class MyissuedActivity extends BaseActivity implements MyIssueAdapter.IonSlidingViewClickListener {

    private FragmentManager mFragmentManager;
    private BaseFragment mCurrentFragment;
    public  static String typeIsssu = null;
    private static String TYPRISSURE="TYPRISSURE";
    private String titleName = null;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_issued;
    }

    @Override
    protected void initView() {
        Intent intent = getIntent();
        if (intent!=null) {
            typeIsssu = intent.getStringExtra(TYPRISSURE);
        }
        Log.i("ssssss", "initView: "+typeIsssu);

        switch (MyissuedActivity.typeIsssu){
            case "设备":
                titleName = "我的设备发布";
                break;
            case "软件":
                titleName = "我的软件发布";
                break;
            case "人员":
                titleName = "我的人员发布";
                break;
            default:
                break;
        }
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

    public static void launchMyIssueActivity(Context context, String typeIsssu) {
        Intent intent = new Intent(context, MyissuedActivity.class);
        //防止多次实例调用
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(TYPRISSURE,typeIsssu);
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

    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void onDeleteBtnCilck(View view, int position) {

    }
}
