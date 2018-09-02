package com.example.lenovo.geographysharing.Issue;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.geographysharing.BaseClass.BaseActivity;
import com.example.lenovo.geographysharing.Element.Demand;
import com.example.lenovo.geographysharing.Element.Software;
import com.example.lenovo.geographysharing.R;
import com.example.lenovo.geographysharing.Utils.CompressImageUtil;
import com.example.lenovo.geographysharing.Utils.LoginUserRegisterUtil;
import com.example.lenovo.geographysharing.Utils.TimeUtil;
import com.example.lenovo.geographysharing.Utils.ToastUitls;

import java.util.Calendar;

/**
 * Created by shdeng on 2018-7-8.
 */

public class IssuDetailsoftActivity extends BaseActivity {
    private Spinner requestSpinner = null;
    private TextView tv_request_classify = null;

    private Button btn_issue_request = null;
    private EditText edt_request_name = null;
    private EditText edt_request_content = null;
    private EditText edt_request_expense = null;
    private EditText edt_request_address = null;
    private EditText edt_request_comment = null;
    Context context = this;

    String[] spinnerItems;

    boolean isSuccess;

    private static final String DEMAND = "demand";
    private Software software;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_issue_soft;
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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setSupportActionBar();//表示当前页面支持ActionBar
        setSupportArrowActionBar(true);
        setTitle("发布需求");
        edt_request_name = bindViewId(R.id.issue_soft_name);
        edt_request_content = bindViewId(R.id.issue_soft_content);//内容
        edt_request_expense = bindViewId(R.id.issue_soft_expense);//租用价格
        edt_request_address = bindViewId(R.id.issue_soft_address);//运行平台
        btn_issue_request = bindViewId(R.id.issue_request_button);
        edt_request_comment = bindViewId(R.id.issue_soft_comment);//备注
        btn_issue_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CompressImageUtil.hideInput(IssuDetailsoftActivity.this, view);
                Calendar now = Calendar.getInstance();
                final String nowDate = now.get(Calendar.YEAR) + "年" + (now.get(Calendar.MONTH) + 1) + "月" + now.get(Calendar.DAY_OF_MONTH) + "日";
                if (checkForm()) {
                    final ProgressDialog proDialog = android.app.ProgressDialog.show(IssuDetailsoftActivity.this, null, "发布中。。");

                    final Handler handler = new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            super.handleMessage(msg);

                            if (isSuccess) {

                                ToastUitls.showShortToast(IssuDetailsoftActivity.this,"发布成功");
                                proDialog.dismiss();
                                finish();

//                                new android.support.v7.app.AlertDialog.Builder(IssuDetailsoftActivity.this).setTitle("提示").setMessage("发布成功，请等待审核，是否继续发布？").setPositiveButton("是", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialogInterface, int i) {
//                                        edt_request_name.getText().clear();
//                                        edt_request_content.getText().clear();
//                                        edt_request_expense.getText().clear();
//                                        edt_request_address.getText().clear();
//                                        edt_request_comment.getText().clear();
//                                    }
//                                }).setNegativeButton("否", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialogInterface, int i) {
//                                        finish();
//                                    }
//                                }).show();

                            } else {

                                ToastUitls.showShortToast(IssuDetailsoftActivity.this,"发布成功");
                                proDialog.dismiss();
                                finish();
                            }
                        }

                    };

                    //        edt_request_name = bindViewId(R.id.issue_soft_name);
//                        edt_request_content = bindViewId(R.id.issue_soft_content);//内容
//                        edt_request_expense = bindViewId(R.id.issue_soft_expense);//租用价格
//                        edt_request_address = bindViewId(R.id.issue_soft_address);//运行平台
//                        btn_issue_request = bindViewId(R.id.issue_request_button);
//                        edt_request_comment = bindViewId(R.id.issue_soft_comment);//备注
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            isSuccess = Software.issueSoftware(
                                    edt_request_name.getText().toString(),
                                    1000.0,
                                    "2018-1-1T1:1:1",
                                    "2018-1-1T1:1:1",
                                    Double.parseDouble(edt_request_expense.getText().toString()),
                                    edt_request_content.getText().toString(),
                                    edt_request_address.getText().toString(),
                                    edt_request_comment.getText().toString(),
                                    1,
                                    LoginUserRegisterUtil.user.getId(),
                                    5
                            );
                            Message message = handler.obtainMessage();
                            handler.sendMessage(message);
                        }
                    });
                    Log.i(nowDate, "时间格式");
                    thread.start();
                } else {
                    ToastUitls.showShortToast(IssuDetailsoftActivity.this, "请完善信息");
                }
            }
        });

    /*
    * 以下被注释的方法适合一个button时用
    * */
//        btn_Upload_Pic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Chose_Pic_or_Cam();
//            }
//        });
        Intent intent = this.getIntent();
        if (intent != null) {
            software = (Software) intent.getSerializableExtra(DEMAND);
        }
    }


    @Override
    protected void initData() {
        if (software != null) {
            edt_request_name.setText(software.getName());
            edt_request_content.setText(software.getComment());
            edt_request_expense.setText(software.getPrice().toString());
            edt_request_address.setText(software.getOwner().getAddress());
        }
    }

    public static void launchIIssueDetailSoftActivity(Activity activity) {
        Intent intent = new Intent(activity, IssuDetailsoftActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        activity.startActivity(intent);

    }

    public static void launchIIssueDetailRequestActivity(Activity activity, Software demand) {
        Intent intent = new Intent(activity, IssueDetailRequestActivity.class);
        //防止多次实例调用
        /**
         * 传一个对象
         */
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        Bundle bundle = new Bundle();
        bundle.putSerializable(DEMAND, demand);
        intent.putExtras(bundle);
        activity.startActivity(intent);

    }

    private boolean checkForm() {
        final String name = edt_request_name.getText().toString();
        final String content = edt_request_content.getText().toString();
        final String expense = edt_request_expense.getText().toString();
        final String address = edt_request_address.getText().toString();

        boolean isPass = true;

        if (name.isEmpty()) {
            edt_request_name.setError("请输入软件名称!");
            isPass = false;
        } else {
            edt_request_name.setError(null);
        }
        if (content.isEmpty()) {
            edt_request_content.setError("请描述一下软件的应用领域!");
            isPass = false;
        } else {
            edt_request_content.setError(null);
        }

        if (expense.isEmpty()) {
            edt_request_expense.setError("请填写求租价格（¥/天）!");
            isPass = false;
        } else {
            edt_request_expense.setError(null);
        }

        if (address.isEmpty()) {
            edt_request_address.setError("请填写软件所在地!");
            isPass = false;
        } else {
            edt_request_address.setError(null);
        }
        return isPass;

    }
}
