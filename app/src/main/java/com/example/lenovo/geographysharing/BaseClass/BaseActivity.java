package com.example.lenovo.geographysharing.BaseClass;

import android.Manifest;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lenovo.geographysharing.Element.User;
import com.example.lenovo.geographysharing.Home.HomeActivity;
import com.example.lenovo.geographysharing.Person.PersonDataActivity;
import com.example.lenovo.geographysharing.R;
import com.example.lenovo.geographysharing.Utils.LoginUserRegisterUtil;
import com.example.lenovo.geographysharing.Utils.MPermissionUtils;
import com.example.lenovo.geographysharing.Utils.ToastUitls;
import com.example.lenovo.geographysharing.Welcom.LoginActivity;

import java.util.List;


/**
 * Created by hejunlin on 17/3/19.
 */

public abstract class BaseActivity extends AppCompatActivity {
    static User user;
    protected Toolbar mToolBar;
    protected ImageView home_share_imageview = null;
    private ImageView imageShare;
    private ImageView imageShare_liu;
    private TextView imageShare_liu_wenzi;
    private android.app.AlertDialog mDialog = null;

    /**
     * 7-6 Token
     */
    public BaseActivity(){
        LoginUserRegisterUtil.TOKEN= LoginActivity.sp.getString("USER_TOKEN","");
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(getLayoutId());
        getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        try {
            initView();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        initData();

        MPermissionUtils.requestPermissionsResult(BaseActivity.this, 1,
                new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE},
                new MPermissionUtils.OnPermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        //getPhotosTask.execute();
                    }
                    @Override
                    public void onPermissionDenied() {
                        MPermissionUtils.showTipsDialog(BaseActivity.this);
                    }
                });
    }

    protected abstract int getLayoutId();
    protected abstract void initView() throws InterruptedException;
    protected abstract void initData();

    protected <T extends View> T bindViewId(int resId){
        return (T) findViewById(resId);
    }

    protected void setSupportActionBar(){
        mToolBar = bindViewId(R.id.toolbar);
        home_share_imageview = bindViewId(R.id.id_home_share);
        home_share_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
        if (mToolBar != null) {

            setSupportActionBar(mToolBar);
        }
    }

    /**
     * 7-8
     * 复制文字，分享
     */

    private void showDialog(){
        mDialog = new android.app.AlertDialog.Builder(BaseActivity.this).create();
        mDialog.show();
        final Window window = mDialog.getWindow();
        if (window != null) {
            window.setContentView(R.layout.home_share_dialog);
            window.setGravity(Gravity.BOTTOM);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            //设置属性
            final WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            window.setAttributes(params);
            imageShare =(ImageView) window.findViewById(R.id.id_share_share);
            imageShare_liu =(ImageView) window.findViewById(R.id.id_share_liu);
            imageShare_liu_wenzi =(TextView) window.findViewById(R.id.id_share_liu_wenzi);

            window.findViewById(R.id.id_share_liu).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    shareActivity.launchshareActivity(BaseActivity.this);
                }
            });
            window.findViewById(R.id.id_share_liu_wenzi).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    shareActivity.launchshareActivity(BaseActivity.this);
                }
            });
            window.findViewById(R.id.id_share_share).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    // 将文本内容放到系统剪贴板里。
                    cm.setText("http://59.110.172.7/Download/GeoShareTemp.apk");
                    mDialog.dismiss();
                    new AlertDialog.Builder(BaseActivity.this).setTitle("提示").setMessage("分享链接已复制，可以去微信粘贴，即将启动微信。。").setPositiveButton("去粘贴", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if(isWeixinAvilible(BaseActivity.this)){
                                Intent intent = new Intent();
                                ComponentName cmp=new ComponentName("com.tencent.mm","com.tencent.mm.ui.LauncherUI");
                                intent.setAction(Intent.ACTION_MAIN);
                                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.setComponent(cmp);
                                startActivity(intent);
                            }else {
                                ToastUitls.showShortToast(BaseActivity.this,"启动微信失败");
                            }

                        }
                    }).setNegativeButton("暂时不", null).show();
                }
            });




        }

    }

    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }

    protected void setSupportArrowActionBar(boolean isSupport){
        getSupportActionBar().setDisplayHomeAsUpEnabled(isSupport);
    }

    protected void setActionBarIcon(int resId){
        if (mToolBar != null){
            mToolBar.setNavigationIcon(resId);
        }
    }

}
