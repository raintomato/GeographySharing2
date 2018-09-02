package com.example.lenovo.geographysharing.Home;

import android.app.ActivityManager;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.geographysharing.BaseClass.BaseActivity;
import com.example.lenovo.geographysharing.BaseClass.BaseFragment;
import com.example.lenovo.geographysharing.Element.Image;
import com.example.lenovo.geographysharing.Element.User;
import com.example.lenovo.geographysharing.Issue.IssueDetailRequestActivity;
import com.example.lenovo.geographysharing.Pay.OrderSubmitActivity;
import com.example.lenovo.geographysharing.Person.MyCertificateActivity;
import com.example.lenovo.geographysharing.Person.PersonHomeActivity;
import com.example.lenovo.geographysharing.Person.PersonHomefragment;
import com.example.lenovo.geographysharing.R;
import com.example.lenovo.geographysharing.Utils.JsonDataUtil;
import com.example.lenovo.geographysharing.Utils.LoginUserRegisterUtil;
import com.example.lenovo.geographysharing.Utils.ToastUitls;
import com.example.lenovo.geographysharing.Welcom.LoginActivity;
import com.example.lenovo.geographysharing.identify.IdentifySucceed;
import com.example.lenovo.geographysharing.others.FragmentManagerWrapper;

import java.util.List;

public class HomeActivity extends BaseActivity {
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private MenuItem mPreItem;
    private FragmentManager mFragmentManager;
    private BaseFragment mCurrentFragment;
    public static TextView username;
    private TextView test;
    public static boolean ifSuccess = false;
    private Bitmap tempBitmap;
    public static ImageView imageView;



    private static final String CHANNEL_ID = "channid";
    private int mChannId;


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
/**
 * 7-12抽屉2/3
 // */
//        WindowManager wm = this.getWindowManager();//获取屏幕宽高
//        int width1 = wm.getDefaultDisplay().getWidth();
//        int height1 = wm.getDefaultDisplay().getHeight();
//        ViewGroup.LayoutParams para= mDrawerLayout.getLayoutParams();//获取drawerlayout的布局
//        para.width=width1/3*2;//修改宽度
//        para.height=height1;//修改高度
//        mDrawerLayout.setLayoutParams(para);

