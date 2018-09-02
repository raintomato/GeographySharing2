package com.example.lenovo.geographysharing.Issue;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
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
import com.example.lenovo.geographysharing.Element.Equipment;
import com.example.lenovo.geographysharing.R;
import com.example.lenovo.geographysharing.Utils.CompressImageUtil;
import com.example.lenovo.geographysharing.Utils.LoginUserRegisterUtil;
import com.example.lenovo.geographysharing.Utils.TimeUtil;
import com.example.lenovo.geographysharing.Utils.ToastUitls;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by shdeng on 2018/4/20.
 * 需求发布的详情界面
 */

public class IssueDetailRequestActivity extends BaseActivity {
    private Spinner requestSpinner = null;
    private TextView tv_request_classify = null;

    private Button btn_issue_request = null;
    private EditText edt_request_name = null;
    private EditText edt_request_content = null;
    private EditText edt_request_expense = null;
    private EditText edt_request_address = null;
    private TextView tv_one;
    private TextView tv_two;
    private TextView tv_three;
    private TextView tv_four;
    private TextView tv_five;
    private TextView tv_six;
    private EditText et_one;
    private EditText et_two;
    private EditText et_three;
    private EditText et_four;
    private EditText et_five;
    private EditText et_six;
    Context context=this;
    int type=0;
    String[] spinnerItems;

    boolean isSuccess;

    private static final String DEMAND = "demand";
    private Demand demand;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_issue_requirements;
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


        spinnerItems = getResources().getStringArray(R.array.request_classify);

