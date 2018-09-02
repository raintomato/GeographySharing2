package com.example.lenovo.geographysharing.Person;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.geographysharing.BaseClass.BaseFragment;
import com.example.lenovo.geographysharing.Home.HomeActivity;
import com.example.lenovo.geographysharing.Home.IssueFragment;
import com.example.lenovo.geographysharing.Issue.IssueDetailRequestActivity;
import com.example.lenovo.geographysharing.R;
import com.example.lenovo.geographysharing.Utils.JsonDataUtil;
import com.example.lenovo.geographysharing.Utils.LoginUserRegisterUtil;
import com.example.lenovo.geographysharing.Utils.ToastUitls;
import com.example.lenovo.geographysharing.Welcom.LoginActivity;
import com.example.lenovo.geographysharing.others.FragmentManagerWrapper;

import org.w3c.dom.Text;

/**
 * Created by lenovo on 2018/4/20.
 */

public class PersonHomefragment extends BaseFragment {
    public static ImageButton imageToPersonData = null;
    private ImageButton imageToMyIssued = null;
    private ImageButton imageToMyFav = null;
    private ImageButton imageToMyRequest = null;
    private ImageButton imageToMyOrder = null;

    private TextView imageToMyCertificate = null;
    private TextView imageToMyCredit = null;
    private TextView imageToMyScore = null;
    private Bitmap tempBitmap;
    public static Boolean isSuccess;
    public static Boolean isSuccessTemp=false;

    public static TextView userNickName;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_personhome;
    }


    @Override
    protected void initView() {
        //初始化控件
        imageToPersonData = bindViewId(R.id.img_to_persondata);
        imageToMyIssued = bindViewId(R.id.btn_to_my_issued);
        imageToMyFav = bindViewId(R.id.btn_to_my_fav);
        imageToMyRequest = bindViewId(R.id.btn_to_my_request);
        imageToMyOrder = bindViewId(R.id.btn_to_my_order);
        userNickName = bindViewId(R.id.person_home_nickname);


        imageToMyCertificate = bindViewId(R.id.my_person_Score);
        imageToMyCredit = bindViewId(R.id.my_person_integrate);
        imageToMyScore = bindViewId(R.id.person_list_myTrustWorth);

        if (LoginUserRegisterUtil.user != null) {
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
                imageToPersonData.setImageResource(R.drawable.tubiao);
            } else {
                imageToPersonData.setImageBitmap(LoginUserRegisterUtil.photo);
            }

            if (LoginUserRegisterUtil.user != null)
                userNickName.setText(LoginUserRegisterUtil.user.getNick_name());
            else
                userNickName.setText("未登录");
        }
        handleViewListener();
    }

    private void handleViewListener() {

        isSuccess = LoginUserRegisterUtil.user.isComplete();

//头像的点击事件，进入个人信息编辑界面
        imageToPersonData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PersonDataActivity.LaunchPersonDataActivity(getActivity());
            }
        });
//监听事件，我的发布
        imageToMyIssued.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext())
                        .setTitle("              选择查看类型")
                        .setItems(new String[]{"设备", "软件","人员"}, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        MyissuedActivity.launchMyIssueActivity(getActivity(),"设备");
                                        break;
                                    case 1:
                                        MyissuedActivity.launchMyIssueActivity(getActivity(),"软件");
                                        break;
                                    case 2:
                                        MyissuedActivity.launchMyIssueActivity(getActivity(),"人员");
                                        break;
                                    default:
                                        break;
                                }

                            }
                        });
                builder.create().show();
            }
        });
//监听事件，我的收藏
        imageToMyFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyFavActivity.launchMyFavActivity(getActivity());
            }
        });
//监听事件，我的需求
        imageToMyRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    MyRequestActivity.launchMyRequestActivity(getActivity());
            }
        });
//监听事件，我的订单
        imageToMyOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUitls.showShortToast(getContext(),"待开发");
//               MyOrderActivity.launchMyOrderActivity(getActivity());
            }
        });
//监听事件，跳转我的认证
        imageToMyCertificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * 8-21记得一定改回来
                 */
                isSuccessTemp = LoginActivity.sp.getBoolean("IFTEMP",false);
                if (isSuccessTemp||isSuccess) {
                    ToastUitls.showShortToast(getContext(),"您已认证完毕，不能重复认证！");
                } else {
                    MyCertificateActivity.launchMyCertificateActivity(getActivity());
                }

            }
        });
//监听事件，跳转我的信用
        imageToMyCredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUitls.showShortToast(getActivity(),"待开发");
            }
        });
//监听事件，跳转我的积分
        imageToMyScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUitls.showShortToast(getActivity(),"待开发");
            }
        });
    }


    @Override
    protected void initData() {

    }
}