        mNavigationView = bindViewId(R.id.navigation_view);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolBar, R.string.drawer_open, R.string.drawer_close);
        mActionBarDrawerToggle.syncState();
        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);
        mPreItem = mNavigationView.getMenu().getItem(0);
        mPreItem.setChecked(false);//GeographySharing1.3 10.09
        initFragemnt();
        //设置监听
        handleNavigationView();
        username = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.home_username);
        imageView = (ImageView) mNavigationView.getHeaderView(0).findViewById(R.id.profile_image);
        Intent intent = super.getIntent();
        final String phone = intent.getStringExtra("userPhone");


        //获取判断跳转到哪个fragment的参数
        mChannId = intent.getIntExtra(CHANNEL_ID, 0);   //默认值
        if (mChannId == 1) {
            switchFragment(CertificateFragment.class);
        }
        if (LoginUserRegisterUtil.user != null) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        username.setText(LoginUserRegisterUtil.user.getNick_name());
                        imageView.setImageBitmap(JsonDataUtil.getImage(LoginUserRegisterUtil.user.getImageURL()));
                    } catch (Exception e) {
                        LoginActivity.launchLoginActivity(HomeActivity.this);
                        finish();
                    }
                }
            });
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (LoginUserRegisterUtil.user != null) {
                    username.setText(LoginUserRegisterUtil.user.getNick_name());
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            tempBitmap = JsonDataUtil.getImage(LoginUserRegisterUtil.user.getImageURL());
                            LoginUserRegisterUtil.photo = tempBitmap;
                        }
                    });
                    thread.start();
                    try {
                        thread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (tempBitmap == null) {
                        imageView.setImageResource(R.drawable.tubiao);
                    } else {
                        imageView.setImageBitmap(LoginUserRegisterUtil.photo);
                    }

                } else
                    username.setText("未登录");
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i("HomeActivity", "run: " + LoginActivity.sp.getString("USER_TOKEN", ""));
                LoginUserRegisterUtil.user = User.findUser(phone);
                Message m = handler.obtainMessage();
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
                    case R.id.navigation_item_demand:
                        switchFragment(RequestListFragment.class);
                        mToolBar.setTitle("需求列表");
                        break;
                    case R.id.navigation_item_issue:

                        ifSuccess = LoginUserRegisterUtil.user.isComplete();

                        PersonHomefragment.isSuccessTemp = LoginActivity.sp.getBoolean("IFTEMP",false);

                        if (PersonHomefragment.isSuccessTemp||ifSuccess) {
                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(HomeActivity.this)
                                    .setTitle("              选择发布类型")
                                    .setItems(new String[]{"出租", "求租"}, new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case 0:
                                                    switchFragment(IssueFragment.class);
                                                    mToolBar.setTitle("选择一个发布类型");
                                                    break;
                                                case 1:
                                                    IssueDetailRequestActivity.launchIIssueDetailRequestActivity(HomeActivity.this);
                                                    break;
                                                default:
                                                    break;
                                            }

                                        }
                                    });
                            builder.create().show();
                        } else {
                            new AlertDialog.Builder(HomeActivity.this).setTitle("提示").setMessage("您还没有进行身份信息认证，暂时不能发布，请前往个人中心-->我的认证 。进行认证操作").setPositiveButton("知道了", null).show();
                        }
                        break;
                    case R.id.navigation_item_quit:
                        LoginActivity.launchLoginActivity(HomeActivity.this);
                        finish();
                    default:
                        break;
                }
                mPreItem = item;
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                item.setChecked(false);
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
//        Log.i("ssssss", "switchFragment: "+fragment);
//        try{
//            mFragmentManager.beginTransaction().remove(FragmentManagerWrapper.getInstance().createFragment(RequestListFragment.class));
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        if (fragment.isAdded()) {
            //mFragmentManager.beginTransaction().hide(mCurrentFragment).show(fragment).commitAllowingStateLoss();
            mFragmentManager.beginTransaction().hide(mCurrentFragment).show(fragment).commitAllowingStateLoss();
        } else {
            mFragmentManager.beginTransaction().hide(mCurrentFragment).add(R.id.fl_main_content, fragment).commitAllowingStateLoss();
        }
        mCurrentFragment = fragment;
    }


    private static boolean isExit = false;

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    /**
     * 监听返回键 直接返回桌面
     *
     * @param keyCode
     * @param event
     * @return
     */

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            ToastUitls.showShortToast(HomeActivity.this, "再按一次退出");
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
        }
    }

    @Override
    protected void initData() {
        //// TODO
    }

    //获取登陆用户
    public User getUser() {
        User user = null;
        Intent intent = new Intent();
        String phone = intent.getStringExtra("userPhone");
        if (phone != null)
            user = User.findUser(phone);
        if (user != null)
            username.setText(username.getText());
        else
            username.setText("未登录");
        return user;
    }

    //跳转进来
    public static void launchDetaiListActivity(Context context, int channelId) {
        Intent intent = new Intent(context, HomeActivity.class);
        //防止多次实例调用
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(CHANNEL_ID, channelId);
        context.startActivity(intent);
    }

    public void launchHomeActivityCertificate(Context context) {
        Intent intent = new Intent(context, HomeActivity.class);
        //防止多次实例调用
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        mFragmentManager = getSupportFragmentManager();
        //初始化传入的homefragment
        mCurrentFragment = FragmentManagerWrapper.getInstance().createFragment(CertificateFragment.class, true);
        //fragmentmanager 事务的回滚
        mFragmentManager.beginTransaction().add(R.id.fl_main_content, mCurrentFragment).commit();
        context.startActivity(intent);
    }

}
