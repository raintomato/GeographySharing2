package com.example.lenovo.geographysharing.Home;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.geographysharing.BaseClass.BaseActivity;
import com.example.lenovo.geographysharing.BaseClass.BaseFragment;
import com.example.lenovo.geographysharing.Element.Image;
import com.example.lenovo.geographysharing.Element.User;
import com.example.lenovo.geographysharing.Person.PersonHomeActivity;
import com.example.lenovo.geographysharing.Person.PersonHomefragment;
import com.example.lenovo.geographysharing.R;
import com.example.lenovo.geographysharing.Utils.JsonDataUtil;
import com.example.lenovo.geographysharing.Utils.LoginUserRegisterUtil;
import com.example.lenovo.geographysharing.Welcom.LoginActivity;
import com.example.lenovo.geographysharing.others.FragmentManagerWrapper;

public class HomeActivity extends BaseActivity {
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private MenuItem mPreItem;
    private FragmentManager mFragmentManager;
    private BaseFragment mCurrentFragment;
    private TextView username;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    protected void initView() {
        setSupportActionBar();
        setActionBarIcon(R.drawable.ic_drawer_home);
        setTitle("首页");
        mDrawerLayout = bindViewId(R.id.drawer_layout);
        mNavigationView = bindViewId(R.id.navigation_view);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolBar, R.string.drawer_open, R.string.drawer_close);
        mActionBarDrawerToggle.syncState();
        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);

        mPreItem = mNavigationView.getMenu().getItem(0);
        mPreItem.setChecked(true);//GeographySharing1.3 10.09
        initFragemnt();
        //设置监听
        handleNavigationView();
        username=(TextView) mNavigationView.getHeaderView(0).findViewById(R.id.home_username);
        final ImageView imageView =(ImageView) mNavigationView.getHeaderView(0).findViewById(R.id.profile_image);
        Intent intent=super.getIntent();
        final String phone=intent.getStringExtra("userPhone");
        final Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (LoginUserRegisterUtil.user!=null) {
                    username.setText(LoginUserRegisterUtil.user.getNick_name());
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setImageBitmap(JsonDataUtil.getImage(LoginUserRegisterUtil.user.getImageURL()));
                        }
                    });
                    thread.start();
                    try {
                        thread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                else
                    username.setText("未登录");
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                LoginUserRegisterUtil.user= User.findUser(phone);
                Message m=handler.obtainMessage();
                handler.sendMessage(m);
            }
        }).start();
    }

    //初始化Fragment
    private void initFragemnt() {

        mFragmentManager = getSupportFragmentManager();
        //初始化传入的homefragment
        mCurrentFragment = FragmentManagerWrapper.getInstance().createFragment(HomeFragment.class, true);
        //fragmentmanager 事务的回滚
        mFragmentManager.beginTransaction().add(R.id.fl_main_content, mCurrentFragment).commit();
    }

    private void handleNavigationView() {
        //获取登陆用户
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                if (mPreItem != null) {
                    mPreItem.setCheckable(false);
                }
                switch (item.getItemId()) {
                    case R.id.navigation_item_video:
                        switchFragment(HomeFragment.class);
                        mToolBar.setTitle("首页");
                        break;
                    case R.id.navigation_item_blog:
//                        switchPersonHome();
                        switchFragment(PersonHomefragment.class);
                        mToolBar.setTitle("个人中心");
                        break;
                    case R.id.navigation_item_about:
                        switchFragment(AboutFragment.class);
                        mToolBar.setTitle("关于我们");
                        break;
                    case R.id.navigation_item_issue:
                        switchFragment(IssueFragment.class);
                        mToolBar.setTitle("选择一个发布类型");
                        break;
                    case R.id.navigation_item_person:
                        switchFragment(CertificateFragment.class);
                        mToolBar.setTitle("信息认证");
                        break;
                    case R.id.navigation_item_quit:
                        LoginActivity.launchLoginActivity(HomeActivity.this);
                        finish();
                    default:
                        break;
                }
                mPreItem = item;
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                item.setChecked(true);
                return false;
            }
        });
    }


    private void switchPersonHome() {
        Intent intent = new Intent(this, PersonHomeActivity.class);
        startActivity(intent);
    }


    private void switchFragment(Class<?> clazz) {
        BaseFragment fragment = FragmentManagerWrapper.getInstance().createFragment(clazz);
        if (fragment.isAdded()) {
            mFragmentManager.beginTransaction().hide(mCurrentFragment).show(fragment).commitAllowingStateLoss();
        } else {
            mFragmentManager.beginTransaction().hide(mCurrentFragment).add(R.id.fl_main_content, fragment).commitAllowingStateLoss();
        }
        mCurrentFragment = fragment;
    }

    @Override
    protected void initData() {
        //// TODO
    }
    //获取登陆用户
    public User getUser(){
        User user=null;
        Intent intent=new Intent();
        String phone=intent.getStringExtra("userPhone");
        if(phone!=null)
            user=User.findUser(phone);
        if(user!=null)
            username.setText(username.getText());
        else
            username.setText("未登录");
        return user;
    }
}