        //设置ArrayAdapter内置的item样式-这里是单行显示样式
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerItems);
        //这里设置的是Spinner的样式 ， 输入 simple_之后会提示有4人，如果专属spinner的话应该是俩种，在特殊情况可自己定义样式
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        //设置Adapter了
        requestSpinner.setAdapter(adapter);
        //监听Spinner的操作
        requestSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //选取时候的操作
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tv_request_classify.setText(adapter.getItem(position));
                switch (position){
                    case 0:
                        type=0;
                        tv_four.setVisibility(View.VISIBLE);
                        et_four.setVisibility(View.VISIBLE);
                        tv_one.setText("设备名称");
                        tv_two.setText("租用时间");
                        tv_three.setText("租用地点");
                        tv_four.setText("备注");
                        tv_five.setVisibility(View.INVISIBLE);
                        et_five.setVisibility(View.INVISIBLE);

                        tv_six.setVisibility(View.INVISIBLE);
                        et_six.setVisibility(View.INVISIBLE);
                        break;
                    case 1:
                        type=1;
                        tv_one.setText("软件名称");
                        tv_two.setText("软件用途");
                        tv_three.setText("备注");
                        tv_four.setText("");
                        tv_four.setVisibility(View.INVISIBLE);
                        et_four.setVisibility(View.INVISIBLE);

                        tv_five.setVisibility(View.INVISIBLE);
                        et_five.setVisibility(View.INVISIBLE);

                        tv_six.setVisibility(View.INVISIBLE);
                        et_six.setVisibility(View.INVISIBLE);

                        break;
                    case 2:
                        type=2;
                        tv_four.setVisibility(View.VISIBLE);
                        et_four.setVisibility(View.VISIBLE);

                        tv_five.setVisibility(View.VISIBLE);
                        et_five.setVisibility(View.VISIBLE);

                        tv_six.setVisibility(View.VISIBLE);
                        et_six.setVisibility(View.VISIBLE);

                        tv_one.setText("应用领域");
                        tv_two.setText("雇用时间");
                        tv_three.setText("雇用人数");
                        tv_four.setText("雇用要求");
                        tv_five.setText("雇用价格");
                        tv_six.setText("备注");
                        break;
                }
            }

            //没被选取时的操作
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                tv_request_classify.setText("未选择");
            }
        });
    }

    @Override
    protected void initView() {
        setSupportActionBar();//表示当前页面支持ActionBar
        setSupportArrowActionBar(true);
        setTitle("发布需求");

        requestSpinner = bindViewId(R.id.request_classify_spinner);
        tv_request_classify = bindViewId(R.id.tv_request_classify);

        edt_request_name = bindViewId(R.id.issue_request_name);
        edt_request_content = bindViewId(R.id.issue_request_content);
        btn_issue_request = bindViewId(R.id.issue_request_button);

        //7-9
        tv_one=bindViewId(R.id.txt_name1);
        tv_two=bindViewId(R.id.txt_time2);
        tv_three=bindViewId(R.id.txt_adress3);
        tv_four=bindViewId(R.id.txt_comment4);
        tv_five=bindViewId(R.id.txt_comment5);
        tv_six=bindViewId(R.id.txt_comment6);
        et_one=bindViewId(R.id.issue_name1);
        et_two=bindViewId(R.id.issue_time2);
        et_three=bindViewId(R.id.issue_address3);
        et_four=bindViewId(R.id.issue_comment4);
        et_five=bindViewId(R.id.issue_comment5);
        et_six=bindViewId(R.id.issue_comment6);


        btn_issue_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CompressImageUtil.hideInput(IssueDetailRequestActivity.this,view);
                final ProgressDialog proDialog = android.app.ProgressDialog.show(IssueDetailRequestActivity.this,null, "发布中。。");
                Calendar now = Calendar.getInstance();
                final String nowDate = now.get(Calendar.YEAR) + "年" + (now.get(Calendar.MONTH) + 1) + "月" + now.get(Calendar.DAY_OF_MONTH) + "日";

                    final Handler handler = new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            super.handleMessage(msg);

                                if (isSuccess) {

                                    ToastUitls.showLongToast(IssueDetailRequestActivity.this,"发布成功");
                                    proDialog.dismiss();
                                    finish();
//                                    new android.support.v7.app.AlertDialog.Builder(IssueDetailRequestActivity.this).setTitle("提示").setMessage("发布成功，请等待审核，是否继续发布？").setPositiveButton("是", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialogInterface, int i) {
//
//                                        }
//                                    }).setNegativeButton("否", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialogInterface, int i) {
//                                            finish();
//                                        }
//                                    }).show();
                                } else {
                                    proDialog.dismiss();
                                    Toast.makeText(IssueDetailRequestActivity.this, "请填全信息", Toast.LENGTH_SHORT).show();
                                }
                            }

                    };
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String content;
                        if(et_one.getText().toString()!=""&&et_two.getText().toString()!=""&&et_three.getText().toString()!=""&&
                                et_four.getText().toString()!=""&&et_five.getText().toString()!=""&&et_six.getText().toString()!=""&&type==2) {
                                 content = edt_request_content.getText().toString() + "\\n" + tv_one.getText().toString() + ":" + et_one.getText().toString() + "\\n"
                                    + tv_two.getText().toString() + ":" + et_two.getText().toString() + "\\n" +
                                    tv_three.getText().toString() + ":" + et_three.getText().toString() + "\\n" +
                                    tv_four.getText().toString() + ":" + et_four.getText().toString() + "\\n" +
                                    tv_five.getText().toString() + ":" + et_five.getText().toString() + "\\n" +
                                    tv_six.getText().toString() + ":" + et_six.getText().toString() + "\\n";
                        }else if(et_one.getText().toString()!=""&&et_two.getText().toString()!=""&&et_three.getText().toString()!=""&&type==1){
                            content = edt_request_content.getText().toString() + "\\n" + tv_one.getText().toString() + ":" + et_one.getText().toString() + "\\n"
                                    + tv_two.getText().toString() + ":" + et_two.getText().toString() + "\\n" +
                                    tv_three.getText().toString() + ":" + et_three.getText().toString() + "\\n";
                        }else if(et_one.getText().toString()!=""&&et_two.getText().toString()!=""&&et_three.getText().toString()!=""&&
                                et_four.getText().toString()!=""&&type==0){
                            content = edt_request_content.getText().toString() + "\\n" + tv_one.getText().toString() + ":" + et_one.getText().toString() + "\\n"
                                    + tv_two.getText().toString() + ":" + et_two.getText().toString() + "\\n" +
                                    tv_three.getText().toString() + ":" + et_three.getText().toString() + "\\n" +
                                    tv_four.getText().toString() + ":" + et_four.getText().toString() + "\\n";
                        }else {
                            content=null;
                        }
                        if(content!=null) {
                            isSuccess = Demand.addDemand((int) (Math.random() * 1000000000),
                                    tv_request_classify.getText().toString().substring(0,2),
                                    edt_request_name.getText().toString()
                                    , content
                                    , TimeUtil.checkTime(nowDate)
//                                ,"Time"
                                    , "0"
                                    , LoginUserRegisterUtil.user.getId(),
                                    "无"
                            );
                        }else {
                            isSuccess=false;
                        }
                        Message message = handler.obtainMessage();
                        handler.sendMessage(message);
                    }
                });
                Log.i(nowDate, "时间格式");
                thread.start();


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
            demand = (Demand) intent.getSerializableExtra(DEMAND);
        }
    }


    @Override
    protected void initData() {
        if(demand!=null){
            try{
                edt_request_name.setText(demand.getTitle());
                edt_request_content.setText(demand.getContent());
                edt_request_expense.setText(demand.getMoney());
                edt_request_address.setText(demand.getDemander().getAddress());
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static void launchIIssueDetailRequestActivity(Activity activity) {
        Intent intent = new Intent(activity, IssueDetailRequestActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        activity.startActivity(intent);

    }

    public static void launchIIssueDetailRequestActivity(Activity activity, Demand demand) {
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
            edt_request_name.setError("请输入需求名称!");
            isPass = false;
        } else {
            edt_request_name.setError(null);
        }

        if (content.isEmpty()) {
            edt_request_content.setError("请描述一下需求的故事或者细节!");
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
            edt_request_address.setError("请填写需求所在地!");
            isPass = false;
        } else {
            edt_request_address.setError(null);
        }

        return isPass;

    }

}
