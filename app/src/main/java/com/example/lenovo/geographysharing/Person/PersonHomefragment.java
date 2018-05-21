package com.example.lenovo.geographysharing.Person;

import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.lenovo.geographysharing.BaseClass.BaseFragment;
import com.example.lenovo.geographysharing.Home.HomeActivity;
import com.example.lenovo.geographysharing.R;
import com.example.lenovo.geographysharing.Utils.JsonDataUtil;
import com.example.lenovo.geographysharing.Utils.LoginUserRegisterUtil;
import com.example.lenovo.geographysharing.others.FragmentManagerWrapper;

import org.w3c.dom.Text;

/**
 * Created by lenovo on 2018/4/20.
 */

public class PersonHomefragment extends BaseFragment {
    private ImageButton imageToPersonData = null;
    private ImageButton imageToMyIssued = null;
    private ImageButton imageToMyFav = null;
    private ImageButton imageToMyRequest = null;
    private ImageButton imageToMyOrder = null;
    TextView userNickName;

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
        if (LoginUserRegisterUtil.user != null) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    imageToPersonData.setImageBitmap(JsonDataUtil.getImage(LoginUserRegisterUtil.user.getImageURL()));
                }
            });
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (LoginUserRegisterUtil.user != null)
                userNickName.setText(LoginUserRegisterUtil.user.getNick_name());
            else
                userNickName.setText("未登录");
        }
        handleViewListener();
    }

    private void handleViewListener() {

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
                MyissuedActivity.launchMyIssueActivity(getActivity());
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
                MyOrderActivity.launchMyOrderActivity(getActivity());
            }
        });


    }


    @Override
    protected void initData() {

    }
}
