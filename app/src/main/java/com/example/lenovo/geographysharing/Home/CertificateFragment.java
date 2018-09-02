package com.example.lenovo.geographysharing.Home;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lenovo.geographysharing.BaseClass.BaseFragment;
import com.example.lenovo.geographysharing.Element.User;
import com.example.lenovo.geographysharing.R;
import com.example.lenovo.geographysharing.Utils.LoginUserRegisterUtil;
import com.example.lenovo.geographysharing.Welcom.LoginActivity;
import com.example.lenovo.geographysharing.identify.IdentifyAPI;
import com.example.lenovo.geographysharing.identify.IdentifyActivity;

/**
 * Created by lenovo on 2017/12/31.
 */

public class CertificateFragment extends BaseFragment {

    Button finish;
    EditText cardId;
    EditText name;
    String user_id;
    String user_name;
//    Boolean ifSuccess = true ;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_certificate;
    }

    @Override
    protected void initView() {
        cardId=bindViewId(R.id.id_card_numbers);
        name=bindViewId(R.id.moreinformation_name);
        finish=bindViewId(R.id.moreinformation_btn_ok);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user_id=cardId.getText().toString();
                user_name=name.getText().toString();
                final String[] result = new String[1];
                //判断一下是否认证成功
                    Runnable identifyRunnable = new Runnable() {
                        @Override
                        public void run() {
                            try{

                                result[0] = IdentifyAPI.info(user_name,user_id);
                            }catch (Exception e){
                                Intent intent=new Intent(getActivity(), IdentifyActivity.class);
                                intent.putExtra("id",0);
                                startActivity(intent);
                                getActivity().finish();
                            }
                            //获取认证结果集
                            Message message = new Message();
                            message.obj = result[0];
                            mHandler.sendMessage(message);
                        }
                    };
                    Thread identifyThread = new Thread(identifyRunnable);
                    identifyThread.start();
                }

        });

    }

    private Handler mHandler = new Handler(){
        public void handleMessage(Message msg){
            String result = (String)msg.obj;
            String[] b =result.split("\"res\":");     //字符分割
            if(b.length == 2) {
                char t = b[1].charAt(0);
                if ( t == '1') {
                    Intent intent = new Intent(getActivity(), IdentifyActivity.class);
                    intent.putExtra("id", 1);
                    startActivity(intent);
                    getActivity().finish();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                User.completeUserInfo(LoginUserRegisterUtil.user.getPhone(), LoginActivity.sp.getString("PASSWORD", ""),user_name,user_id,true);
                            }
                            catch (Exception e){
                                e.printStackTrace();
                                Intent intent=new Intent(getActivity(), IdentifyActivity.class);
                                intent.putExtra("id",0);
                                startActivity(intent);
                                getActivity().finish();
                            }

                        }
                    }).start();

                }
                else{
                    //不匹配
                    Intent intent=new Intent(getActivity(), IdentifyActivity.class);
                    intent.putExtra("id",0);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
            else{
                //不匹配
                Intent intent=new Intent(getActivity(), IdentifyActivity.class);
                intent.putExtra("id",0);
                startActivity(intent);
                getActivity().finish();
            }

        }
    };

    @Override
    protected void initData() {

    }
}
